package kg.saimatelecom.catalog.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record IncomeResponseForAdmin(
        Long id,
        LocalDate createdAt,
        String dealerEmail,
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
