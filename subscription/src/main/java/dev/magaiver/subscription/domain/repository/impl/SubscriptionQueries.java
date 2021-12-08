package dev.magaiver.subscription.domain.repository.impl;

import dev.magaiver.subscription.domain.model.Subscription;

import java.util.Optional;

/**
 * @author Magaiver Santos
 */
public interface SubscriptionQueries {
    Optional<Subscription> findByUserEmailAndEventIdAnAndStatus(String userEmail, String eventId);
}
