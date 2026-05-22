package com.muciociosan.theproject.applicationlogic.userregistration;

import com.muciociosan.theproject.adapters.MailingClient;
import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.usecases.EmailValue;
import com.muciociosan.theproject.users.usecases.EmailVerificationUseCase;
import com.muciociosan.theproject.users.usecases.UserCreationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistrationProcess {

    private final UserCreationUseCase userCreationUseCase;
    private final EmailVerificationUseCase emailVerificationUseCase;
    private final MailingClient mailingClient;

    public UserId registerUser(final String username, final String email) {
        final var emailValue = EmailValue.from(email);
        // Transaction one - user creation
        final var userId = userCreationUseCase.createUser(username, emailValue.value());

        // Transaction two - sending email
        final var emailStatus = mailingClient.sendEmail(
                "Please verify", "Link to finish verification", emailValue.value());
        if (emailStatus.success()) {
            emailVerificationUseCase.markVerificationStarted(userId, emailValue);
        } else {
            log.error("Verification not triggered for %s".formatted(username));
        }
        return userId;
    }
}
