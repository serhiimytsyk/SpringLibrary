package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.entitiy.Order;
import smytsyk.final_project.spring.library.library.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

    public Order changeOrderStatus(Order order, int status) {
        order.setOrderStatusId(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    public Order getOrderById(int id) {
        return orderRepository.getById(id);
    }

    public Order makeOrder(int bookId, int userId, LocalDate returnDate) {
        Order order = Order.builder().orderStatusId(0).bookId(bookId).readerId(userId).returnDate(returnDate).build();
        return orderRepository.save(order);
    }

    public String getFine(LocalDate returnDate) {
        LocalDate now = LocalDate.now();
        if (returnDate.isBefore(now)) {
            return String.valueOf(10*DAYS.between(returnDate, now));
        }
        return "-";
    }
}
