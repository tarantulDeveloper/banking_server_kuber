package kg.saimatelecom.mailing.event;


public record SendRegisterMessageEvent(
        String to,
        String name,
        String token
) {
}
