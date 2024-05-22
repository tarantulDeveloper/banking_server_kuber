package kg.saimatelecom.transactionservice.service;

import kg.saimatelecom.transactionservice.dto.response.TransactionRequestResponse;
import kg.saimatelecom.transactionservice.model.TransactionRequest;

import java.util.List;

public interface TransactionRequestService {

    List<TransactionRequestResponse> getAllPending();

    void save(TransactionRequest build);

    TransactionRequest getById(Long id);
}
