package dev.magaiver.subscription.domain.repository.impl;

import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.model.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

/**
 * @author Magaiver Santos
 */
public class SubscriptionRepositoryImpl implements SubscriptionQueries {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubscriptionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Subscription> findByUserEmailAndEventIdAnAndStatus(String userEmail, String eventId) {
        Query query = new Query(Criteria.where("userEmail").is(userEmail)
                .and("eventId").is(eventId)
                .orOperator(
                        Criteria.where("status").is(SubscriptionStatus.ENABLED),
                        Criteria.where("status").is(SubscriptionStatus.PRESENT)
                ));
        return Optional.ofNullable(mongoTemplate.findOne(query, Subscription.class));
    }
}
