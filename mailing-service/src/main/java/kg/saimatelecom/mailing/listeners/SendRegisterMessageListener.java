package kg.saimatelecom.mailing.listeners;

import kg.saimatelecom.mailing.configurations.RabbitMqConfig;
import kg.saimatelecom.mailing.event.SendRegisterMessageEvent;
import kg.saimatelecom.mailing.service.MailSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendRegisterMessageListener {
    MailSender mailSender;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_SEND_REGISTER_MESSAGE_EVENT)
    void handle(SendRegisterMessageEvent event) {
        mailSender.sendRegisterMessage(event.to(), event.name(), event.token());
    }
}
