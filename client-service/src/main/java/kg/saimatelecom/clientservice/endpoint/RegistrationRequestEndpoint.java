package kg.saimatelecom.clientservice.endpoint;

import kg.saimatelecom.clientservice.configurations.RabbitMqConfig;
import kg.saimatelecom.clientservice.dto.response.MessageResponse;
import kg.saimatelecom.clientservice.dto.response.RegistrationRequestResponse;
import kg.saimatelecom.clientservice.enums.RegistrationRequestStatus;
import kg.saimatelecom.clientservice.enums.Role;
import kg.saimatelecom.clientservice.event.SendRegisterMessageEvent;
import kg.saimatelecom.clientservice.event.SendUserRegistrationRejectionMessageEvent;
import kg.saimatelecom.clientservice.mapper.RegistrationRequestMapper;
import kg.saimatelecom.clientservice.mapper.UserMapper;
import kg.saimatelecom.clientservice.model.ClientAccount;
import kg.saimatelecom.clientservice.model.Document;
import kg.saimatelecom.clientservice.model.RegistrationRequest;
import kg.saimatelecom.clientservice.model.User;
import kg.saimatelecom.clientservice.service.ClientAccountService;
import kg.saimatelecom.clientservice.service.DocumentService;
import kg.saimatelecom.clientservice.service.RegistrationRequestService;
import kg.saimatelecom.clientservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationRequestEndpoint {
    RegistrationRequestService registrationRequestService;
    RegistrationRequestMapper registrationRequestMapper;
    UserMapper userMapper;
    UserService userService;
    DocumentService documentService;
    ClientAccountService clientAccountService;
    RabbitTemplate mailSenderRabbitTemplate;

    public List<RegistrationRequestResponse> findAllPending() {
        return registrationRequestMapper.productsToProductDTOs(registrationRequestService.findAllPending());
    }

    @Transactional
    public MessageResponse approveRequest(Long requestId) {
        RegistrationRequest registrationRequest = registrationRequestService.findPendingById(requestId);
        String activationToken = String.valueOf(UUID.randomUUID());
        Document document =
                registrationRequestMapper.registrationRequestToDocument(registrationRequest);
        User user = userMapper.fromRegistrationRequestToUserEntity(registrationRequest);
        ClientAccount clientAccount = ClientAccount.builder()
                .balance(new BigDecimal(0))
                .build();
        user.setActivationToken(activationToken);
        user.setRole(Role.ROLE_USER);
        User saved = userService.saveUser(user);
        document.setUsername(saved.getEmail());
        clientAccount.setUsername(saved.getEmail());
        documentService.saveDocument(document);
        clientAccountService.save(clientAccount);
        registrationRequest.setStatus(RegistrationRequestStatus.APPROVED);
        registrationRequestService.saveRequest(registrationRequest);

        mailSenderRabbitTemplate.convertAndSend(
                RabbitMqConfig.QUEUE_SEND_REGISTER_MESSAGE_EVENT,
                SendRegisterMessageEvent.builder()
                        .to(registrationRequest.getEmail())
                        .name(registrationRequest.getLastName() +
                                " " + registrationRequest.getFirstName())
                        .token(activationToken)
                        .build());

        return new MessageResponse("Request has been approved");
    }

    public MessageResponse rejectRequest(Long requestId) {
        RegistrationRequest registrationRequest = registrationRequestService.findPendingById(requestId);
        registrationRequest.setStatus(RegistrationRequestStatus.REJECTED);
        registrationRequestService.saveRequest(registrationRequest);

        mailSenderRabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_SEND_REGISTRATION_REJECTION_MESSAGE_EVENT,
                SendUserRegistrationRejectionMessageEvent.builder()
                        .to(registrationRequest.getEmail())
                        .name(registrationRequest.getLastName() +
                                " " + registrationRequest.getFirstName())
                        .build());

        return new MessageResponse("Request has been rejected");
    }
}
