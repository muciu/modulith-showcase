package com.muciociosan.theproject.adapters.mailsenderclient;

import com.muciociosan.theproject.adapters.MailingClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailingClientAdapter implements MailingClient {

    private final MailSenderRestClient mailSenderRestClient;

    @Override
    public SendingResult sendEmail(String title, String content, String to) {
        log.info("Sending email %s, %s, %s".formatted(title, content, to));
        // TODO handle retires and exception thrown by the HTTP client and wrap it into response
        // add ResilienceFor4
        mailSenderRestClient.sendEmail();
        return new SendingResult(true, null);
    }


}
