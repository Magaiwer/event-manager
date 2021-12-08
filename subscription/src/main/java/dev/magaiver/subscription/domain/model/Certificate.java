package dev.magaiver.subscription.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Magaiver Santos
 */
@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@Document("certificate")
public class Certificate implements AbstractEntity, Serializable {

    @Id
    private String id;
    private Subscription subscription;

    public Certificate(String id, Subscription subscription) {
        this.id = id;
        this.subscription = subscription;
    }

    @Override
    public String getId() {
        return null;
    }
}
