package com.muciociosan.theproject.adapters.mailsenderclient;

import com.muciociosan.theproject.adapters.mailsenderclient.dto.MailSendingClientRequestDto;
import com.muciociosan.theproject.adapters.mailsenderclient.dto.MailSendingClientResponseDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(accept = "application/json")
public interface MailSenderRestClient {

    @PostExchange("/fake/mail-sender")
    MailSendingClientResponseDto sendEmail(@RequestBody final MailSendingClientRequestDto payload);

}
