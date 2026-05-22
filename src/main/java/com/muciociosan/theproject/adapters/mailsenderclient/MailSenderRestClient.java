package com.muciociosan.theproject.adapters.mailsenderclient;

import com.muciociosan.theproject.adapters.mailsenderclient.dto.MailSendingClientResponseDto;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = "application/json")
public interface MailSenderRestClient {

    @GetExchange("/api/collections/send-email/records?project_id=23618")
    MailSendingClientResponseDto sendEmail();

}
