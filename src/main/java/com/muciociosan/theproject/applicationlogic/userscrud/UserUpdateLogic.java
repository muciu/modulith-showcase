package com.muciociosan.theproject.applicationlogic.userscrud;

import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.usecases.EmailUpdateUseCase;
import com.muciociosan.theproject.users.usecases.EmailValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateLogic {
    private final EmailUpdateUseCase emailUpdateUseCase;

    public void updateEmail(final UserId userId, final String email) {
        emailUpdateUseCase.updateEmail(userId, EmailValue.from(email));
    }
}
