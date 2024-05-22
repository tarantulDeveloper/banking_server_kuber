package kg.saimatelecom.mailing.listeners;

import kg.saimatelecom.mailing.configurations.RabbitMqConfig;
import kg.saimatelecom.mailing.event.SendUserRegistrationRejectionMessageEvent;
import kg.saimatelecom.mailing.service.MailSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendUserRegistrationRejectionMessageListener {
    MailSender mailSender;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_SEND_REGISTRATION_REJECTION_MESSAGE_EVENT)
    void handle(SendUserRegistrationRejectionMessageEvent event) {
        mailSender.sendUserRegistrationRejectionMessage(event.to(), event.name());
    }
}
