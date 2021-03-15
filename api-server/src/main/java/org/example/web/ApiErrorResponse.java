package org.example.web;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.example.util.ValidationUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A generic representation of api error.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponse implements Serializable {

    private static final long serialVersionUID = 7525331975415269356L;

    @NotBlank
    private String code;

    @NotBlank
    private String message;

    private Map<String, Object> addnlErrorInfo;

    /**
     * Creating a custom builder.
     *
     * @return new custom builder.
     */
    public static ApiErrorResponseBuilder builder() {
        return new ApiErrorResponseBuilderCustom();
    }

    /**
     * Make sure the object is validated upon creation using builder.
     */
    public static class ApiErrorResponseBuilderCustom extends ApiErrorResponseBuilder {

        @Override
        public ApiErrorResponse build() {
            final ApiErrorResponse obj = super.build();
            ValidationUtils.validate(obj);
            return obj;
        }
    }
}
