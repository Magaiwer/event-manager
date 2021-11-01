package dev.magaiver.api.model.input;

import dev.magaiver.domain.model.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventInput {

    private Long id;
    private LocalDateTime dateTime;
    private String description;

    public Event assembler() {
        return new Event();
    }
}
