package kg.saimatelecom.catalog.endpoint;

import kg.saimatelecom.catalog.dto.response.IncomeResponseForAdmin;
import kg.saimatelecom.catalog.dto.response.IncomeResponseForDealer;
import kg.saimatelecom.catalog.dto.response.MonthlyIncomeResponseForManager;
import kg.saimatelecom.catalog.exceptions.DivisionByZeroException;
import kg.saimatelecom.catalog.feign.CurrencyServiceFeignClient;
import kg.saimatelecom.catalog.mapper.IncomeMapper;
import kg.saimatelecom.catalog.service.IncomeService;
import kg.saimatelecom.catalog.utils.ExcelWriterForAdmin;
import kg.saimatelecom.catalog.utils.ExcelWriterForDealer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IncomeEndpoint {

    IncomeService incomeService;
    CurrencyServiceFeignClient currencyServiceFeignClient;
    ExcelWriterForAdmin excelWriterForAdmin;
    ExcelWriterForDealer excelWriterForDealer;
    RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    IncomeMapper incomeMapper;


    public List<IncomeResponseForAdmin> getAllSystemIncomes(String isoCode, LocalDate startDate, LocalDate endDate, List<String> products) {
        List<IncomeResponseForAdmin> incomeInSoftcoin = incomeService.getSystemIncomes(startDate, endDate,products);

        Pair<BigDecimal, BigDecimal> rates = getRates(isoCode);

        if (rates == null) {
            return incomeInSoftcoin;
        }

        BigDecimal currencyRate = rates.getLeft();
        BigDecimal softCoinRate = rates.getRight();

        return incomeInSoftcoin.stream()
                .map(incomeResponseForAdmin -> (IncomeResponseForAdmin)convertToCurrency(incomeResponseForAdmin, currencyRate, softCoinRate))
                .collect(Collectors.toList());
    }

    public List<IncomeResponseForDealer> getAllDealerIncomesResponses(String isoCode, LocalDate startDate, LocalDate endDate, List<String> products, String email) {
        List<IncomeResponseForDealer> incomeInSoftcoin = incomeService.getDealerIncomes(startDate, endDate,products,email);

        Pair<BigDecimal, BigDecimal> rates = getRates(isoCode);

        if (rates == null) {
            return incomeInSoftcoin;
        }

        BigDecimal currencyRate = rates.getLeft();
        BigDecimal softCoinRate = rates.getRight();

        return incomeInSoftcoin.stream()
                .map(IncomeResponseForDealer -> (IncomeResponseForDealer) convertToCurrency(IncomeResponseForDealer, currencyRate, softCoinRate))
                .collect(Collectors.toList());
    }

    public BigDecimal getAllSystemIncomesSum(String isoCode) {
        BigDecimal incomesSumInSoftcoin = incomeService.getIncomesSum();

        if (incomesSumInSoftcoin == null) {
            return BigDecimal.ZERO;
        }

        Pair<BigDecimal, BigDecimal> rates = getRates(isoCode);

        if(rates == null) {
            return incomesSumInSoftcoin;
        }

        BigDecimal currencyRate = rates.getLeft();
        BigDecimal softCoinRate = rates.getRight();

        BigDecimal incomeAmountDivideBySoftCoinRateResult = incomesSumInSoftcoin.divide(softCoinRate, 4, roundingMode);

        return incomeAmountDivideBySoftCoinRateResult.divide(currencyRate, 4, roundingMode);
    }

    public List<MonthlyIncomeResponseForManager> getSystemMonthlyIncome(Optional<Integer> year, String isoCode) {
        List<MonthlyIncomeResponseForManager> incomes;
        if (year.isPresent()) {
            incomes = getMonthlyIncomeResponseForManagers(isoCode, incomeService.getSystemMonthlyIncomeByYear(year.get()));
        } else {
            incomes = getMonthlyIncomeResponseForManagers(isoCode,incomeService.getSystemMonthlyIncome());
        }


        Map<String, BigDecimal> incomeMap = new HashMap<>();
        for (MonthlyIncomeResponseForManager income : incomes) {
            incomeMap.put(income.month(), income.total());
        }

        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] months = dfs.getMonths();

        List<MonthlyIncomeResponseForManager> result = new ArrayList<>();
        for (int i = 0; i < months.length - 1; i++) {
            String monthName = months[i];
            BigDecimal sum = incomeMap.getOrDefault(monthName,BigDecimal.ZERO);
            result.add(new MonthlyIncomeResponseForManager(monthName, sum));
        }
        return result;
    }

    private Pair<BigDecimal, BigDecimal> getRates(String isoCode) {
        if (isoCode.equals("SFC")) {
            return null;
        }

        BigDecimal currencyRate = currencyServiceFeignClient.getCurrencyByIsoCode(isoCode).value();
        BigDecimal softCoinRate = currencyServiceFeignClient.getCurrencyByIsoCode("SFC").value();

        if (currencyRate.compareTo(BigDecimal.ZERO) == 0 || softCoinRate.compareTo(BigDecimal.ZERO) == 0) {
            throw new DivisionByZeroException();
        }

        return Pair.of(currencyRate, softCoinRate);
    }

    private Object convertToCurrency(Object object, BigDecimal currencyRate, BigDecimal softCoinRate) {
        if (object instanceof IncomeResponseForAdmin incomeResponseForAdmin) {
            BigDecimal incomeAmount = convert(incomeResponseForAdmin.incomeAmount(), currencyRate, softCoinRate);
            BigDecimal cost = convert(incomeResponseForAdmin.cost(), currencyRate, softCoinRate);
            BigDecimal realCost = convert(incomeResponseForAdmin.realCost(), currencyRate, softCoinRate);
            return incomeMapper.toIncomeResponseForAdminInCurrency(incomeResponseForAdmin, incomeAmount, incomeResponseForAdmin.systemPortion(), incomeResponseForAdmin.dealerPortion(), cost, realCost);
        }else if (object instanceof IncomeResponseForDealer incomeResponseForDealer){
            BigDecimal incomeAmount = convert(incomeResponseForDealer.incomeAmount(), currencyRate, softCoinRate);
            BigDecimal cost = convert(incomeResponseForDealer.cost(), currencyRate, softCoinRate);
            BigDecimal realCost = convert(incomeResponseForDealer.realCost(), currencyRate, softCoinRate);
            return new IncomeResponseForDealer(incomeResponseForDealer.id(),incomeResponseForDealer.createdAt(), incomeAmount,incomeResponseForDealer.systemPortion(),
                    incomeResponseForDealer.dealerPortion(), cost, realCost,incomeResponseForDealer.productId(),incomeResponseForDealer.productName());
        }
        return null;
    }



    private BigDecimal convert(BigDecimal amount, BigDecimal currencyRate, BigDecimal softCoinRate) {
        return amount.divide(softCoinRate, 4, roundingMode).divide(currencyRate, 4, roundingMode);
    }

    private List<MonthlyIncomeResponseForManager> getMonthlyIncomeResponseForManagers(String isoCode, List<MonthlyIncomeResponseForManager> monthlyIncomes) {
        Pair<BigDecimal, BigDecimal> rates = getRates(isoCode);

        if (rates == null) {
            return monthlyIncomes;
        }

        BigDecimal currencyRate = rates.getLeft();
        BigDecimal softCoinRate = rates.getRight();

        return monthlyIncomes.stream()
                .map(monthlyIncome -> {
                    BigDecimal totalIncomePerMonthDividedBySoftCoinRateResult = monthlyIncome.total().divide(softCoinRate, 4, roundingMode);
                    return new MonthlyIncomeResponseForManager(monthlyIncome.month(), totalIncomePerMonthDividedBySoftCoinRateResult.divide(currencyRate, 4, roundingMode));
                })
                .collect(Collectors.toList());
    }


    public ResponseEntity<byte[]> exportSystemIncomes(String isoCode,
                                                      LocalDate startDate, LocalDate endDate, List<String> products) {
        return excelWriterForAdmin.export(getAllSystemIncomes(isoCode,startDate,endDate,products));
    }

    public ResponseEntity<byte[]> exportDealerIncomes(String isoCode,
                                                      LocalDate startDate, LocalDate endDate, List<String> products,String email) {
        return excelWriterForDealer.export(getAllDealerIncomesResponses(isoCode,startDate,endDate,products,email));
    }


    public List<IncomeResponseForDealer> getAllDealerIncomes(String isoCode, LocalDate startDate, LocalDate endDate, List<String> products, String email) {
        return getAllDealerIncomesResponses(isoCode,startDate,endDate,products,email);
    }
}

