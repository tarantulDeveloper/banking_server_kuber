package kg.saimatelecom.transactionservice.repository;

import kg.saimatelecom.transactionservice.dto.response.TransactionResponse;
import kg.saimatelecom.transactionservice.mapper.TransactionMapper;
import kg.saimatelecom.transactionservice.model.Transaction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionRepository {

    JdbcTemplate jdbcTemplate;

    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (sender_id, receiver_id, amount, deleted, created_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transaction.getSender(), transaction.getReceiver(),
                transaction.getAmount(), false, new Timestamp(new Date().getTime()));
    }

    public List<TransactionResponse> findTransactionsByEmailAndDateRange(String email, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT sender.email as sender_email, receiver.email as receiver_email, t.amount, t.created_at " +
                "FROM transactions t " +
                "JOIN client_accounts sender_account ON t.sender_id = sender_account.id " +
                "JOIN client_accounts receiver_account ON t.receiver_id = receiver_account.id " +
                "JOIN users sender ON sender_account.user_id = sender.id " +
                "JOIN users receiver ON receiver_account.user_id = receiver.id " +
                "WHERE (sender.email = ? OR receiver.email = ?) " +
                "AND t.created_at BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, ps -> {
            ps.setString(1, email);
            ps.setString(2, email);
            ps.setTimestamp(3, startDate);
            ps.setTimestamp(4, endDate);
        }, new TransactionMapper());
    }
}
