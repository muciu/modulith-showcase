package com.muciociosan.theproject.users.usecases;

import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.domain.DomainEventsPublisher;
import com.muciociosan.theproject.users.domain.UserAggregateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailVerificationUseCase {
    private final UserAggregateRepository userRepository;
    private final DomainEventsPublisher eventsPublisher;

    @Transactional
    public void markVerificationStarted(final UserId userId, final EmailValue newEmail) {
        final var user = userRepository.getBy(userId);
        user.emailVerificationStarted(newEmail);
        final var events = userRepository.save(user);
        eventsPublisher.publishFrom(events);
    }
}
