package kg.saimatelecom.clientservice.feign;

import kg.saimatelecom.clientservice.configurations.FeignMultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "image-service", configuration = FeignMultipartSupportConfig.class)
public interface ImageServiceFeignClient {

    @PostMapping(value = "/api/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadImage(@RequestParam("file") MultipartFile file);

}
