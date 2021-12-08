package dev.magaiver.subscription.domain.service.exeption;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class AlreadyCheckInException extends BusinessException {
    public AlreadyCheckInException(String message) {
        super(message);
    }
}
