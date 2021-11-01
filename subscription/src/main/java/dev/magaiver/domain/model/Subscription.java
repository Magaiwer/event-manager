package dev.magaiver.domain.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.OffsetDateTime;

@Entity
@Data
public class Subscription implements AbstractEntity {

    @Id
    private Long id;
    @OneToMany
    private Event event;
    @Column
    private boolean enabled;
    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdAt;
}
