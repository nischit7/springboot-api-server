package org.example.services.impl;

import org.example.localization.LocalizationParamValueException;
import org.example.localization.MsgKey;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Thrown when team info is not found.
 */
@ResponseStatus(NOT_FOUND)
@MsgKey("team.not.found")
public class TeamNotFoundException extends LocalizationParamValueException {

    private static final long serialVersionUID = -7118701777583863756L;

    public TeamNotFoundException(final Object...args) {
        super(args);
    }
}
