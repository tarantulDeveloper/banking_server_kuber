package kg.saimatelecom.catalog.service;

import kg.saimatelecom.catalog.model.Purchase;

import java.util.List;

public interface HistoryService {
    List<Purchase> getPurchasesHistory(String email);
}
