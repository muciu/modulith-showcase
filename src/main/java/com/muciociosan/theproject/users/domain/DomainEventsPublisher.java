package com.muciociosan.theproject.users.domain;

import com.muciociosan.theproject.shared.events.DomainEvent;
import com.muciociosan.theproject.shared.exceptions.ApplicationException;
import com.muciociosan.theproject.users.UserCreatedDomainEvent;
import com.muciociosan.theproject.users.UserUpdatedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DomainEventsPublisher {

    public void publishFrom(List<? extends UserAggregateEvent> userEvents) {
        userEvents.forEach(event -> {
            switch (event) {
                case UserCreated created -> publish(new UserCreatedDomainEvent(created.userId()));
                case UserEmailAdded emailAdded -> publish(new UserUpdatedDomainEvent(
                        emailAdded.userId(),
                        properties(emailAdded)));
                default -> throw new ApplicationException("Unsupported event type received for publication %s".formatted(event.getClass()));
            }
        });
    }

    private <T extends DomainEvent<?>> void publish(T event) {
        // Publish events to some cross-domain events
    }

    private Map<String, Object> properties(final UserEmailAdded event) {
        return Map.of("email", event.email());
    }
}
