package dev.magaiver.subscription.domain.repository;

import dev.magaiver.subscription.domain.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
}
