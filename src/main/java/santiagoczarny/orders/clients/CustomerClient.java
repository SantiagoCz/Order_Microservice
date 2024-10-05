package santiagoczarny.orders.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import santiagoczarny.orders.classes.CustomerDto;

@FeignClient(name = "customers", url = "http://localhost:8081/customer")
public interface CustomerClient {

    @GetMapping("/get-id={id}")
    CustomerDto getCustomerById(@PathVariable Long id);

}
