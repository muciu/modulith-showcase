package com.muciociosan.theproject.users;

import com.muciociosan.theproject.shared.events.DomainEvent;
import com.muciociosan.theproject.shared.ids.UserId;

import java.util.Map;

public class UserUpdatedDomainEvent extends DomainEvent<UserId> {
    public UserUpdatedDomainEvent(UserId identity, Map<String, Object> parameters) {
        super(identity, parameters);
    }
}
