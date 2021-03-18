package org.example.app;

import org.example.config.EnableRestControllers;
import org.example.localization.config.EnableLocalization;
import org.example.persistence.sql.config.EnableSqlPersistence;
import org.example.security.config.EnableAuthentication;
import org.example.support.ApiUrls;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Starter application.
 */
@SpringBootApplication
@EnableLocalization
@EnableRestControllers
@EnableSqlPersistence
@EnableAuthentication
public class ApiApplication extends SpringBootServletInitializer {

    public static final String API_SERVLET_NAME = "API_SERVLET";
    public static final String API_SERVLET_REG_BEAN = "API_SERVLET_REG_BEAN";

    /**
     * The started method.
     *
     * @param args An array of {@link String}.
     */
    public static void main(final String[] args) {
        final SpringApplication application = new SpringApplication(ApiApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    /**
     * Returns an instance of {@link DispatcherServlet}.
     *
     * @return An instance of {@link DispatcherServlet}.
     */
    @Bean(name = API_SERVLET_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    /**
     * Returns an instance of {@link ServletRegistrationBean} for specified path params.
     *
     * @return An instance of {@link ServletRegistrationBean}.
     */
    @Bean(name = API_SERVLET_REG_BEAN)
    public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration() {
        final ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(
                    dispatcherServlet(),
                    ApiUrls.API_CONTEXT_PATTERN,
                    ApiUrls.ERROR_CONFIG_URI);
        registration.addUrlMappings(ApiUrls.API_CONTEXT_PATTERN);
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return registration;
    }
}
