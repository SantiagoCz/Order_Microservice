package santiagoczarny.orders.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import santiagoczarny.orders.entities.Order;
import santiagoczarny.orders.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public List<Order> findAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id){
        return orderRepository.findById(id);
    }

    public List<Order> findOrdersByCustomerId(Long id) {
        return orderRepository.findOrdersByIdCustomer(id);
    }
}
