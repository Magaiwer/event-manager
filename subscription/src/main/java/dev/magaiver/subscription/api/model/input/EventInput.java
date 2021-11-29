package dev.magaiver.subscription.api.model.input;

import dev.magaiver.subscription.domain.model.Event;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class EventInput {

    private String id;
    private String name;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeClose;
    @NotEmpty
    private String description;
    private int capacity;

    public Event assembler() {
        return new Event(this.getId(), this.getName(), this.getDateTime(), this.getDescription(),
                this.getDateTimeClose(), this.getCapacity());
    }
}
