package dev.magaiver.subscription.domain.service.exeption;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class AlreadySubscribeException extends BusinessException {
    public AlreadySubscribeException(String message) {
        super(message);
    }
}
