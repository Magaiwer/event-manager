package dev.magaiver.subscription.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@Document("subscription")
public class Subscription implements AbstractEntity {

    @Id
    private String id;
    private Event event;
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private LocalDateTime createdAt;

    public Subscription(String id, Event event, String userEmail, SubscriptionStatus status) {
        this.id = id;
        this.event = event;
        this.userEmail = userEmail;
        this.status = status;

        if (isNew()) {
            this.status = SubscriptionStatus.ENABLED;
            this.createdAt = LocalDateTime.now();
        }
    }

    public boolean isNew() {
        return this.getId() == null || this.getId().isBlank();
    }
}
