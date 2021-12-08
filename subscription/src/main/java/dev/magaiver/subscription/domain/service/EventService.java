package dev.magaiver.subscription.domain.service;

import dev.magaiver.core.exception.EntityNotFoundException;
import dev.magaiver.subscription.domain.model.CheckIn;
import dev.magaiver.subscription.domain.model.Event;
import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.model.SubscriptionStatus;
import dev.magaiver.subscription.domain.repository.CheckInRepository;
import dev.magaiver.subscription.domain.repository.EventRepository;
import dev.magaiver.subscription.domain.repository.SubscriptionRepository;
import dev.magaiver.subscription.domain.service.exeption.AlreadyCheckInException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {
    private static final String MSG_NOT_FOUND = "Event of code %s not found";
    private final EventRepository eventRepository;
    private final CheckInRepository checkInRepository;
    private final SubscriptionRepository subscriptionRepository;

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

    public CheckIn checkIn(CheckIn checkIn) {
        Event event = findByIdOrElseThrow(checkIn.getEvent().getId());
        checkIn.setEvent(event);
        checkIn.setDateTime(LocalDateTime.now());
        boolean allReadyCheckIn = verifyCheckInExists(checkIn.getUserEmail(), event.getId());
        if (allReadyCheckIn) throw new AlreadyCheckInException("Already check in this event");

        subscriptionFast(checkIn, event);
        return checkInRepository.save(checkIn);
    }

    private Subscription subscriptionFast(CheckIn checkIn, Event event) {
        Optional<Subscription> subscriptionOption = subscriptionRepository
                .findByUserEmailAndEventIdAnAndStatus(checkIn.getUserEmail(), event.getId());

        Subscription subscription = subscriptionOption.orElse(new Subscription("", event, checkIn.getUserEmail(), SubscriptionStatus.PRESENT));
        subscription.setStatus(SubscriptionStatus.PRESENT);
        subscription = subscriptionRepository.save(subscription);
        return subscription;
    }


    public boolean verifyCheckInExists(String userEmail, String eventId) {
        return checkInRepository
                .findByUserEmailAndAndEventId(userEmail, eventId)
                .isPresent();
    }
}
