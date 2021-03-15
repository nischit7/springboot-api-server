package org.example.web;

import java.io.Serializable;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.example.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents each API request.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRequest implements Serializable {

    public static final String API_REQUEST_CONTEXT = "apiRequestContext";

    private static final long serialVersionUID = 3043101598949139021L;

    @NotBlank
    private String requestUrl;

    @NotBlank
    private String requestMethod;

    @NotBlank
    private String requestId;

    @Min(value = 1L)
    private long requestStartTime;

    @NotBlank
    private Locale locale;

    /**
     * Creating a custom builder.
     *
     * @return new custom builder.
     */
    public static ApiRequest.ApiRequestBuilder builder() {
        final ApiRequest.ApiRequestBuilder builder = new ApiRequestBuilderCustom();
        return builder
                .requestStartTime(Instant.now().toEpochMilli())
                .requestId(UUID.randomUUID().toString());
    }

    /**
     * Make sure the object is validated upon creation using builder.
     */
    public static class ApiRequestBuilderCustom extends ApiRequest.ApiRequestBuilder {
        @Override
        public ApiRequest build() {
            final ApiRequest obj = super.build();
            ValidationUtils.validate(obj);
            return obj;
        }
    }
}
