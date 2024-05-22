package kg.saimatelecom.clientservice.service;

import kg.saimatelecom.clientservice.model.RegistrationRequest;

import java.util.List;

public interface RegistrationRequestService {
    List<RegistrationRequest> findAllPending();

    void saveRequest(RegistrationRequest registrationRequest);

    RegistrationRequest findPendingById(Long id);

    boolean existsByEmail(String email);
}
