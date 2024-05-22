package kg.saimatelecom.mailing.service.impl;

import kg.saimatelecom.mailing.service.MailSender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailSenderImpl implements MailSender {
    JavaMailSender mailSender;

    @Value("${server.host}")
    String host;
    @Value("${spring.mail.username}")
    String fromEmail;

    public MailSenderImpl(JavaMailSender javaMailSender,
                          @Value("${server.host}") String host,
                          @Value("${spring.mail.username}") String fromMail
    ) {
        this.mailSender = javaMailSender;
        this.host = host;
        this.fromEmail = fromMail;
    }


    @Override
    @Async
    public void sendRegisterMessage(String to, String name, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setSubject("Подтверждение почты");
            message.setTo(to);
            message.setText(MailUtils.getRegistrationMessage(name, host, token));
            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException(e.getMessage());
        }
    }

    @Override
    public void sendUserRegistrationRejectionMessage(String to, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setSubject("Отклонение запроса на регистрацию");
            message.setTo(to);
            message.setText(MailUtils.getUserRegistrationRejectionMessage(name));
            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException(e.getMessage());
        }
    }

    private static class MailUtils {
        public static String getRegistrationMessage(String name, String host, String token) {
            return "Привет, " + name + "\n\nВаш аккаунт на платформе " +
                    "Software Updater Banking создан! Пожалуйста, " +
                    "пройдите по ссылке, чтобы активировать аккаунт: \n\n"
                    + getVerificationUrl(host, token);
        }

        public static String getUserRegistrationRejectionMessage(String name) {
            return "Привет, " + name + "\n\nВаш запрос на регистрацию на платформе " +
                    "Software Updater Banking был отклонен! ";
        }
    }

    private static String getVerificationUrl(String host, String token) {
        return host + "/api/auth/activate?token=" + token;
    }
}
