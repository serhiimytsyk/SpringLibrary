package smytsyk.final_project.spring.library.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import smytsyk.final_project.spring.library.library.dto.OrderDTO;
import smytsyk.final_project.spring.library.library.entitiy.Book;
import smytsyk.final_project.spring.library.library.entitiy.Order;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.service.BookService;
import smytsyk.final_project.spring.library.library.service.OrderService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReaderController {
    private final OrderService orderService;
    private final BookService bookService;
    private static final int BOOKS_PER_PAGE = 5;
    private static final int ORDERS_PER_PAGE = 10;

    @Autowired
    public ReaderController(OrderService orderService, BookService bookService) {
        this.orderService = orderService;
        this.bookService = bookService;
    }


    @GetMapping("/reader/cabinet")
    public String goToCabinet() {
        return "/reader/cabinet";
    }

    //orders

    @GetMapping("/reader/orders")
    public String goToOrdersPage(@AuthenticationPrincipal User user,
                                 Model model) {
        return goToOrdersPage(user,1, model);
    }

    @GetMapping("/reader/orders/{pageNo}")
    public String goToOrdersPage(@AuthenticationPrincipal User user,
                                 @PathVariable("pageNo") int page,
                                 Model model) {
        Page<Order> ordersPage = orderService.findAllOrdersByUserId(user.getId(), page, ORDERS_PER_PAGE);
        List<Order> orders = ordersPage.getContent();

        Map<Integer, String> orderIdToBookName = new HashMap<>();
        Map<Integer, String> orderIdToFine = new HashMap<>();

        orders.forEach(o -> {
            orderIdToBookName.put(o.getId(), bookService.getBookById(o.getBookId()).getName());
            orderIdToFine.put(o.getId(), orderService.getFine(o.getReturnDate()));
        });

        model.addAttribute("page", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("orders", orders);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("orderIdToBookName", orderIdToBookName);
        model.addAttribute("orderIdToFine", orderIdToFine);

        return "/reader/orders";
    }

    //catalog

    @GetMapping("/reader/catalog")
    public String goToCatalog(Model model) {
        return goToCatalog(1, "id", model);
    }

    @GetMapping("/reader/catalog/{pageNo}")
    public String goToCatalog(@PathVariable("pageNo") int page,
                              @RequestParam("sortField") String sortField,
                              Model model) {
        Page<Book> booksPage = bookService.findFreeBooksByPage(page, BOOKS_PER_PAGE, sortField);
        List<Book> books = booksPage.getContent();

        model.addAttribute("page", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("books", books);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return "/reader/catalog";
    }

    @GetMapping("/reader/make_order/{id}")
    public String goToMakeOrderPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("orderDTO", new OrderDTO(LocalDate.now()));
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return "/reader/make_order";
    }

    @PostMapping("/reader/make_order/{id}")
    public String makeOrder(@PathVariable("id") int bookId,
                            @AuthenticationPrincipal User user,
                            @Valid @ModelAttribute("orderDTO") OrderDTO orderDTO,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("incorrectDateFormat", 1);
            model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            model.addAttribute("book", bookService.getBookById(bookId));
            return "/reader/make_order";
        }
        if (orderDTO.getReturnDate().isBefore(LocalDate.now())) {
            model.addAttribute("incorrectDatePast", 1);
            model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            model.addAttribute("book", bookService.getBookById(bookId));
            return "/reader/make_order";
        }
        orderService.makeOrder(bookId, user.getId(), orderDTO.getReturnDate());
        return "redirect:/reader/catalog";
    }

}
