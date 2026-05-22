package com.muciociosan.theproject.adapters;

import org.jspecify.annotations.Nullable;

public interface MailingClient {

    SendingResult sendEmail(final String title, final String content, final String to);

    record SendingResult(boolean success, @Nullable String error) {
    }
}
