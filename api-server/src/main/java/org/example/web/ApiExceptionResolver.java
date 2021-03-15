package org.example.web;

import java.util.Locale;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.example.localization.LocalizationParamValueException;
import org.example.localization.Messages;
import org.example.localization.MsgKey;
import org.example.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * A common error handler for all errors reported by API execution.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionResolver.class);

    private final Messages messages;

    @Autowired
    public ApiExceptionResolver(final Messages messages) {
        this.messages = messages;
    }

    /**
     * Handles exceptions of type {@link LocalizationParamValueException}.
     *
     * @param request An instance of {@link HttpServletRequest}.
     * @param response An instance of {@link HttpServletResponse}.
     * @param exception An instance of {@link Exception}.
     * @return An instance of {@link ResponseEntity}.
     */
    @ExceptionHandler(LocalizationParamValueException.class)
    public ResponseEntity<String> handleApplicationException(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Exception exception) {

        final LocalizationParamValueException ex = (LocalizationParamValueException)exception;
        final ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        final MsgKey msgKey = AnnotationUtils.findAnnotation(ex.getClass(), MsgKey.class);
        final String errorCode = Objects.nonNull(msgKey) ? msgKey.value() : Messages.SERVER_ERROR_KEY;

        final HttpStatus responseCode = responseStatus.value();
        final Object[] msgArgs = ex.getArgs();
        final String localizedMessage = getLocalizedMessage(request, errorCode, msgArgs);
        return handleAllExceptions(request, response, responseCode, errorCode, localizedMessage, ex);
    }

    /**
     * Handles exception of type of few types.
     * They are -
     * - {@link ConstraintViolationException}
     * - {@link HttpMessageNotReadableException}
     * - {@link IllegalArgumentException}
     * - {@link MethodArgumentNotValidException}
     * - {@link MethodArgumentTypeMismatchException}
     *
     * @param request An instance of {@link HttpServletRequest}.
     * @param response An instance of {@link HttpServletResponse}.
     * @param ex An instance of {@link Exception}.
     * @return An instance of {@link ResponseEntity}.
     */
    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> handleBadRequestException(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Exception ex) {
        return handleAllExceptions(request, response, HttpStatus.BAD_REQUEST, Messages.BAD_REQUEST_ERROR, ex);
    }

    /**
     * Handles exception of type of few types.
     * They are -
     * - {@link HttpMediaTypeNotSupportedException}
     *
     * @param request An instance of {@link HttpServletRequest}.
     * @param response An instance of {@link HttpServletResponse}.
     * @param ex An instance of {@link Exception}.
     * @return An instance of {@link ResponseEntity}.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpMediaTypeNotSupportedException ex) {
        return handleAllExceptions(
            request, response, HttpStatus.UNSUPPORTED_MEDIA_TYPE, Messages.UNSUPPORTED_MEDIA_TYPE_ERROR, ex);
    }

    /**
     * Handles exception of type of few types.
     * They are -
     * - {@link HttpMediaTypeNotAcceptableException}
     *
     * @param request An instance of {@link HttpServletRequest}.
     * @param response An instance of {@link HttpServletResponse}.
     * @param ex An instance of {@link Exception}.
     * @return An instance of {@link ResponseEntity}.
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpMediaTypeNotAcceptableException ex) {
        return handleAllExceptions(
            request, response, HttpStatus.NOT_ACCEPTABLE, Messages.UNSUPPORTED_ACCEPT_HEADER_ERROR, ex);
    }

    /**
     * Handles generic exception represented by {@link Exception}.
     *
     * @param request An instance of {@link HttpServletRequest}.
     * @param response An instance of {@link HttpServletResponse}.
     * @param ex ex An instance of {@link Exception}.
     * @return An instance of {@link ResponseEntity}.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleAnyGenericException(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Exception ex) {
        return handleAllExceptions(request, response, HttpStatus.INTERNAL_SERVER_ERROR, Messages.SERVER_ERROR_KEY, ex);
    }

    private ResponseEntity<String> handleAllExceptions(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpStatus responseCode,
            final String errorCode,
            final String localizedMessage,
            final Exception ex) {
        final ApiRequest apiRequest = (ApiRequest)request.getAttribute(ApiRequest.API_REQUEST_CONTEXT);
        printError(request, apiRequest, responseCode, errorCode, localizedMessage, ex);

        // Send error response back to user
        final ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(errorCode)
                .message(localizedMessage)
                .build();
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, responseCode);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, createErrorJson(apiErrorResponse));
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE, ex.getClass());
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, ex);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(createErrorJson(apiErrorResponse), httpHeaders, responseCode);
    }

    private void printError(
            final HttpServletRequest request,
            final ApiRequest apiRequest,
            final HttpStatus responseCode,
            final String errorCode,
            final String localizedMessage,
            final Exception ex) {
        if (Objects.nonNull(apiRequest)) {
            final long elapsedTime = System.currentTimeMillis() - apiRequest.getRequestStartTime();
            LOGGER.error("RequestId {} meant for request URI {} returned in {}ms, it resulted with return code {}, "
                    + "mapped to error code {} and, error is: ",
                    apiRequest.getRequestId(),
                    apiRequest.getRequestUrl(),
                    elapsedTime,
                    responseCode,
                    errorCode, ex);
        } else {
            LOGGER.error("Request for request URI {} resulted in an error with return code {},"
                    + " mapped to error code {}  and, error is: ",
                        request.getRequestURI(), responseCode, errorCode, ex);
        }
        LOGGER.error("Since request failed, client will be reported http status code: {}, "
                + "error code {} and error message {}", responseCode, errorCode, localizedMessage);
    }

    private String createErrorJson(final ApiErrorResponse apiErrorResponse) {
        return JsonUtils.convertToJson(apiErrorResponse);
    }

    private ResponseEntity<String> handleAllExceptions(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpStatus responseCode,
            final String errorCode,
            final Exception ex) {
        final ApiRequest apiRequest = (ApiRequest)request.getAttribute(ApiRequest.API_REQUEST_CONTEXT);
        final Object[] msgArgs = Objects.nonNull(apiRequest) ? new Object[]{apiRequest.getRequestId()} : new Object[0];
        final String localizedMessage = getLocalizedMessage(request, errorCode, msgArgs);
        return handleAllExceptions(request, response, responseCode, errorCode, localizedMessage, ex);
    }

    private String getLocalizedMessage(
            final HttpServletRequest request,
            final String errorCode,
            final Object... msgArgs) {

        final ApiRequest apiRequest = (ApiRequest)request.getAttribute(ApiRequest.API_REQUEST_CONTEXT);
        final Locale locale = Objects.nonNull(apiRequest) ? apiRequest.getLocale() : request.getLocale();
        final String msgKey = Objects.nonNull(errorCode) ? errorCode : Messages.SERVER_ERROR_KEY;
        return messages.getLocalizedErrorMessage(msgKey, locale, msgArgs);
    }
}
