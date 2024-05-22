package kg.saimatelecom.transactionservice.dto.response;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record TransactionRequestResponse(
        Long id,
        String sender,
        String receiver,
        BigDecimal amount,
        Timestamp createdAt
) {
}
