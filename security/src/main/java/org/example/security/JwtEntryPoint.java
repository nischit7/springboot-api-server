package org.example.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Denies the user by flagging as unauthorized.
 */
@Component("jwtEntryPoint")
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final AuthenticationException e) throws IOException {
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Error: Unauthorized");
    }
}
