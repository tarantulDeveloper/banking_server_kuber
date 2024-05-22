package kg.saimatelecom.clientservice.service;

import kg.saimatelecom.clientservice.model.ClientAccount;

public interface ClientAccountService {
    ClientAccount findClientAccountByUserEmail(String email);
    void save(ClientAccount clientAccount);
}
