package kg.saimatelecom.currency.mapper;

import kg.saimatelecom.currency.dto.response.CurrencyResponse;
import kg.saimatelecom.currency.model.Currency;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyResponse toCurrencyResponse(Currency currency);

    List<CurrencyResponse> toCurrencyResponseList(List<Currency> currencies);
}
