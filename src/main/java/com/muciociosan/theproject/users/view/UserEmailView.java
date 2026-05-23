package com.muciociosan.theproject.users.view;

import com.muciociosan.theproject.users.usecases.EmailValue;

public record UserEmailView(EmailValue email, boolean isVerified) {
}
