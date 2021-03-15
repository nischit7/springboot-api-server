package org.example.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.support.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * An error handle for endpoint "/error".
 */
@Controller
public class ApiErrorController {

    private ApiExceptionResolver apiExceptionResolver;

    @Autowired
    public ApiErrorController(final ApiExceptionResolver apiExceptionResolver) {
        this.apiExceptionResolver = apiExceptionResolver;
    }

    /**
     * Reports the error body when "/error" is invoked.
     *
     * @param request An instance of {@link HttpServletRequest}.
     * @param response An instance of {@link HttpServletResponse}.
     * @return An instance of {@link ResponseEntity}.
     */
    @RequestMapping(value = ApiUrls.ERROR_CONFIG_URI)
    public ResponseEntity<String> sendError(final HttpServletRequest request, final HttpServletResponse response) {
        return apiExceptionResolver.handleAnyGenericException(
                request,
                response,
                (Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
    }

}
