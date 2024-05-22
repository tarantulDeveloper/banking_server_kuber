package kg.saimatelecom.clientservice.dto.request;

import jakarta.validation.constraints.*;

import java.sql.Timestamp;

public record ClientUpdateRequest(
        @Positive(message = "Value must be a positive number")
        Long id,
        @NotEmpty(message = "The phone number is required.")
//        @Pattern(regexp = "^\\+?[0-9]{12,20}$", message = "Invalid phone number")
        String phoneNumber,
        @NotBlank(message = "Name cannot be empty")
        String firstName,
        @NotBlank(message = "Last name cannot be empty")
        String lastName,
        @NotBlank(message = "Patronymic cannot be empty")
        String patronymic,
        Boolean activated,
        Boolean deleted,
        @Pattern(regexp = "[a-zA-Z]{2}\\d*", message = "The first two characters must be letters in document id, followed by digits")
        @NotBlank(message = "Document id cannot be empty")
        String documentId,
        @NotBlank(message = "authority cannot be empty")
        String authority,
        @NotNull(message = "documentIssuedAt cannot be null")
        Timestamp documentIssuedAt,
        @NotNull(message = "documentExpiresAt cannot be null")
        Timestamp documentExpiresAt,
        @NotBlank(message = "citizenship cannot be empty")
        String citizenship

) {
}