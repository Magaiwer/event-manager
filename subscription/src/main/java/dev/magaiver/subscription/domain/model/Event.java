package dev.magaiver.subscription.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@Document("event")
public class Event implements AbstractEntity {
    @Id
    private String id;
    private String name;
    private LocalDateTime dateTime;
    private LocalDateTime dateClose;
    private int capacity;
    private String description;
    private boolean enabled;
    private LocalDateTime createdAt;

    public Event(String id, String name, LocalDateTime dateTime, String description,
                 LocalDateTime dateClose, int capacity) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.dateClose = dateClose;
        this.capacity = capacity;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
    }

    public Event(String id) {
        this.id = id;
    }

    public Event() {
    }

    public String dateTimeStr() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateTime.format(df);
    }
}
