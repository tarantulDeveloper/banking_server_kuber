package kg.saimatelecom.transactionservice.service;

import kg.saimatelecom.transactionservice.dto.response.TransactionResponse;
import kg.saimatelecom.transactionservice.model.Transaction;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionService {
    void save(Transaction transaction);
    List<TransactionResponse> getTransactionInfoByEmail(String email, Timestamp startDate, Timestamp endDate);
    ResponseEntity<byte[]> exportTransactionsToExcel(String email, Timestamp startDate, Timestamp endDate);
}
