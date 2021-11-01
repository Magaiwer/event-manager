package dev.magaiver.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@DynamicUpdate
public class Event implements AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime dateTime;
    @Column
    private String description;
    @Column
    private boolean enabled;
    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    public Event(Long id, LocalDateTime dateTime, String description) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
    }

    public Event() {
    }
}
