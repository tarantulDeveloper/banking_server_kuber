package kg.saimatelecom.mailing.event;

public record SendUserRegistrationRejectionMessageEvent (
    String to,
    String name
) {}
