package santiagoczarny.orders.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import santiagoczarny.orders.classes.CustomerDto;
import santiagoczarny.orders.classes.Validations;
import santiagoczarny.orders.clients.CustomerClient;
import santiagoczarny.orders.entities.Order;
import santiagoczarny.orders.services.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/all")
    public List<Order> findAllOrders(){
        return orderService.findAllOrders();
    }

    @GetMapping("/get-id={id}")
    public Order getOrderById(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.findOrderById(id);

        return orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
    }

    @GetMapping("/all-by-customer{id}")
    public List<Order> findOrdersByCustomer(@PathVariable Long id) {
        CustomerDto customer = customerClient.getCustomerById(id);
        return orderService.findOrdersByCustomerId(customer.getId());
    }

    @Transactional
    @PostMapping("/save-customer{id}")
    public ResponseEntity<?> save(@RequestBody @Valid Order request,
                                  @PathVariable Long id,
                                  BindingResult result) {
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }

        try {

            CustomerDto customer = customerClient.getCustomerById(id);

            request.setProcessingStatus("Pending");
            request.setIdCustomer(customer.getId());

            orderService.saveOrder(request);

            return ResponseEntity.ok("Order assigned to customer succesfully.");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }

    }

    @Transactional
    @PutMapping("/edit-{id}")
    public ResponseEntity<?> edit(@RequestBody @Valid Order request,
                                  @PathVariable Long id,
                                  BindingResult result) {

        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }

        try {
            Optional<Order> orderOptional = orderService.findOrderById(id);
            orderOptional.orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));

            Order order = orderOptional.get();
            order.setCreationDate(request.getCreationDate());
            order.setItemDescription(request.getItemDescription());
            orderService.saveOrder(order);

            return ResponseEntity.ok("Order modified successfully.");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @PutMapping("/change-status-id={id}")
    public ResponseEntity<?> changeProcessingStatus(@RequestBody String request,
                                                    @PathVariable Long id) {
        try {
            Map<String, String> jsonMap = objectMapper.readValue(request, Map.class);
            String newStatus = jsonMap.get("processingStatus");

            Optional<Order> orderOptional = orderService.findOrderById(id);
            orderOptional.orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));

            @Valid Order order = orderOptional.get();
            order.setProcessingStatus(newStatus);
            orderService.saveOrder(order);

            return ResponseEntity.ok("Order processing status modified successfully.");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

}
