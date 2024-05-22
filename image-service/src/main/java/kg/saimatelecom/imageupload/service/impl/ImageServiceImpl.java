package kg.saimatelecom.imageupload.service.impl;

import com.cloudinary.Cloudinary;
import kg.saimatelecom.imageupload.service.ImageService;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {

    Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            Transformation transformation = new Transformation().quality(50).fetchFormat("auto");
            return String.valueOf(
                    cloudinary.uploader()
                            .upload(multipartFile.getBytes(), ObjectUtils.asMap(
                                    "transformation", transformation.generate()
                            ))
                            .get("secure_url")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
