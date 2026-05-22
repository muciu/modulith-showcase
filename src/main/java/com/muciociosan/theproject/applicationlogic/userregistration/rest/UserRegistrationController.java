package com.muciociosan.theproject.applicationlogic.userregistration.rest;

import com.muciociosan.theproject.applicationlogic.userregistration.UserRegistrationProcess;
import com.muciociosan.theproject.openapi.generated.UserRegistrationApi;
import com.muciociosan.theproject.openapi.generated.model.UserRegistrationApiRequestDto;
import com.muciociosan.theproject.openapi.generated.model.UserRegistrationApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UserRegistrationController implements UserRegistrationApi {

    private final UserRegistrationProcess userRegistrationProcess;

    @Override
    public ResponseEntity<UserRegistrationApiResponseDto> registerUser(
            final UserRegistrationApiRequestDto request
    ) {
        final var result = userRegistrationProcess.registerUser(
                request.getUsername(),
                request.getEmail());

        return ResponseEntity.ok(new UserRegistrationApiResponseDto(result.uuid()));
    }
}
