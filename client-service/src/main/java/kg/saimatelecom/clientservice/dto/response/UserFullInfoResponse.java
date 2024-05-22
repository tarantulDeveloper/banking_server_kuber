package kg.saimatelecom.clientservice.dto.response;

import kg.saimatelecom.clientservice.enums.Role;

import java.sql.Timestamp;

public record UserFullInfoResponse(
        Long id,
        String email,
        Role role,
        String phoneNumber,
        String firstName,
        String lastName,
        String patronymic,
        String profileImagePath,
        boolean activated,
        Timestamp createdAt,
        Timestamp updatedAt,
        boolean deleted

) {
}
