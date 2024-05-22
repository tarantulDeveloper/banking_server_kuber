package kg.saimatelecom.catalog.service.impl;

import kg.saimatelecom.catalog.dto.response.MonthlyPurchaseResponseForManager;
import kg.saimatelecom.catalog.model.Purchase;
import kg.saimatelecom.catalog.repository.PurchaseRepository;
import kg.saimatelecom.catalog.service.PurchaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseServiceImpl implements PurchaseService {

    PurchaseRepository purchaseRepository;

    @Override
    public void save(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    @Override
    public Integer getTotalPurchases() {
        return purchaseRepository.getTotalPurchases();
    }

    @Override
    public List<MonthlyPurchaseResponseForManager> getSystemMonthlyPurchasesByYear(Integer integer) {
        return purchaseRepository.getPurchaseTotalByMonth(integer);
    }

    @Override
    public List<MonthlyPurchaseResponseForManager> getSystemMonthlyPurchases() {
        return purchaseRepository.getPurchaseTotalByMonth(Year.now().getValue());
    }
}