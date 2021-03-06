package dev.magaiver.subscription.domain.service;

import dev.magaiver.core.exception.EntityNotFoundException;
import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.model.SubscriptionStatus;
import dev.magaiver.subscription.domain.repository.SubscriptionRepository;
import dev.magaiver.subscription.domain.service.exeption.AlreadyCheckInException;
import dev.magaiver.subscription.domain.service.exeption.AlreadySubscribeException;
import dev.magaiver.subscription.domain.service.exeption.EventCloseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionService {
    private static final String MSG_NOT_FOUND_BY_EMAIL = "Subscription(s) of email %s not found";
    private static final String MSG_NOT_FOUND = "Subscription of id %s not found";
    private final SubscriptionRepository subscriptionRepository;
    private final EventService eventService;

    public Subscription save(Subscription subscription) {
        subscription.setEvent(eventService.findByIdOrElseThrow(subscription.getEvent().getId()));
        verifyAlreadySubscribed(subscription);
        verifyEventClose(subscription);
        return subscriptionRepository.save(subscription);
    }

    public void cancel(Subscription subscription) {
        verifyEventClose(subscription);
        boolean alreadyCheckIn = eventService.verifyCheckInExists(subscription.getUserEmail(), subscription.getEvent().getId());
        if (alreadyCheckIn) {
            throw new AlreadyCheckInException("Cannot cancel subscription with check in!");
        }
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
    }

    public Subscription findByIdOrElseThrow(String id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format(MSG_NOT_FOUND, id)));
    }

    public Subscription findByUserEmailAndEventIdOrElseThrow(String userEmail, String eventId) {
        return subscriptionRepository.findByUserEmailAndEventIdAnAndStatus(userEmail, eventId)
                .orElseThrow(() -> new EntityNotFoundException(format(MSG_NOT_FOUND_BY_EMAIL, userEmail)));
    }

    public List<Subscription> findAllByUserEmailOrElseThrow(String userEmail) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserEmail(userEmail);

        if (subscriptions.isEmpty()) {
            throw new EntityNotFoundException(format(MSG_NOT_FOUND_BY_EMAIL, userEmail));
        }
        return subscriptions;
    }

    private void verifyEventClose(Subscription subscription) {
        boolean eventClose = subscription.getCreatedAt()
                .isAfter(subscription.getEvent().getDateClose());

        if (eventClose) throw new EventCloseException("Event is close! not allowed new subscriptions");
    }

    private void verifyAlreadySubscribed(Subscription subscription) {
        boolean alreadySubscribed = subscriptionRepository.
                findByUserEmailAndEventIdAnAndStatus(subscription.getUserEmail(), subscription.getEvent().getId())
                .isPresent();
        if (alreadySubscribed) {
            throw new AlreadySubscribeException(format("User with email %s already subscribed", subscription.getUserEmail()));
        }
    }


}
