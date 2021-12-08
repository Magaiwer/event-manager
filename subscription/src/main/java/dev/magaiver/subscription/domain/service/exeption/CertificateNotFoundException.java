package dev.magaiver.subscription.domain.service.exeption;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class CertificateNotFoundException extends BusinessException {
    public CertificateNotFoundException(String message) {
        super(message);
    }
}
