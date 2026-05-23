package com.muciociosan.theproject.users.usecases;

import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.domain.UserAggregate;
import com.muciociosan.theproject.users.domain.UserAggregateRepository;
import com.muciociosan.theproject.users.domain.UsernameValue;
import com.muciociosan.theproject.users.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserLookupUseCase {

    private final UserAggregateRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<UserView> findUser(final String username) {
        return userRepository.findBy(UsernameValue.from(username))
                .map(UserAggregate::view);
    }

    @Transactional(readOnly = true)
    public UserView getUser(final UserId userId) {
        return userRepository.getBy(userId).view();
    }
}
