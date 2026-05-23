package com.muciociosan.theproject.users.view;

import java.time.Instant;

public record UserView(String username, UserEmailView email, Instant createdAt, Instant updatedAt) {

}
