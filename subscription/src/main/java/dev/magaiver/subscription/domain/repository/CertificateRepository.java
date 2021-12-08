package dev.magaiver.subscription.domain.repository;

import dev.magaiver.subscription.domain.model.Certificate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Magaiver Santos
 */
@Repository
public interface CertificateRepository extends MongoRepository<Certificate, String> {

    Optional<Certificate> findBySubscriptionId(String subscriptionId);
 }
