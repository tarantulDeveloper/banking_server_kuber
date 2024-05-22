package kg.saimatelecom.transactionservice.model;

import jakarta.persistence.*;
import kg.saimatelecom.transactionservice.enums.TransactionRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "transaction_requests")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionRequest extends BaseEntity {
    String sender;
    String receiver;
    @Column(nullable = false, columnDefinition = "numeric(38,2) check (amount >= 0)")
    BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionRequestStatus status;
}
