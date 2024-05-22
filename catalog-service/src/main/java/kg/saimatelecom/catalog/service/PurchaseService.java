package kg.saimatelecom.catalog.service;

import kg.saimatelecom.catalog.dto.response.MonthlyPurchaseResponseForManager;
import kg.saimatelecom.catalog.model.Purchase;

import java.util.List;

public interface PurchaseService {

    void save(Purchase purchase);

    Integer getTotalPurchases();

    List<MonthlyPurchaseResponseForManager> getSystemMonthlyPurchasesByYear(Integer integer);

    List<MonthlyPurchaseResponseForManager> getSystemMonthlyPurchases();
}

