package dev.magaiver.api.model.output;

import dev.magaiver.domain.model.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class EventOutput {

    private Long id;
    private LocalDateTime dateTime;
    private String description;
    private boolean enabled;
    private OffsetDateTime createdAt;

    public EventOutput disassembler(Event eventSaved) {
        this.setId(eventSaved.getId());
        this.setDateTime(eventSaved.getDateTime());
        this.setEnabled(eventSaved.isEnabled());
        this.setCreatedAt(eventSaved.getCreatedAt());
        return this;
    }
}
