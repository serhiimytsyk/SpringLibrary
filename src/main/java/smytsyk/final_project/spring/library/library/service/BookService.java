package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.dto.BookDTO;
import smytsyk.final_project.spring.library.library.entitiy.Book;
import smytsyk.final_project.spring.library.library.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final OrderService orderService;

    @Autowired
    public BookService (BookRepository bookRepository, OrderService orderService) {
        this.bookRepository = bookRepository;
        this.orderService = orderService;
    }
    
    public Page<Book> findBooksByPage(int page, int booksPerPage, String sortField) {
        Pageable pageable = PageRequest.of(page - 1, booksPerPage, Sort.by(sortField));
        return bookRepository.findAll(pageable);
    }

    public List<Integer> getAllBooksIds() {
        return bookRepository.findAll().stream().map(Book::getId).collect(Collectors.toList());
    }

    public Page<Book> findFreeBooksByPage(int page, int booksPerPage, String sortField) {
        Pageable pageable = PageRequest.of(page - 1, booksPerPage, Sort.by(sortField));
        List<Integer> ids = getAllBooksIds();
        ids.removeAll(orderService.getIdsOfNotFreeBooks());
        return bookRepository.findAllByIdIn(ids, pageable);
    }

    public Book insertBookFromDTO(BookDTO bookDTO) {
        Book book = Book.builder()
                .name(bookDTO.getName())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .publicationDate(bookDTO.getPublicationDate())
                .build();
        return bookRepository.save(book);
    }

    public Book updateBookFromDTO(BookDTO bookDTO, int id) {
        Book book = Book.builder()
                .id(id)
                .name(bookDTO.getName())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .publicationDate(bookDTO.getPublicationDate())
                .build();
        return bookRepository.save(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public Book getBookById(int id) {
        return bookRepository.getById(id);
    }
}
