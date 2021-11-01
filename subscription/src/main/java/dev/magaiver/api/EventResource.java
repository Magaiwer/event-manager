package dev.magaiver.api;

import dev.magaiver.api.model.input.EventInput;
import dev.magaiver.api.model.output.EventOutput;
import dev.magaiver.domain.model.Event;
import dev.magaiver.domain.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
