package kg.saimatelecom.currency.dto.response;

import java.math.BigDecimal;

public record CurrencyResponse(
        String isoCode,
        BigDecimal value,
        Long id
) {
}