package kg.saimatelecom.clientservice.service;

import kg.saimatelecom.clientservice.dto.response.ChangeProfileInfoResponse;
import kg.saimatelecom.clientservice.model.ProfileChangeRequest;

import java.util.List;

public interface ProfileChangeRequestService {
    void save(ProfileChangeRequest profileChangeRequest);

    ProfileChangeRequest getById(Long id);

    List<ChangeProfileInfoResponse> getAllPending();
}
