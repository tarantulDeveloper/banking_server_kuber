package kg.saimatelecom.clientservice.dto.response;

import kg.saimatelecom.clientservice.enums.Role;

public record AdminOrManagerInfoResponse(
        String email,
        Role role,
        String firstName,
        String lastName,
        String patronymic,
        String phoneNumber,
        String profileImagePath
) {
}
