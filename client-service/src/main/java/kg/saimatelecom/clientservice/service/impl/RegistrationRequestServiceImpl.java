package kg.saimatelecom.clientservice.service.impl;

import kg.saimatelecom.clientservice.exceptions.ResourceNotFoundException;
import kg.saimatelecom.clientservice.model.RegistrationRequest;
import kg.saimatelecom.clientservice.repository.RegistrationRequestRepository;
import kg.saimatelecom.clientservice.service.RegistrationRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RegistrationRequestServiceImpl implements RegistrationRequestService {

    RegistrationRequestRepository registrationRequestRepository;

    @Override
    public List<RegistrationRequest> findAllPending() {
        return registrationRequestRepository.findAllPending();
    }

    @Override
    public void saveRequest(RegistrationRequest registrationRequest) {
        registrationRequestRepository.save(registrationRequest);
    }

    @Override
    public RegistrationRequest findPendingById(Long id) {
        return registrationRequestRepository.findPendingById(id).orElseThrow(
                () -> new ResourceNotFoundException("Registration request with id " + id + " not found")
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return registrationRequestRepository.existsByEmail(email);
    }
}
