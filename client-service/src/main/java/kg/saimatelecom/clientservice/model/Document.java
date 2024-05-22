package kg.saimatelecom.clientservice.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "documents")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Document {
    @Id
    Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    boolean deleted;
    String username;
    String personalNumber;
    String documentId;
    String authority;
    Timestamp documentIssuedAt;
    Timestamp documentExpiresAt;
    String citizenship;
    Timestamp birthDate;

}
