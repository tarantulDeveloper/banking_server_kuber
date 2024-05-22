package kg.saimatelecom.transactionservice.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientBearerTokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authorization)) {
                requestTemplate.header("Authorization", new String[]{authorization});
            }
        }
    }
}
