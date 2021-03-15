package org.example.localization;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link Messages}.
 */
@ExtendWith(MockitoExtension.class)
public class MessagesTest {

    private static final Locale NON_DEFAULT_LOCALE = Locale.FRENCH;
    private static final String MESSAGE_KEY = "MESSAGE_KEY";
    private static final String DEFAULT_ERROR_MESSAGE = "DEFAULT_ERROR_MESSAGE";
    private static final String LOCALIZED_ERROR_MESSAGE = "LOCALIZED_ERROR_MESSAGE";

    @Mock
    private MessageSource mockMessageSource;

    @Mock
    private MessageSource mockErrorMessageSource;

    private Messages messages;

    @BeforeEach
    public void setup() throws Exception {
        Messages.setLocale(Messages.DEFAULT_LOCALE);
        this.messages = new Messages(this.mockErrorMessageSource, this.mockMessageSource);
    }

    @Test
    public void testDefaultErrorMessage() {
        when(this.mockErrorMessageSource.getMessage(eq(MESSAGE_KEY), any(), eq(Messages.DEFAULT_LOCALE))).thenReturn(DEFAULT_ERROR_MESSAGE);
        final String message = this.messages.getErrorMessage(MESSAGE_KEY);
        assertThat(message, equalTo(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    public void testLocalizedErrorMessage() {
        when(this.mockErrorMessageSource.getMessage(
            eq(MESSAGE_KEY), any(), eq(NON_DEFAULT_LOCALE))).thenReturn(LOCALIZED_ERROR_MESSAGE);
        Messages.setLocale(NON_DEFAULT_LOCALE);
        final String message = this.messages.getLocalizedErrorMessage(MESSAGE_KEY);
        assertThat(message, equalTo(LOCALIZED_ERROR_MESSAGE));
    }
}
