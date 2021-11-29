package dev.magaiver.subscription.domain.repository;

import dev.magaiver.subscription.domain.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Magaiver Santos
 */
@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Optional<Subscription> findByUserEmailAndAndEventId(String userEmail, String eventId);
    Optional<Subscription> findByUserEmailAndAndEventIdAndEnabledTrue(String userEmail, String eventId);

    List<Subscription> findAllByUserEmail(String userEmail);
}
