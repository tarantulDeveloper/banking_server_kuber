package kg.saimatelecom.catalog.service.impl;

import kg.saimatelecom.catalog.model.Purchase;
import kg.saimatelecom.catalog.repository.HistoryRepository;
import kg.saimatelecom.catalog.service.HistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HistoryServiceImpl implements HistoryService {

    HistoryRepository historyRepository;

    @Override
    public List<Purchase> getPurchasesHistory(String email) {
        return historyRepository.findAllExistingByOwnerEmail(email);
    }
}
