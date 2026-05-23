package com.muciociosan.theproject.applicationlogic.emailverification;

import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.usecases.EmailValue;
import com.muciociosan.theproject.users.usecases.EmailVerificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerifyEmailLogic {

    private final EmailVerificationUseCase emailVerificationUseCase;

    public void verificationCompleted(final UserId userId, final EmailValue emailValue) {
        emailVerificationUseCase.verificationCompleted(userId, emailValue);
    }
}
