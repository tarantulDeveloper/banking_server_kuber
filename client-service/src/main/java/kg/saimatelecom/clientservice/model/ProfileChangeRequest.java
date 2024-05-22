package kg.saimatelecom.clientservice.model;

import kg.saimatelecom.clientservice.enums.ProfileChangeRequestStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name ="profile_changes_requests")
@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileChangeRequest {
    @Id
    Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    boolean deleted;
    String dealerEmail;
    String phoneNumber;
    String firstName;
    String lastName;
    String patronymic;
    String documentId;
    String authority;
    Timestamp documentIssuedAt;
    Timestamp documentExpiresAt;
    String citizenship;
    Timestamp birthDate;
    ProfileChangeRequestStatus status;
}
