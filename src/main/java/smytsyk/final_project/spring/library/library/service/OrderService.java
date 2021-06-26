package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.entitiy.Order;
import smytsyk.final_project.spring.library.library.repository.BookRepository;
import smytsyk.final_project.spring.library.library.repository.OrderRepository;
import smytsyk.final_project.spring.library.library.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(BookRepository bookRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<Integer> getIdsOfNotFreeBooks() {
        return orderRepository.findAllByOrderStatusId(1).stream().map(Order::getBookId).collect(Collectors.toList());
    }

    public Page<Order> findAllOrderRequests(int page, int ordersPerPage) {
        Pageable pageable = PageRequest.of(page - 1, ordersPerPage, Sort.by("id"));
        return orderRepository.findAllByOrderStatusId(0, pageable);
    }

    public Page<Order> findAllOrdersByUserId(int id, int page, int ordersPerPage) {
        Pageable pageable = PageRequest.of(page - 1, ordersPerPage, Sort.by("id"));
        return orderRepository.findAllByOrderStatusIdAndReaderId(1, id, pageable);
    }

    public boolean changeOrderStatus(int id, int status) {
        Order order = orderRepository.getById(id);
        if (status == 1) {
            if (order.getReturnDate().isBefore(LocalDate.now())) return false;
        }
        order.setOrderStatusId(status);
        orderRepository.save(order);
        return true;
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    public Order getOrderById(int id) {
        return orderRepository.getById(id);
    }
}
