package com.muciociosan.theproject.infrastructure;

import com.muciociosan.theproject.openapi.generated.SystemApi;
import com.muciociosan.theproject.openapi.generated.model.SystemStatusApiResponseDto;
import com.muciociosan.theproject.shared.exceptions.ApplicationException;
import com.muciociosan.theproject.shared.exceptions.ResourceNotFoundException;
import com.muciociosan.theproject.shared.exceptions.ValidationException;
import com.muciociosan.theproject.shared.ids.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.Objects.isNull;

@RestController
public class ApplicationStatusController implements SystemApi {

    @Override
    public ResponseEntity<SystemStatusApiResponseDto> getSystemStatus(final String type) {
        final var effectiveType = isNull(type) ? "none" : type;
        return switch (effectiveType) {
            case "validation" -> throw new ValidationException("systemStatus", "Test purpose system status error!");
            case "not_found" -> throw new ResourceNotFoundException(String.class, UserId.from(UUID.randomUUID()));
            case "internal" -> throw new ApplicationException("Internal error!");
            default -> ResponseEntity
                    .ok(new SystemStatusApiResponseDto("the-project", "OK", OffsetDateTime.now()));
        };
    }
}
