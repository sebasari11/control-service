package proyecto.ucuenca.control.client;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import proyecto.ucuenca.control.model.User;

@FeignClient(name="user-service", path="/user")
public interface UserClient {
    @RequestLine("GET /{id}")
    public User getUser(@Param Long id);
}
