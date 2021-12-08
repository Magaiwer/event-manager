package dev.magaiver.subscription.domain.repository;

import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.repository.impl.SubscriptionQueries;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Magaiver Santos
 */
@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String>, SubscriptionQueries {

    Optional<Subscription> findByUserEmailAndEventId(String userEmail, String eventId);

    List<Subscription> findAllByUserEmail(String userEmail);
}
