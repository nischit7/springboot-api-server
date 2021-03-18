package org.example.security.config;

import org.example.security.AuthTokenFilter;
import org.example.security.JwtEntryPoint;
import org.example.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring security config.
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "org.example.security")
public class SecurityConfig {

    @Order(1)
    @Configuration
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtEntryPoint jwtEntryPoint;

        @Autowired
        private JwtHelper jwtHelper;

        @Qualifier("myUserDetailsService")
        @Autowired
        private UserDetailsService userDetailsService;

        public ApiSecurityConfig() {
            super(true);
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            final AuthTokenFilter authTokenFilter = new AuthTokenFilter(
                    this.jwtHelper,
                    this.userDetailsService,
                    this.jwtEntryPoint);

            http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/services/api/setup/**")
                .authorizeRequests()
                .antMatchers("/services/api/setup/**").hasAnyRole("ADMIN", "ROLE_ADMIN")
                .and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(this.jwtEntryPoint)
                .and()
                .csrf(csrf -> csrf.disable());
        }

        @Override
        public void configure(final WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/manage/**");
        }
    }

    public static class NoSecurityConfig extends WebSecurityConfigurerAdapter {

        public NoSecurityConfig() {
            super(true);
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/manage/**")
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .and()
                .anonymous();
        }
    }
}
