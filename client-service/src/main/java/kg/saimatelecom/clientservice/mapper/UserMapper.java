package kg.saimatelecom.clientservice.mapper;


import kg.saimatelecom.clientservice.dto.request.RegistrationRequestForDealer;
import kg.saimatelecom.clientservice.dto.response.AdminOrManagerInfoResponse;
import kg.saimatelecom.clientservice.dto.response.ClientFullInfoResponse;
import kg.saimatelecom.clientservice.dto.response.UserFullInfoResponse;
import kg.saimatelecom.clientservice.model.Document;
import kg.saimatelecom.clientservice.model.RegistrationRequest;
import kg.saimatelecom.clientservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User fromRegistrationRequestToUserEntity(RegistrationRequest registrationRequest);

    @Mapping(target = "password", ignore = true)
    User fromRegistrationRequestForDealerRequestToUserEntity(RegistrationRequestForDealer registrationRequest);

    Document fromRegistrationRequestForDealerRequestToDocumentEntity(RegistrationRequestForDealer request);

    UserFullInfoResponse toUserFullInfoResponse(User user);

    List<UserFullInfoResponse> toUserFullInfoResponse(List<User> users);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.createdAt", target = "createdAt")
    @Mapping(source = "user.updatedAt", target = "updatedAt")
    @Mapping(source = "user.deleted", target = "deleted")
    ClientFullInfoResponse toClientInfoResponse(User user, Document document);

    AdminOrManagerInfoResponse toAdminOrManagerInfoResponse(User user);
}
