package org.example.web;

import java.util.Locale;

import javax.servlet.RequestDispatcher;

import org.example.localization.Messages;
import org.example.services.impl.TeamNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiExceptionResolverTest {

    @Mock
    private Messages mockMessages;

    private ApiExceptionResolver apiExceptionResolver;

    @BeforeEach
    public void setUp() {
        this.apiExceptionResolver = new ApiExceptionResolver(mockMessages);
    }

    @Test
    @DisplayName("When application exception is thrown")
    public void handleApplicationExceptionSucceeds() {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        when(mockMessages.getLocalizedErrorMessage(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Locale.class),
                ArgumentMatchers.any()))
                .thenReturn("my error msg");

        final ResponseEntity<String> responseEntity = this.apiExceptionResolver.handleApplicationException(
                mockHttpServletRequest,
                mockHttpServletResponse,
                new TeamNotFoundException());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE),
                equalTo(HttpStatus.NOT_FOUND));
        assertThat(mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                equalTo(TeamNotFoundException.class));
    }

    @Test
    @DisplayName("When bad request exception is thrown")
    public void handleBadRequestExceptionSucceeds() {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        when(mockMessages.getLocalizedErrorMessage(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Locale.class),
                ArgumentMatchers.any()))
                .thenReturn("my error msg");

        final ResponseEntity<String> responseEntity = this.apiExceptionResolver.handleBadRequestException(
                mockHttpServletRequest, mockHttpServletResponse, new IllegalArgumentException());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE),
                equalTo(HttpStatus.BAD_REQUEST));
        assertThat(mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                equalTo(IllegalArgumentException.class));
    }

    @Test
    @DisplayName("When media type not supported exception is thrown")
    public void handleHttpMediaTypeNotSupportedExceptionSucceeds() {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        when(mockMessages.getLocalizedErrorMessage(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Locale.class),
                ArgumentMatchers.any()))
                .thenReturn("my error msg");

        final ResponseEntity<String> responseEntity =
                this.apiExceptionResolver.handleHttpMediaTypeNotSupportedException(
                    mockHttpServletRequest,
                    mockHttpServletResponse,
                    new HttpMediaTypeNotSupportedException("application/json"));

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(
                mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE),
                equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        assertThat(
                mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                equalTo(HttpMediaTypeNotSupportedException.class));
    }

    @Test
    @DisplayName("When media type not acceptable exception is thrown")
    public void handleHttpMediaTypeNotAcceptableExceptionSucceeds() {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        when(mockMessages.getLocalizedErrorMessage(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Locale.class),
                ArgumentMatchers.any()))
                .thenReturn("my error msg");

        final ResponseEntity<String> responseEntity =
                this.apiExceptionResolver.handleHttpMediaTypeNotAcceptableException(
                    mockHttpServletRequest,
                    mockHttpServletResponse,
                    new HttpMediaTypeNotAcceptableException("application/json"));

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_ACCEPTABLE));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(
                mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE),
                equalTo(HttpStatus.NOT_ACCEPTABLE));
        assertThat(
                mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                equalTo(HttpMediaTypeNotAcceptableException.class));
    }

    @Test
    @DisplayName("When any unhandled error is thrown")
    public void handleAnyGenericException() {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        when(mockMessages.getLocalizedErrorMessage(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Locale.class),
                ArgumentMatchers.any()))
                .thenReturn("my error msg");

        final ResponseEntity<String> responseEntity =
                this.apiExceptionResolver.handleAnyGenericException(
                    mockHttpServletRequest,
                    mockHttpServletResponse,
                    new NullPointerException());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(
                mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE),
                equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(
                mockHttpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                equalTo(NullPointerException.class));
    }
}
