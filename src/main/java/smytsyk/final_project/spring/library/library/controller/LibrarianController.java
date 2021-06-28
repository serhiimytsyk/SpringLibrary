package smytsyk.final_project.spring.library.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import smytsyk.final_project.spring.library.library.entitiy.Book;
import smytsyk.final_project.spring.library.library.entitiy.Order;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.service.BookService;
import smytsyk.final_project.spring.library.library.service.EmailSenderService;
import smytsyk.final_project.spring.library.library.service.OrderService;
import smytsyk.final_project.spring.library.library.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LibrarianController {
    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;
    private final EmailSenderService emailSenderService;
    private static final int USERS_PER_PAGE = 5;
    private static final int ORDERS_PER_PAGE = 10;

    @Autowired
    public LibrarianController(UserService userService, BookService bookService, OrderService orderService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/librarian/cabinet")
    public String goToCabinet() {
        return "/librarian/cabinet";
    }

    //readers

    @GetMapping("/librarian/readers")
    public String goToReadersPage(Model model) {
        return goToReadersPage(1, model);
    }

    @GetMapping(value = "/librarian/readers/{pageNo}")
    public String goToReadersPage(@PathVariable("pageNo") int page,
                                Model model) {
        Page<User> readersPage = userService.findReadersByPage(page, USERS_PER_PAGE);
        List<User> readers = readersPage.getContent();

        model.addAttribute("page", page);
        model.addAttribute("totalPages", readersPage.getTotalPages());
        model.addAttribute("readers", readers);

        return "/librarian/readers";
    }

    //orders

    @GetMapping("/librarian/orders")
    public String goToOrdersPage(Model model) {
        return goToOrdersPage(1, model);
    }

    @GetMapping("/librarian/orders/{pageNo}")
    public String goToOrdersPage(@PathVariable("pageNo") int page,
                                 Model model) {
        Page<Order> ordersPage = orderService.findAllOrderRequests(page, ORDERS_PER_PAGE);
        List<Order> orders = ordersPage.getContent();

        Map<Integer, String> orderIdToBookName = new HashMap<>();
        Map<Integer, String> orderIdToUsername = new HashMap<>();

        orders.forEach(o -> {
            orderIdToBookName.put(o.getId(), bookService.getBookById(o.getBookId()).getName());
            orderIdToUsername.put(o.getId(), userService.getUserById(o.getReaderId()).getUsername());
        });

        model.addAttribute("page", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("orders", orders);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("orderIdToBookName", orderIdToBookName);
        model.addAttribute("orderIdToUsername", orderIdToUsername);

        return "/librarian/orders";
    }

    @GetMapping("/librarian/reader_orders/{id}/{pageNo}")
    public String goToReaderOrdersPage(@PathVariable("pageNo") int page,
                                       @PathVariable("id") int id,
                                       Model model) {
        Page<Order> ordersPage = orderService.findAllOrdersByUserId(id, page, ORDERS_PER_PAGE);
        List<Order> orders = ordersPage.getContent();
        Map<Integer, String> orderIdToBookName = new HashMap<>();

        orders.forEach(o -> orderIdToBookName.put(o.getId(), bookService.getBookById(o.getBookId()).getName()));

        model.addAttribute("page", page);
        model.addAttribute("id", id);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("orders", orders);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("orderIdToBookName", orderIdToBookName);

        return "/librarian/reader_orders";
    }

    @PostMapping("/librarian/orders/accept/{id}")
    public String acceptOrder(@PathVariable("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order.getReturnDate().isBefore(LocalDate.now())) {
            model.addAttribute("orderWithErrorDate", id);
            return goToOrdersPage(1, model);
        }
        if (orderService.getIdsOfNotFreeBooks().contains(order.getBookId())) {
            model.addAttribute("orderWithErrorBook", id);
            return goToOrdersPage(1, model);
        }
        orderService.changeOrderStatus(order, 1);
        return "redirect:/librarian/orders";
    }

    @PostMapping("/librarian/orders/reject/{id}")
    public String rejectOrder(@PathVariable("id") int id) {
        orderService.deleteOrder(id);
        return "redirect:/librarian/orders";
    }

    @PostMapping("/librarian/orders/close/{id}")
    public String closeOrder(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id);
        orderService.changeOrderStatus(order, 2);
        return "redirect:/librarian/reader_orders/" + order.getReaderId() + "/1";
    }

    @PostMapping("/librarian/orders/notify/{id}")
    public String notify(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id);
        User user = userService.getUserById(order.getReaderId());
        Book book = bookService.getBookById(order.getBookId());
        emailSenderService.sendNotification(user, book, order);
        return "redirect:/librarian/reader_orders/" + order.getReaderId() + "/1";
    }
}
