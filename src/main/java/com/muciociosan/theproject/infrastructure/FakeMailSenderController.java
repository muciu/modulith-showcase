package com.muciociosan.theproject.infrastructure;

import com.muciociosan.theproject.openapi.generated.FakeMailSenderApi;
import com.muciociosan.theproject.openapi.generated.model.SendEmail200Response;
import com.muciociosan.theproject.openapi.generated.model.SendEmailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeMailSenderController implements FakeMailSenderApi {
    @Override
    public ResponseEntity<SendEmail200Response> sendEmail(SendEmailRequest sendEmailRequest) {
        final var status = sendEmailRequest.getTo().startsWith("sending-failure") ? "FAILED" : "OK";
        return ResponseEntity.ok(new SendEmail200Response().status(status));
    }
}
