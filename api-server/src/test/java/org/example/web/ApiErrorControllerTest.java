package org.example.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ApiErrorControllerTest {

    @Mock
    private ApiExceptionResolver mockApiExceptionResolver;

    private ApiErrorController apiErrorController;

    @BeforeEach
    void setUp() {
        this.apiErrorController = new ApiErrorController(this.mockApiExceptionResolver);
    }

    @Test
    void sendErrorSucceeds() {
        this.apiErrorController.sendError(mock(HttpServletRequest.class), mock(HttpServletResponse.class));
        verify(this.mockApiExceptionResolver).handleAnyGenericException(any(HttpServletRequest.class), any(HttpServletResponse.class), any());
    }
}
