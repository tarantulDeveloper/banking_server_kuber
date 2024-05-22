package kg.saimatelecom.clientservice.dto.response;

import java.math.BigDecimal;

public record CurrencyResponse(
        String isoCode,
        BigDecimal value
) {
}