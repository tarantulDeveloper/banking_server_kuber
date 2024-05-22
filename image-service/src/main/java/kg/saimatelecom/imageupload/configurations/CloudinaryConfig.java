package kg.saimatelecom.imageupload.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryConfig {
    String cloud;
    String api_secret;
    String api_key;

    public CloudinaryConfig(
            @Value("${cloud.name}") String cloud,
            @Value("${api.secret") String api_secret,
            @Value("${api.key}") String api_key) {
        this.cloud = cloud;
        this.api_secret = api_secret;
        this.api_key = api_key;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloud,
                "api_key", api_key,
                "api_secret", api_secret
        ));
    }
}

