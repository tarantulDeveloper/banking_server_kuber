package kg.saimatelecom.catalog.endpoint;

import kg.saimatelecom.catalog.dto.request.PurchaseRequest;
import kg.saimatelecom.catalog.dto.response.ClientFullInfoResponse;
import kg.saimatelecom.catalog.dto.response.CurrencyResponse;
import kg.saimatelecom.catalog.dto.response.MessageResponse;
import kg.saimatelecom.catalog.dto.response.MonthlyPurchaseResponseForManager;
import kg.saimatelecom.catalog.enums.OwnerType;
import kg.saimatelecom.catalog.exceptions.PurchaseQuantityException;
import kg.saimatelecom.catalog.feign.CurrencyServiceFeignClient;
import kg.saimatelecom.catalog.feign.UserServiceFeignClient;
import kg.saimatelecom.catalog.model.Income;
import kg.saimatelecom.catalog.model.Product;
import kg.saimatelecom.catalog.model.Purchase;
import kg.saimatelecom.catalog.service.IncomeService;
import kg.saimatelecom.catalog.service.ProductService;
import kg.saimatelecom.catalog.service.PurchaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.util.*;



@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseEndpoint {
    PurchaseService purchaseService;
    ProductService productService;
    UserServiceFeignClient userServiceFeignClient;
    CurrencyServiceFeignClient currencyServiceFeignClient;
    IncomeService incomeService;

    @Transactional
    public MessageResponse buyProduct(PurchaseRequest purchaseRequest, String dealerEmail) {
        ClientFullInfoResponse userByEmail = userServiceFeignClient.getClientFullInfoByEmail(dealerEmail);
        Product productById = productService.getProductById(purchaseRequest.productId());
        if (productById.getQuantity() >= purchaseRequest.quantity()) {
            productById.setQuantity(productById.getQuantity() - purchaseRequest.quantity());
            CurrencyResponse softCoin = currencyServiceFeignClient.getCurrencyByIsoCode("SFC");

            BigDecimal ownPriceInSoftCoin = (productById.getPrice().multiply(
                            BigDecimal.valueOf(purchaseRequest.quantity()))
                    .multiply(currencyServiceFeignClient.getCurrencyByCurrencyId(productById.getCurrencyId()).value()).multiply(softCoin.value()));
            BigDecimal benefitInSoftCoin = (productById.getCommission().multiply(
                            BigDecimal.valueOf(purchaseRequest.quantity()))
                    .multiply(currencyServiceFeignClient.getCurrencyByCurrencyId(productById.getCurrencyId()).value()).multiply(softCoin.value()));
            BigDecimal portionForDealer = benefitInSoftCoin.multiply(productById.getPercentageOfProfitForDealer()).divide(BigDecimal.valueOf(100),0,RoundingMode.HALF_EVEN);
            BigDecimal portionForSystem = benefitInSoftCoin.multiply(productById.getPercentageOfProfitForSystem()).divide(BigDecimal.valueOf(100),0,RoundingMode.HALF_EVEN);
            Purchase purchase = Purchase.builder()
                    .product(productById)
                    .quantity(purchaseRequest.quantity())
                    .username(userByEmail.email())
                    .cost(benefitInSoftCoin.add(ownPriceInSoftCoin).setScale(0,RoundingMode.UP))
                    .realCost(ownPriceInSoftCoin.setScale(0,RoundingMode.DOWN))
                    .build();
            purchaseService.save(purchase);
            Income incomeForDealer = Income.builder()
                    .purchase(purchase)
                    .incomeAmount(portionForDealer)
                    .ownerType(OwnerType.DEALER)
                    .build();
            Income incomeForSystem = Income.builder()
                    .purchase(purchase)
                    .incomeAmount(portionForSystem)
                    .ownerType(OwnerType.SYSTEM)
                    .build();
            incomeService.save(incomeForDealer);
            incomeService.save(incomeForSystem);
            return new MessageResponse("Purchase completed successfully");
        }

        throw new PurchaseQuantityException();
    }



    public Integer getTotalPurchases() {
        return purchaseService.getTotalPurchases();
    }

    public List<MonthlyPurchaseResponseForManager> getSystemMonthlyPurchase(Optional<Integer> year) {
        List<MonthlyPurchaseResponseForManager> purchases;
        if (year.isPresent()) {
            purchases = purchaseService.getSystemMonthlyPurchasesByYear(year.get());
        } else {
            purchases = purchaseService.getSystemMonthlyPurchases();
        }
        Map<String, Long> incomeMap = new HashMap<>();
        for (MonthlyPurchaseResponseForManager purchase : purchases) {
            incomeMap.put(purchase.month(), purchase.total());
        }
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] months = dfs.getMonths();
        List<MonthlyPurchaseResponseForManager> result = new ArrayList<>();
        for (int i = 0; i < months.length - 1; i++) {
            String monthName = months[i];
            Long sum = incomeMap.getOrDefault(monthName,0L);
            result.add(new MonthlyPurchaseResponseForManager(monthName, sum));
        }
        return result;
    }
}

