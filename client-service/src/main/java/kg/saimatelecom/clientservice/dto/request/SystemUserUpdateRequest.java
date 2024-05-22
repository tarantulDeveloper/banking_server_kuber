package kg.saimatelecom.clientservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record SystemUserUpdateRequest(
        @Positive(message = "Value must be a positive number")
        Long id,
        @NotEmpty(message = "The role is required.")
        String role,
        @NotEmpty(message = "The phone number is required.")
//        @Pattern(regexp = "^\\+?[0-9]{13,20}$", message = "Invalid phone number")
        String phoneNumber,
        @NotBlank(message = "Name cannot be empty")
        String firstName,
        @NotBlank(message = "Last name cannot be empty")
        String lastName,
        String patronymic,
        Boolean activated,
        Boolean deleted

) {
}
