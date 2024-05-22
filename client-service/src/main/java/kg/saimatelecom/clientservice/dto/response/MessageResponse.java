package kg.saimatelecom.clientservice.dto.response;

import lombok.Builder;

@Builder
public record MessageResponse(
        String message
) {
}