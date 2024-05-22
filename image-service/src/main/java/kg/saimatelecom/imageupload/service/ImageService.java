package kg.saimatelecom.imageupload.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String upload(MultipartFile multipartFile);
}
