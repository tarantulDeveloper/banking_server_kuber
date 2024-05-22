package kg.saimatelecom.mailing.service;

public interface MailSender {
    void sendRegisterMessage(String to, String name, String token);
    void sendUserRegistrationRejectionMessage(String to, String name);
}
