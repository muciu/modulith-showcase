package com.muciociosan.theproject.users.internal;

import com.muciociosan.theproject.users.domain.UserAggregate;
import com.muciociosan.theproject.users.domain.UsernameValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAggregateJpaRepository extends JpaRepository<UserAggregate, UUID> {

    Optional<UserAggregate> findByUsername(UsernameValue username);
}
