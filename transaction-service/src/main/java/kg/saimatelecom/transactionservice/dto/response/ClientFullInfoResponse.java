package kg.saimatelecom.transactionservice.dto.response;



import kg.saimatelecom.transactionservice.utils.Role;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record ClientFullInfoResponse(
        Long id,
        Timestamp createdAt,
        Timestamp updatedAt,
        boolean deleted,
        String email,
        Role role,
        String phoneNumber,
        String firstName,
        String lastName,
        String patronymic,
        String profileImagePath,
        boolean activated,
        String personalNumber,
        String documentId,
        String authority,
        Timestamp documentIssuedAt,
        Timestamp documentExpiresAt,
        String citizenship,
        Timestamp birthDate,
        BigDecimal balance

) {
}
