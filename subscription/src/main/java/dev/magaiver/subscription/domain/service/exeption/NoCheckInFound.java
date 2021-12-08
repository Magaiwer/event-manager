package dev.magaiver.subscription.domain.service.exeption;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class NoCheckInFound extends BusinessException {
    public NoCheckInFound(String message) {
        super(message);
    }
}
