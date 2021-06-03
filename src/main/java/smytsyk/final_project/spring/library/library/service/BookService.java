package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final OrderService orderService;

    @Autowired
    public BookService (BookRepository bookRepository, OrderService orderService) {
        this.bookRepository = bookRepository;
        this.orderService = orderService;
    }
}
