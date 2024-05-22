package kg.saimatelecom.clientservice.endpoint;

import kg.saimatelecom.clientservice.dto.response.ChangeProfileInfoResponse;
import kg.saimatelecom.clientservice.dto.response.MessageResponse;
import kg.saimatelecom.clientservice.enums.ProfileChangeRequestStatus;
import kg.saimatelecom.clientservice.exceptions.ResourceNotFoundException;
import kg.saimatelecom.clientservice.model.Document;
import kg.saimatelecom.clientservice.model.ProfileChangeRequest;
import kg.saimatelecom.clientservice.model.User;
import kg.saimatelecom.clientservice.service.DocumentService;
import kg.saimatelecom.clientservice.service.ProfileChangeRequestService;
import kg.saimatelecom.clientservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileChangeRequestEndpoint {
    ProfileChangeRequestService profileChangeRequestService;
    UserService userService;
    DocumentService documentService;

    public MessageResponse approveById(Long id) {
        ProfileChangeRequest request = profileChangeRequestService.getById(id);

        User user = userService.findUserByEmail(request.getDealerEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found!")
        );
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPatronymic(request.getPatronymic());
        user.setPhoneNumber(request.getPhoneNumber());
        userService.saveUser(user);
        Document userDocumentInfo = documentService.findDocumentByUsername(user.getEmail());
        userDocumentInfo.setDocumentId(request.getDocumentId());
        userDocumentInfo.setAuthority(request.getAuthority());
        userDocumentInfo.setDocumentIssuedAt(request.getDocumentIssuedAt());
        userDocumentInfo.setDocumentExpiresAt(request.getDocumentExpiresAt());
        userDocumentInfo.setCitizenship(request.getCitizenship());
        userDocumentInfo.setBirthDate(request.getBirthDate());
        documentService.saveDocument(userDocumentInfo);

        request.setStatus(ProfileChangeRequestStatus.APPROVED);
        profileChangeRequestService.save(request);

        return new MessageResponse("Profile changing request has been approved successfully");
    }

    public MessageResponse rejectById(Long id) {
        ProfileChangeRequest request = profileChangeRequestService.getById(id);
        request.setStatus(ProfileChangeRequestStatus.REJECTED);
        profileChangeRequestService.save(request);
        return new MessageResponse("Profile changing request has been rejected successfully");
    }

    public List<ChangeProfileInfoResponse> getAllPending() {
        return profileChangeRequestService.getAllPending();
    }
}
