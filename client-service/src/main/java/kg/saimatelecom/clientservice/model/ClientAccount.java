package kg.saimatelecom.clientservice.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "client_accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class ClientAccount {
    @Id
    Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    boolean deleted;
    String username;
    BigDecimal balance;
}
