package kg.saimatelecom.catalog.endpoint;

import kg.saimatelecom.catalog.dto.response.HistoryResponse;
import kg.saimatelecom.catalog.model.Purchase;
import kg.saimatelecom.catalog.service.HistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryEndpoint {

    HistoryService historyService;

    public List<HistoryResponse> getAllPurchases(String email){
        List<Purchase> purchasesHistory = historyService.getPurchasesHistory(email);
        return purchasesHistory.stream().map(purchase ->
                new HistoryResponse(purchase.getUsername() ,purchase.getQuantity(),
                        purchase.getProduct().getName(),purchase.getCost(),purchase.getCreatedAt())).collect(Collectors.toList());
    }
}