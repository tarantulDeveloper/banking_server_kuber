package kg.saimatelecom.transactionservice.endpoint;


import kg.saimatelecom.transactionservice.dto.request.TransactionRequest;
import kg.saimatelecom.transactionservice.dto.response.ClientFullInfoResponse;
import kg.saimatelecom.transactionservice.dto.response.MessageResponse;
import kg.saimatelecom.transactionservice.dto.response.TransactionResponse;
import kg.saimatelecom.transactionservice.enums.TransactionRequestStatus;
import kg.saimatelecom.transactionservice.exceptions.DuplicateEmailException;
import kg.saimatelecom.transactionservice.exceptions.InsufficientFundsException;
import kg.saimatelecom.transactionservice.feign.UserServiceFeignClient;
import kg.saimatelecom.transactionservice.service.TransactionRequestService;
import kg.saimatelecom.transactionservice.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionEndpoint {

    UserServiceFeignClient userServiceFeignClient;
    TransactionRequestService transactionRequestService;
    TransactionService transactionService;


    public MessageResponse createTransaction(TransactionRequest transactionRequest, String email) {
        if (transactionRequest.email().equals(email)) {
            throw new DuplicateEmailException("Duplicate emails has been provided");
        }
        BigDecimal amountToSend = transactionRequest.amount();
        ClientFullInfoResponse sender =
                userServiceFeignClient.getClientFullInfoByEmail(email);
        ClientFullInfoResponse receiver =
                userServiceFeignClient.getClientFullInfoByEmail(transactionRequest.email());
        validate(sender, amountToSend);

        transactionRequestService.save(kg.saimatelecom.transactionservice.model.TransactionRequest.builder()
                .status(TransactionRequestStatus.PENDING)
                .amount(amountToSend)
                .receiver(receiver.email())
                .sender(sender.email())
                .build());

        return new MessageResponse("Transaction request has been accepted successfully");
    }


    private void validate(ClientFullInfoResponse sender, BigDecimal amount) {
        if (!(sender.balance().compareTo(amount) >= 0)) {
            log.error("There are not enough funds on the balance");
            throw new InsufficientFundsException();
        }
    }

    public List<TransactionResponse> getTransactionsInfoByEmail(String email, Timestamp startDate, Timestamp endDate) {
        return transactionService.getTransactionInfoByEmail(email, startDate, endDate);

    }
    public ResponseEntity<byte[]> exportTransactionsToExcel(String email, Timestamp startDate, Timestamp endDate) {
        return transactionService.exportTransactionsToExcel(email, startDate, endDate);

    }

}

