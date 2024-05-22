package kg.saimatelecom.clientservice.model;

import kg.saimatelecom.clientservice.enums.RegistrationRequestStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "registration_requests")
@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    @Id
    Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    boolean deleted;
    String email;
    String password;
    String phoneNumber;
    String personalNumber;
    String documentId;
    String authority;
    Timestamp documentIssuedAt;
    Timestamp documentExpiresAt;
    String citizenship;
    Timestamp birthDate;
    String firstName;
    String lastName;
    String patronymic;
    String profileImagePath;
    RegistrationRequestStatus status;
}
