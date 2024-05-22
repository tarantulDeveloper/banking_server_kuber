package kg.saimatelecom.transactionservice.service.impl;

import kg.saimatelecom.transactionservice.dto.response.TransactionRequestResponse;
import kg.saimatelecom.transactionservice.exceptions.RequestNotFoundException;
import kg.saimatelecom.transactionservice.model.TransactionRequest;
import kg.saimatelecom.transactionservice.repository.TransactionRequestRepository;
import kg.saimatelecom.transactionservice.service.TransactionRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionRequestServiceImpl implements TransactionRequestService {

    TransactionRequestRepository transactionRequestRepository;

    @Override
    public List<TransactionRequestResponse> getAllPending() {
        return transactionRequestRepository.getAllPending();
    }

    @Override
    public void save(TransactionRequest transactionRequest) {
        transactionRequestRepository.save(transactionRequest);
    }

    @Override
    public TransactionRequest getById(Long id) {
        return transactionRequestRepository.findPendingById(id).orElseThrow(RequestNotFoundException::new);
    }
}

