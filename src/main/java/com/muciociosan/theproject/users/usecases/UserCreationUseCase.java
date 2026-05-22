package com.muciociosan.theproject.users.usecases;

import com.muciociosan.theproject.shared.exceptions.ResourceConflictException;
import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.domain.DomainEventsPublisher;
import com.muciociosan.theproject.users.domain.UserAggregate;
import com.muciociosan.theproject.users.domain.UserAggregateRepository;
import com.muciociosan.theproject.users.domain.UsernameValue;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreationUseCase {
    private final UserAggregateRepository userAggregateRepository;
    private final DomainEventsPublisher eventsPublisher;

    @Transactional
    public UserId createUser(final String username, final String email) {
        final var usernameValue = UsernameValue.from(username);
        final var existingByUsername = userAggregateRepository.findBy(usernameValue);
        existingByUsername.ifPresent(user -> {
            throw new ResourceConflictException(user.getClass(), user.username().value());
        });
        final var user = UserAggregate.newFrom(usernameValue, email);
        final var events = userAggregateRepository.save(user);
        eventsPublisher.publishFrom(events);
        return user.id();
    }
}
