package org.example.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.Builder;
import lombok.Getter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ValidationUtils}.
 */
public class ValidationUtilsTest {

    private static final List<Path> BEAN1_DEFAULT_PROPERTY_PATHS = Collections.unmodifiableList(Arrays.asList(
            PathImpl.createPathFromString("a"),
            PathImpl.createPathFromString("b")));

    @Test
    public void validateEmptyBean1() {
        final ConstraintViolationException expn = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            final Bean1 b = Bean1.builder()
                    .build();
            ValidationUtils.validate(b);
        });
        final Set<ConstraintViolation<?>> violations = expn.getConstraintViolations();
        assertThat(violations, hasSize(2));

        final List<Path> foundPaths = violations.stream()
                .map(x -> x.getPropertyPath())
                .collect(Collectors.toList());
        assertThat(foundPaths, containsInAnyOrder(BEAN1_DEFAULT_PROPERTY_PATHS.toArray()));
    }

    @Test
    public void validateBean1Ok() {
        final Bean1 b = Bean1.builder()
                .a("foo")
                .b(3)
                .build();

        ValidationUtils.validate(b);
    }

    static Stream<Arguments> dataProviderIsValid() {
        return Stream.of(
            Arguments.of(Bean1.builder().a("foo").build(), false),
            Arguments.of(Bean1.builder().b(3).build(), false),
            Arguments.of(Bean1.builder().a("foo").b(6).build(), false),
            Arguments.of(Bean1.builder().a("foo").b(3).build(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("dataProviderIsValid")
    @DisplayName("Validates various inputs")
    public void isValid(final Bean1 b, final boolean expectedResult) {
        assertThat(ValidationUtils.isValid(b), is(expectedResult));
    }

    @Builder
    static class Bean1 {

        @Getter
        @NotNull
        private String a;

        @Getter
        @Range(min = 1, max = 5)
        private int b;
    }
}
