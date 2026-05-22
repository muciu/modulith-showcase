package com.muciociosan.theproject.users.domain;

import com.muciociosan.theproject.shared.ids.UserId;

public interface UserAggregateEvent {
    UserId userId();
}

record UserCreated(UserId userId) implements UserAggregateEvent {
}

record UserUpdated(UserId userId) implements UserAggregateEvent {
}

record UserEmailAdded(UserId userId, String email) implements UserAggregateEvent {
}
