package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.entitiy.Order;
import smytsyk.final_project.spring.library.library.repository.BookRepository;
import smytsyk.final_project.spring.library.library.repository.OrderRepository;
import smytsyk.final_project.spring.library.library.repository.UserRepository;

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
}
