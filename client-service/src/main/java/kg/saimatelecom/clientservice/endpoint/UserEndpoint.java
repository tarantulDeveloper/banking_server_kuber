package kg.saimatelecom.clientservice.endpoint;

import kg.saimatelecom.clientservice.dto.request.*;
import kg.saimatelecom.clientservice.dto.response.*;
import kg.saimatelecom.clientservice.enums.ProfileChangeRequestStatus;
import kg.saimatelecom.clientservice.enums.Role;
import kg.saimatelecom.clientservice.exceptions.ResourceNotFoundException;
import kg.saimatelecom.clientservice.exceptions.UserIsAlreadyRegisteredException;
import kg.saimatelecom.clientservice.feign.CurrencyServiceFeignClient;
import kg.saimatelecom.clientservice.feign.ImageServiceFeignClient;
import kg.saimatelecom.clientservice.mapper.UserMapper;
import kg.saimatelecom.clientservice.model.ClientAccount;
import kg.saimatelecom.clientservice.model.Document;
import kg.saimatelecom.clientservice.model.ProfileChangeRequest;
import kg.saimatelecom.clientservice.model.User;
import kg.saimatelecom.clientservice.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEndpoint {

    UserService userService;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ImageServiceFeignClient imageServiceFeignClient;
    DocumentService documentService;
    ProfileChangeRequestService profileChangeRequestService;
    CurrencyServiceFeignClient currencyServiceFeignClient;
    RegistrationRequestService registrationRequestService;
    ClientAccountService clientAccountService;

    public UserPersonalAccountResponse getPersonalInfo(String email) {
        return userService.findByEmail(email);
    }

    public List<UserFullInfoResponse> getAllUsers() {
        return userMapper.toUserFullInfoResponse(userService.getAllUsers());
    }

    public UserFullInfoResponse getUserById(Long userId) {
        return userMapper.toUserFullInfoResponse(userService.getUserById(userId));
    }

    public UserFullInfoResponse addSystemUser(SystemUserAddRequest userInfoRequest) {
        if (userService.userExistsByEmail(userInfoRequest.email())) {
            throw new UserIsAlreadyRegisteredException();
        }

        if (userInfoRequest.role().equalsIgnoreCase("user")) {
            throw new IllegalArgumentException("Invalid role.");
        }

        User user = User.builder()
                .email(userInfoRequest.email())
                .password(passwordEncoder.encode(userInfoRequest.password()))
                .phoneNumber(userInfoRequest.phoneNumber())
                .role(Role.valueOf(userInfoRequest.role().toUpperCase()))
                .firstName(userInfoRequest.firstName())
                .lastName(userInfoRequest.lastName())
                .patronymic(userInfoRequest.patronymic())
                .profileImagePath("")
                .activated(true)
                .activationToken("")
                .build();

        return userMapper.toUserFullInfoResponse(userService.saveUser(user));
    }

    public UserFullInfoResponse updateSystemUser(SystemUserUpdateRequest userUpdateRequest) {
        User user = userService.getUserById(userUpdateRequest.id());

        if (user.getRole().equals(Role.ROLE_USER)) {
            throw new IllegalArgumentException("You can't change client's role.");
        }

        user.setRole(Role.valueOf(userUpdateRequest.role().toUpperCase()));
        user.setPhoneNumber(userUpdateRequest.phoneNumber());
        user.setFirstName(userUpdateRequest.firstName());
        user.setLastName(userUpdateRequest.lastName());
        user.setPatronymic(userUpdateRequest.patronymic());
        return userMapper.toUserFullInfoResponse(userService.saveUser(user));
    }

    public UserFullInfoResponse enableOrDisableUser(UserEnableOrDisableRequest userEnableOrDisableRequest) {
        User user = userService.getUserById(userEnableOrDisableRequest.id());
        user.setActivated(userEnableOrDisableRequest.activated());
        return userMapper.toUserFullInfoResponse(userService.saveUser(user));
    }

    public MessageResponse deleteUser(UserDeleteRequest userDeleteRequest) {
        User user = userService.getUserById(userDeleteRequest.id());

        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User not found!");
        }

        user.setDeleted(userDeleteRequest.deleted());
        user.setActivated(false);
        userService.saveUser(user);
        return new MessageResponse("User has been successfully deleted");
    }

    public ClientFullInfoResponse getClientByUsername(String username) {
        User user = userService.findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Document document = documentService.findDocumentByUsername(username);

        return userMapper.toClientInfoResponse(user, document);
    }

    public ClientFullInfoResponse updateClient(ClientUpdateRequest clientUpdateRequest) {
        User user = userService.getUserById(clientUpdateRequest.id());
        Document document = documentService.findDocumentByUsername(user.getEmail());

        user.setDeleted(clientUpdateRequest.deleted());
        user.setPhoneNumber(clientUpdateRequest.phoneNumber());
        user.setFirstName(clientUpdateRequest.firstName());
        user.setLastName(clientUpdateRequest.lastName());
        user.setPatronymic(clientUpdateRequest.patronymic());
        user.setActivated(clientUpdateRequest.activated());

        document.setAuthority(clientUpdateRequest.authority());
        document.setDocumentId(clientUpdateRequest.documentId());
        document.setDocumentExpiresAt(clientUpdateRequest.documentExpiresAt());
        document.setDocumentIssuedAt(clientUpdateRequest.documentIssuedAt());
        document.setCitizenship(clientUpdateRequest.citizenship());

        return userMapper.toClientInfoResponse(userService.saveUser(user), documentService.saveDocument(document));
    }

    public MessageResponse updateUserImage(String userEmail, UserPhotoUpdateRequest userUpdateRequest) {
        User user = userService.findUserByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User not found!")
        );

        String createdUrl = imageServiceFeignClient.uploadImage(userUpdateRequest.profileImg());
        log.info("Image has been saved to url " + createdUrl);

        user.setProfileImagePath(createdUrl);
        userService.saveUser(user);
        return MessageResponse.builder()
                .message(user.getProfileImagePath())
                .build();
    }

    public MessageResponse updatePersonalInfo(String userEmail, ChangeProfileInfoRequest request) {

        ProfileChangeRequest profileChangeRequest = ProfileChangeRequest.builder()
                .dealerEmail(userEmail)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .patronymic(request.patronymic())
                .phoneNumber(request.phoneNumber())
                .documentId(request.documentId())
                .authority(request.authority())
                .documentIssuedAt(request.documentIssuedAt())
                .documentExpiresAt(request.documentExpiresAt())
                .citizenship(request.citizenship())
                .birthDate(request.birthDate())
                .status(ProfileChangeRequestStatus.PENDING)
                .build();

        profileChangeRequestService.save(profileChangeRequest);
        return new MessageResponse("Profile changes request has been accepted successfully");
    }

    public BigDecimal getPersonalBalance(String username, String isoCode) {
        BigDecimal balanceInSoftcoin = userService.getPersonalBalance(username);

        if(isoCode.equals("SFC")) {
            return balanceInSoftcoin;
        }
        BigDecimal currencyRate = currencyServiceFeignClient.getCurrencyByIsoCode(isoCode).value();
        BigDecimal softCoinRate = currencyServiceFeignClient.getCurrencyByIsoCode("SFC").value();

        BigDecimal balanceInSoftcoinDividedBySoftcoinRate = balanceInSoftcoin.divide(softCoinRate, 4, RoundingMode.HALF_EVEN);
        return balanceInSoftcoinDividedBySoftcoinRate.divide(currencyRate, 4, RoundingMode.HALF_EVEN);
    }

    public MessageResponse addDealerByManager(RegistrationRequestForDealer request) {
        if (userService.userExistsByEmail(request.email()) || registrationRequestService.existsByEmail(request.email())) {
            throw new UserIsAlreadyRegisteredException();
        }

        User dealer = userMapper.fromRegistrationRequestForDealerRequestToUserEntity(request);
        dealer.setRole(Role.ROLE_USER);
        dealer.setDeleted(false);
        dealer.setActivated(true);
        dealer.setPassword(passwordEncoder.encode(request.password()));
        dealer.setProfileImagePath("https://wallpapers.com/images/hd/cool-profile-picture-1ecoo30f26bkr14o.jpg");
        dealer.setActivationToken("");
        userService.saveUser(dealer);

        Document document = userMapper.fromRegistrationRequestForDealerRequestToDocumentEntity(request);
        document.setUsername(dealer.getEmail());
        documentService.saveDocument(document);

        ClientAccount clientAccount = ClientAccount.builder()
                .balance(BigDecimal.ZERO)
                .username(dealer.getEmail())
                .deleted(false)
                .build();
        clientAccountService.save(clientAccount);

        return new MessageResponse("Dealer was saved successfully!");
    }

    public AdminOrManagerInfoResponse getAdminOrManagerByUsername(String username) {
        return userMapper.toAdminOrManagerInfoResponse(userService.findUserByEmail(username)
                .orElseThrow( () -> new ResourceNotFoundException("User not found!")));
    }

    public AdminOrManagerInfoResponse updateAdminOrManagerInfo(String username, AdminOrManagerUpdateRequest request) {
        User user = userService.findUserByEmail(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found!")
        );

        user.setPhoneNumber(request.phoneNumber());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPatronymic(request.patronymic());
        return userMapper.toAdminOrManagerInfoResponse(userService.saveUser(user));
    }

    public MessageResponse activateUserByToken(String token) {
        User user = userService.findByActivationToken(token).orElseThrow(
                () -> new ResourceNotFoundException("User not found!")
        );
        user.setActivated(true);
        user.setActivationToken("");
        userService.saveUser(user);
        return new MessageResponse("Your account has been activated");
    }
}
