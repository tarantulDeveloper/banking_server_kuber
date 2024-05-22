package kg.saimatelecom.currency.dto.response;

import lombok.Builder;

@Builder
public record MessageResponse (
        String message
) {
}
