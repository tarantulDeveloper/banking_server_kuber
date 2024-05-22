package kg.saimatelecom.clientservice.mapper;

import kg.saimatelecom.clientservice.dto.request.RegistrationRequestForDealer;
import kg.saimatelecom.clientservice.dto.response.RegistrationRequestResponse;
import kg.saimatelecom.clientservice.model.Document;
import kg.saimatelecom.clientservice.model.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RegistrationRequestMapper {

    @Mapping(target = "password", ignore = true)
    public abstract RegistrationRequest toEntityRegistrationRequest(RegistrationRequestForDealer request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    public abstract Document registrationRequestToDocument(RegistrationRequest registrationRequest);

    public abstract List<RegistrationRequestResponse> productsToProductDTOs(List<RegistrationRequest> products);
}
