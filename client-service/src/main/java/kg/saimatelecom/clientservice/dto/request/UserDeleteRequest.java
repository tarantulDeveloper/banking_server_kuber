package kg.saimatelecom.clientservice.dto.request;

import jakarta.validation.constraints.Positive;

public record UserDeleteRequest(
        @Positive(message = "Value must be a positive number")
        Long id,
        boolean deleted
) {
}