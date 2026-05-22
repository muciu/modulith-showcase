package com.muciociosan.theproject.users;

import com.muciociosan.theproject.shared.events.DomainEvent;
import com.muciociosan.theproject.shared.ids.UserId;

@org.jmolecules.event.annotation.DomainEvent
public class UserCreatedDomainEvent extends DomainEvent<UserId> {
    public UserCreatedDomainEvent(UserId identity) {
        super(identity);
    }
}
