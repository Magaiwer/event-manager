package dev.magaiver.subscription.domain.service.exeption;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class EventCloseException extends BusinessException {

    public EventCloseException(String message) {
        super(message);
    }
}
