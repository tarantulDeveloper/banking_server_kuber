package kg.saimatelecom.catalog.feign;

import kg.saimatelecom.catalog.dto.response.ClientFullInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service")
public interface UserServiceFeignClient {

    @GetMapping("/api/users/client/{username}")
    ClientFullInfoResponse getClientFullInfoByEmail(@PathVariable("username") String username);
}
