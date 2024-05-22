package kg.saimatelecom.mailing.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_SEND_REGISTER_MESSAGE_EVENT = "q.send-register-message-event";
    public static final String QUEUE_SEND_REGISTRATION_REJECTION_MESSAGE_EVENT = "q.send-registration-rejection-message-event";

    @Bean
    Queue sendRegisterMessageEventQueue() {
        return new Queue(QUEUE_SEND_REGISTER_MESSAGE_EVENT);
    }

    @Bean
    Queue sendRegistrationRejectionMessageEventQueue() {
        return new Queue(QUEUE_SEND_REGISTRATION_REJECTION_MESSAGE_EVENT);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
