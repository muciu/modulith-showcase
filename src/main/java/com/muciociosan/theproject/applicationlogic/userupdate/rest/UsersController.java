package com.muciociosan.theproject.applicationlogic.userupdate.rest;

import com.muciociosan.theproject.applicationlogic.userupdate.UserUpdateLogic;
import com.muciociosan.theproject.openapi.generated.UsersApi;
import com.muciociosan.theproject.openapi.generated.model.GetUser200Response;
import com.muciociosan.theproject.openapi.generated.model.UserUpdateApiRequestDto;
import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.domain.view.UserView;
import com.muciociosan.theproject.users.usecases.UserLookupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
class UsersController implements UsersApi {

    private final UserUpdateLogic userUpdateLogic;
    private final UserLookupUseCase usersLookup;

    @Override
    public ResponseEntity<Void> updateUser(final UUID userId, final UserUpdateApiRequestDto request) {
        userUpdateLogic.updateEmail(UserId.from(userId), request.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetUser200Response> getUser(UUID userId) {
        final var user = usersLookup.getUser(UserId.from(userId));

        return ResponseEntity.ok(toDto(user));
    }

    private GetUser200Response toDto(final UserView user) {
        return new GetUser200Response(user.username(), user.email().email().value(), user.email().isVerified());
    }

}
