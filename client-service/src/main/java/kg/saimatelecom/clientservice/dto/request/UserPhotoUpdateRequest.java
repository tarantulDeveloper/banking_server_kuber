package kg.saimatelecom.clientservice.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UserPhotoUpdateRequest(
        @NotNull(message = "Profile image can not be null")
        MultipartFile profileImg
) {
}
