package kg.saimatelecom.transactionservice.dto.response;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record TransactionResponse(
        String sender,
        String receiver,
        BigDecimal amount,
        Timestamp createdAt
) {
}
