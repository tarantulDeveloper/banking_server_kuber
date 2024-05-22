package kg.saimatelecom.catalog.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record IncomeResponseForDealer(
        Long id,
        LocalDate createdAt,
        BigDecimal incomeAmount,
        BigDecimal systemPortion,
        BigDecimal dealerPortion,
        BigDecimal cost,
        BigDecimal realCost,
        Long productId,
        String productName
)
{

}