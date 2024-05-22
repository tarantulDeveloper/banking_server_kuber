package kg.saimatelecom.catalog.feign;

import kg.saimatelecom.catalog.dto.response.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-service")
public interface CurrencyServiceFeignClient {

    @GetMapping("/api/exchange-rates/{isoCode}")
    CurrencyResponse getCurrencyByIsoCode(@PathVariable("isoCode") String isoCode);

    @GetMapping("/api/exchange-rates/id/{currencyId}")
    CurrencyResponse getCurrencyByCurrencyId(@PathVariable("currencyId") Long currencyId);
}