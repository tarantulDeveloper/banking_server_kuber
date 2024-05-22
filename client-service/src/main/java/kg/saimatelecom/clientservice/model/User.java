package kg.saimatelecom.clientservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.saimatelecom.clientservice.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@Setter
public class User {
    @Id
    Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    boolean deleted;
    String email;
    @JsonIgnore
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