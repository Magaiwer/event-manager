package dev.magaiver.subscription.domain.repository;

import dev.magaiver.subscription.domain.model.CheckIn;
import dev.magaiver.subscription.domain.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckInRepository extends MongoRepository<CheckIn, String> {

    Optional<CheckIn> findByUserEmailAndAndEventId(String userEmail, String eventId);
}
