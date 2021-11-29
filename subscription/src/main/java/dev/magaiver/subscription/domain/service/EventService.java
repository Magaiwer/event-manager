package dev.magaiver.subscription.domain.service;

import dev.magaiver.core.exception.EntityNotFoundException;
import dev.magaiver.subscription.domain.model.Event;
import dev.magaiver.subscription.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {
    private static final String MSG_NOT_FOUND = "Event of code %s not found";
    private final EventRepository eventRepository;

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void cancel(Event event) {
        event.setEnabled(false);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findByIdOrElseThrow(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format(MSG_NOT_FOUND, id)));
    }
}
