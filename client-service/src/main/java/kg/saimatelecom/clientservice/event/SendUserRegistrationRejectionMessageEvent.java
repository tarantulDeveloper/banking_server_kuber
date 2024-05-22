package kg.saimatelecom.clientservice.event;

import lombok.Builder;

@Builder
public record SendUserRegistrationRejectionMessageEvent (
        String to,
        String name
) {}
