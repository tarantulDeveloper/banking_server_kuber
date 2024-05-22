package kg.saimatelecom.transactionservice.dto.response;

import lombok.Builder;

@Builder
public record MessageResponse (
        String message
) {
}
