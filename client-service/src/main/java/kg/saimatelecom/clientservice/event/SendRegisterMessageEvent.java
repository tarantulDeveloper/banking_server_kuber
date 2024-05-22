package kg.saimatelecom.clientservice.event;

import lombok.Builder;

@Builder
public record SendRegisterMessageEvent(
        String to,
        String name,
        String token
) {
}