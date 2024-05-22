package kg.saimatelecom.clientservice.service.impl;

import kg.saimatelecom.clientservice.exceptions.ClientAccountNotFoundException;
import kg.saimatelecom.clientservice.model.ClientAccount;
import kg.saimatelecom.clientservice.repository.ClientAccountRepository;
import kg.saimatelecom.clientservice.service.ClientAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientAccountServiceImpl implements ClientAccountService {
    ClientAccountRepository clientAccountRepository;

    @Override
    public ClientAccount findClientAccountByUserEmail(String email) {
        return clientAccountRepository.findByEmail(email).orElseThrow(
                () -> new ClientAccountNotFoundException("Client has not been found")
        );
    }

    @Override
    public void save(ClientAccount clientAccount) {
        clientAccountRepository.save(clientAccount);
    }
}
