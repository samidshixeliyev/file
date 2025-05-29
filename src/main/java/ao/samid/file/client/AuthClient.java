package ao.samid.file.client;

import ao.samid.file.config.FeignClientInterceptorConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", url = "${client.ms-auth.endpoint}", configuration = FeignClientInterceptorConfig.class)
public interface AuthClient {
    @PostMapping("/auth/access")
    Void checkAccess(@RequestHeader(name="access-token") String accessToken);
}
