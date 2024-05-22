package kg.saimatelecom.clientservice.service.impl;

import kg.saimatelecom.clientservice.dto.response.ChangeProfileInfoResponse;
import kg.saimatelecom.clientservice.mapper.ProfileChangeRequestMapper;
import kg.saimatelecom.clientservice.model.ProfileChangeRequest;
import kg.saimatelecom.clientservice.repository.ProfileChangeRequestRepository;
import kg.saimatelecom.clientservice.service.ProfileChangeRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileChangeRequestServiceImpl implements ProfileChangeRequestService {
    ProfileChangeRequestRepository repository;
    ProfileChangeRequestMapper profileChangeRequestMapper;

    @Override
    public void save(ProfileChangeRequest profileChangeRequest) {
        repository.save(profileChangeRequest);
    }

    @Override
    public ProfileChangeRequest getById(Long id) {
        return repository.findPendingById(id).orElseThrow(
                () -> new ResolutionException("Request not found!")
        );
    }

    @Override
    public List<ChangeProfileInfoResponse> getAllPending() {
        return repository.getAllPending().stream()
                .map(profileChangeRequestMapper::toProfileInfoResponse)
                .toList();
    }

}
