package kg.saimatelecom.imageupload.controllers;

import kg.saimatelecom.imageupload.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/images")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ImageController {
    ImageService imageService;

    @PostMapping
    public String uploadImage(@RequestPart("file") MultipartFile file) {
        return imageService.upload(file);
    }

}
