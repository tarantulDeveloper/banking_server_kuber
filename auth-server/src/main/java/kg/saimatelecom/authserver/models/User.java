package kg.saimatelecom.authserver.models;

import kg.saimatelecom.authserver.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Getter
@Builder
@Table(name = "users")
public class User {
    @Id
    Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    boolean deleted;
    String email;
    String password;
    Role role;
    String phoneNumber;
    String firstName;
    String lastName;
    String patronymic;
    String profileImagePath;
    boolean activated;
    String activationToken;
}
