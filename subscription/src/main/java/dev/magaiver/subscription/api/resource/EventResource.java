package dev.magaiver.subscription.api.resource;

import dev.magaiver.subscription.api.model.input.CheckInInput;
import dev.magaiver.subscription.api.model.input.EventInput;
import dev.magaiver.subscription.api.model.output.EventOutput;
import dev.magaiver.subscription.domain.model.CheckIn;
import dev.magaiver.subscription.domain.model.Event;
import dev.magaiver.subscription.domain.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/event")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventResource {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutput register(@Valid @RequestBody final EventInput eventInput) {
        final Event eventSaved = eventService.save(eventInput.assembler());
        return new EventOutput().disassembler(eventSaved);
    }

    @PostMapping("/checkin")
    @ResponseStatus(HttpStatus.CREATED)
    public void checkIn(@RequestBody final CheckInInput checkInInput) {
        CheckIn checkIn = eventService.checkIn(checkInInput.assembler());
    }

    @GetMapping
    public List<EventOutput> findAll() {
        return new EventOutput().collectionDisassembler(eventService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutput findById(@PathVariable String id) {
        return new EventOutput().disassembler(eventService.findByIdOrElseThrow(id));
    }
}
