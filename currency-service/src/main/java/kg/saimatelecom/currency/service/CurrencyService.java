package kg.saimatelecom.currency.service;

import kg.saimatelecom.currency.dto.request.SetSoftcoinCurrencyRequest;
import kg.saimatelecom.currency.dto.response.CurrencyResponse;
import kg.saimatelecom.currency.dto.response.MessageResponse;
import kg.saimatelecom.currency.model.Currency;

import java.util.List;

public interface CurrencyService {
    void updateCurrencyValuesTask();

    void initValues();

    List<CurrencyResponse> getCurrencies();

    List<CurrencyResponse> getCurrenciesByCodes(List<String> isoCodes);

    MessageResponse setSoftcoinCurrency(SetSoftcoinCurrencyRequest setSoftcoinCurrencyRequest);

    Currency findByIsoCode(String isoCode);

    CurrencyResponse getCurrencyResponseByIsoCode(String isoCode);

    CurrencyResponse getCurrencyResponseById(Long currencyId);
}
