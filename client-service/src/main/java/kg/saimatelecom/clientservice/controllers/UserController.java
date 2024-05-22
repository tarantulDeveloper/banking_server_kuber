package kg.saimatelecom.clientservice.controllers;

import jakarta.validation.Valid;
import kg.saimatelecom.clientservice.dto.request.*;
import kg.saimatelecom.clientservice.dto.response.*;
import kg.saimatelecom.clientservice.endpoint.UserEndpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserEndpoint userEndpoint;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    public List<UserFullInfoResponse> getAllUsers() {
        return userEndpoint.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public UserFullInfoResponse getUserById(@PathVariable("userId") Long userId) {
        return userEndpoint.getUserById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullInfoResponse addUser(@Valid @RequestBody SystemUserAddRequest userInfoRequest) {
        return userEndpoint.addSystemUser(userInfoRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public UserFullInfoResponse updateAdminOrManager(@Valid @RequestBody SystemUserUpdateRequest userUpdateRequest) {
        return userEndpoint.updateSystemUser(userUpdateRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enable")
    public UserFullInfoResponse enableOrDisableUser(@Valid @RequestBody UserEnableOrDisableRequest userEnableOrDisableRequest) {
        return userEndpoint.enableOrDisableUser(userEnableOrDisableRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/delete")
    public MessageResponse deleteUser(@Valid @RequestBody UserDeleteRequest userDeleteRequest) {
        return userEndpoint.deleteUser(userDeleteRequest);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/client/{username}")
    public ClientFullInfoResponse getClientById(@PathVariable("username") String username) {
        return userEndpoint.getClientByUsername(username);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/client")
    public ClientFullInfoResponse updateClient(@Valid @RequestBody ClientUpdateRequest clientUpdateRequest) {
        return userEndpoint.updateClient(clientUpdateRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/personal")
    public UserPersonalAccountResponse getPersonalInfo(Principal principal) {
        return userEndpoint.getPersonalInfo(principal.getName());
    }

    @PutMapping(value = "/image")
    public MessageResponse updateProfilePhoto(Principal principal,@Valid @ModelAttribute UserPhotoUpdateRequest userUpdateRequest) {
        return userEndpoint.updateUserImage(principal.getName(), userUpdateRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/personal/update")
    public MessageResponse updatePersonalInfo(Principal principal, @RequestBody ChangeProfileInfoRequest changeProfileInfoRequest) {
        return userEndpoint.updatePersonalInfo(principal.getName(), changeProfileInfoRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/balance")
    public BigDecimal getPersonalBalance(Principal principal,
                                         @RequestParam(name = "isoCode", required = false, defaultValue = "SFC") String isoCode){
        return userEndpoint.getPersonalBalance(principal.getName(), isoCode.toUpperCase());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/dealer")
    public MessageResponse addDealerByManager(@Valid @RequestBody RegistrationRequestForDealer request) {
        return userEndpoint.addDealerByManager(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/admin-manager")
    public AdminOrManagerInfoResponse getAdminOrManagerInfo(Principal principal) {
        return userEndpoint.getAdminOrManagerByUsername(principal.getName());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/update-admin-manager")
    public AdminOrManagerInfoResponse updateAdminOrManagerInfo(Principal principal,
                                                               @RequestBody AdminOrManagerUpdateRequest request) {
        return userEndpoint.updateAdminOrManagerInfo(principal.getName(), request);
    }

    @GetMapping("/activate")
    public MessageResponse activate(@RequestParam("token") String token) {
        return userEndpoint.activateUserByToken(token);
    }
}

