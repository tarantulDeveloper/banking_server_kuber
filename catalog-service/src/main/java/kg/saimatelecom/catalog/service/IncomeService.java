package kg.saimatelecom.catalog.service;

import kg.saimatelecom.catalog.dto.response.IncomeResponseForAdmin;
import kg.saimatelecom.catalog.dto.response.IncomeResponseForDealer;
import kg.saimatelecom.catalog.dto.response.MonthlyIncomeResponseForManager;
import kg.saimatelecom.catalog.model.Income;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeService {
    Income save(Income income);

    List<IncomeResponseForAdmin> getSystemIncomes(LocalDate startDate, LocalDate endDate, List<String> products);

    BigDecimal getIncomesSum();

    List<MonthlyIncomeResponseForManager> getSystemMonthlyIncome();

    List<MonthlyIncomeResponseForManager> getSystemMonthlyIncomeByYear(int year);

    List<IncomeResponseForDealer> getDealerIncomes(LocalDate startDate, LocalDate endDate, List<String> products, String email);

}