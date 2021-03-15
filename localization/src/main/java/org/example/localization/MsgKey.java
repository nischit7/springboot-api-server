package org.example.localization;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for exceptions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgKey {
    String value();
}
