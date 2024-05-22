package kg.saimatelecom.transactionservice.enums;

import kg.saimatelecom.clientservice.model.ClientAccount;
import kg.saimatelecom.transactionservice.dto.response.ClientFullInfoResponse;
import kg.saimatelecom.transactionservice.dto.response.MessageResponse;
import kg.saimatelecom.transactionservice.dto.response.TransactionRequestResponse;
import kg.saimatelecom.transactionservice.feign.UserServiceFeignClient;
import kg.saimatelecom.transactionservice.model.Transaction;
import kg.saimatelecom.transactionservice.model.TransactionRequest;
import kg.saimatelecom.transactionservice.service.TransactionRequestService;
import kg.saimatelecom.transactionservice.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionRequestEndpoint {

    TransactionRequestService transactionRequestService;
    TransactionService transactionService;
    UserServiceFeignClient userServiceFeignClient;


    public List<TransactionRequestResponse> getAllPending() {
        return transactionRequestService.getAllPending();
    }


    public MessageResponse rejectById(Long id) {
        TransactionRequest transactionRequest = transactionRequestService.getById(id);
        transactionRequest.setStatus(TransactionRequestStatus.REJECTED);
        transactionRequestService.save(transactionRequest);
        return new MessageResponse("Transaction has been rejected successfully");
    }


    @Transactional
    public MessageResponse approveById(Long id) {
        TransactionRequest transactionRequest = transactionRequestService.getById(id);
        BigDecimal amountToSend = transactionRequest.getAmount();
        ClientFullInfoResponse sender = userServiceFeignClient.getClientFullInfoByEmail(transactionRequest.getSender());
        ClientFullInfoResponse receiver = userServiceFeignClient.getClientFullInfoByEmail(transactionRequest.getReceiver());
        transactionRequest.setStatus(TransactionRequestStatus.APPROVED);
        transactionService.save(Transaction.builder()
                .amount(amountToSend)
                .sender(sender.email())
                .receiver(receiver.email())
                .build());
        return new MessageResponse("Transaction has been approved successfully");
    }
}

