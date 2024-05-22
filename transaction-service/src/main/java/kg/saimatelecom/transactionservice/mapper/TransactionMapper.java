package kg.saimatelecom.transactionservice.mapper;

import kg.saimatelecom.transactionservice.dto.response.TransactionResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionMapper implements RowMapper<TransactionResponse> {

    @Override
    public TransactionResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        String senderEmail = rs.getString("sender_email");
        String receiverEmail = rs.getString("receiver_email");
        BigDecimal amount = rs.getBigDecimal("amount");
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new TransactionResponse(senderEmail,receiverEmail, amount, createdAt);
    }
}