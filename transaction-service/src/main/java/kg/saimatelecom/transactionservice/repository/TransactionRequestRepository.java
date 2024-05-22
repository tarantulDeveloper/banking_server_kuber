package kg.saimatelecom.transactionservice.repository;

import kg.saimatelecom.transactionservice.dto.response.TransactionRequestResponse;
import kg.saimatelecom.transactionservice.model.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {

    @Query(value = "SELECT NEW kg.saimatelecom.transactionservice.dto.response.TransactionRequestResponse" +
            "(t.id,t.sender,t.receiver,t.amount,t.createdAt) FROM " +
            "TransactionRequest t where t.status = kg.saimatelecom.transactionservice.enums.TransactionRequestStatus.PENDING")
    List<TransactionRequestResponse> getAllPending();


    @Query(value = "SELECT * FROM transaction_requests tr WHERE tr.status = 'PENDING' AND tr.id = ? LIMIT 1", nativeQuery = true)
    Optional<TransactionRequest> findPendingById(Long id);
}
