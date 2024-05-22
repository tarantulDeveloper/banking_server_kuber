package kg.saimatelecom.clientservice.controllers;

import kg.saimatelecom.clientservice.dto.response.MessageResponse;
import kg.saimatelecom.clientservice.dto.response.RegistrationRequestResponse;
import kg.saimatelecom.clientservice.endpoint.RegistrationRequestEndpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/registration/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationRequestController {

    RegistrationRequestEndpoint registrationRequestEndpoint;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping
    public List<RegistrationRequestResponse> getRegistrationRequests() {
        return registrationRequestEndpoint.findAllPending();
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/approve/{requestId}")
    public MessageResponse approveRegistrationRequestById(
            @PathVariable Long requestId) {
        return registrationRequestEndpoint.approveRequest(requestId);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/reject/{requestId}")
    public MessageResponse rejectRegistrationRequestById(@PathVariable Long requestId) {
        return registrationRequestEndpoint.rejectRequest(requestId);
    }


}
