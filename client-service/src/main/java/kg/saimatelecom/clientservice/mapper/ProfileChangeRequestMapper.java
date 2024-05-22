package kg.saimatelecom.clientservice.mapper;

import kg.saimatelecom.clientservice.dto.response.ChangeProfileInfoResponse;
import kg.saimatelecom.clientservice.model.ProfileChangeRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileChangeRequestMapper {
    ChangeProfileInfoResponse toProfileInfoResponse(ProfileChangeRequest profileChangeRequest);
}
