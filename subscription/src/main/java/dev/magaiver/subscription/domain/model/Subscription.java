package dev.magaiver.subscription.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@Document("subscription")
public class Subscription implements AbstractEntity {

    @Id
    private String id;
    private Event event;
    private String userEmail;
    private boolean enabled;
    private LocalDateTime createdAt;

    public Subscription(String id, Event event, String userEmail, boolean enabled) {
        this.id = id;
        this.event = event;
        this.userEmail = userEmail;
        this.enabled = enabled;

        if (isNew()) {
            this.enabled = true;
            this.createdAt = LocalDateTime.now();
        }
    }

    public boolean isNew() {
        return this.getId() == null || this.getId().isBlank();
    }
}
