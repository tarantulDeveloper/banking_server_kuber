package kg.saimatelecom.catalog.dto.response;

import java.math.BigDecimal;

public record ProductResponseForClient(
        Long id,
        String name,
        String imageUrl,
        Integer quantity,
        BigDecimal price
) {
}
