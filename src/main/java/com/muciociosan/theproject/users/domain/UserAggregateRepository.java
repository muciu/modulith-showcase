package com.muciociosan.theproject.users.domain;

import com.muciociosan.theproject.shared.exceptions.ResourceNotFoundException;
import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.internal.UserAggregateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAggregateRepository {
    private final UserAggregateJpaRepository userAggregateJpaRepository;

    public UserAggregate getBy(final UserId userId) {
        return findBy(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserAggregate.class, userId));
    }

    public Optional<UserAggregate> findBy(final UserId userId) {
        return userAggregateJpaRepository.findById(userId.value());
    }

    public List<? extends UserAggregateEvent> save(final UserAggregate userAggregate) {
        final var saved = userAggregateJpaRepository.save(userAggregate);
        return saved.resetPendingEvents();
    }

    public Optional<UserAggregate> findBy(UsernameValue username) {
        return userAggregateJpaRepository.findByUsername(username);
    }
}
