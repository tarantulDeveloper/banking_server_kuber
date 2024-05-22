package kg.saimatelecom.currency.controllers;

import jakarta.validation.Valid;
import kg.saimatelecom.currency.dto.request.SetSoftcoinCurrencyRequest;
import kg.saimatelecom.currency.dto.response.CurrencyResponse;
import kg.saimatelecom.currency.dto.response.MessageResponse;
import kg.saimatelecom.currency.service.CurrencyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyController {

    CurrencyService currencyService;

    @GetMapping
    public List<CurrencyResponse> fetchCurrencyValues(
            @RequestParam(value = "isoCodes", required = false) List<String> isoCodes) {
        return isoCodes == null ? currencyService.getCurrencies() : currencyService.getCurrenciesByCodes(isoCodes);
    }

    @PutMapping("/softcoin")
    public MessageResponse setSoftcoinCurrency(@Valid @RequestBody SetSoftcoinCurrencyRequest setSoftcoinCurrencyRequest) {
        return currencyService.setSoftcoinCurrency(setSoftcoinCurrencyRequest);
    }

    @GetMapping("/{isoCode}")
    public CurrencyResponse getCurrencyResponseByIsoCode(@PathVariable("isoCode") String isoCode) {
        return currencyService.getCurrencyResponseByIsoCode(isoCode.toUpperCase());
    }

    @GetMapping("/id/{currencyId}")
    public CurrencyResponse getCurrencyResponseById(@PathVariable("currencyId") Long currencyId) {
        return currencyService.getCurrencyResponseById(currencyId);
    }


}
