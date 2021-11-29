package dev.magaiver.subscription.api.model.output;

import dev.magaiver.subscription.domain.model.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventOutput {

    private String id;
    private String name;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeClose;
    private String description;
    private boolean enabled;

    public EventOutput disassembler(Event eventSaved) {
        this.setId(eventSaved.getId());
        this.setDateTime(eventSaved.getDateTime());
        this.setDateTimeClose(eventSaved.getDateClose());
        this.setEnabled(eventSaved.isEnabled());
        this.setDescription(eventSaved.getDescription());
        this.setName(eventSaved.getName());
        return this;
    }

    public List<EventOutput> collectionDisassembler(List<Event> eventList) {
        List<EventOutput> eventOutputList = new ArrayList<>();
        eventList.forEach(e -> eventOutputList.add(new EventOutput().disassembler(e)));
        return eventOutputList;
    }
}
