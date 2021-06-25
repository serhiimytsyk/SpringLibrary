package smytsyk.final_project.spring.library.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import smytsyk.final_project.spring.library.library.dto.BookDTO;
import smytsyk.final_project.spring.library.library.dto.UserDTO;
import smytsyk.final_project.spring.library.library.entitiy.Book;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.service.BookService;
import smytsyk.final_project.spring.library.library.service.UserService;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final BookService bookService;
    private static final int BOOKS_PER_PAGE = 5;
    private static final int USERS_PER_PAGE = 10;

    @Autowired
    public AdminController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/admin/cabinet")
    public String goToCabinet() {
        return "/admin/cabinet";
    }

    //books

    @GetMapping("/admin/books")
    public String goToBooksPage(Model model) {
        return goToBooksPage(1, "id", model);
    }

    @GetMapping(value = "/admin/books/{pageNo}")
    public String goToBooksPage(@PathVariable("pageNo") int page,
                                @RequestParam("sortField") String sortField,
                                Model model) {
        Page<Book> booksPage = bookService.findBooksByPage(page, BOOKS_PER_PAGE, sortField);
        List<Book> books = booksPage.getContent();

        model.addAttribute("page", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("books", books);
        model.addAttribute("sortField", sortField);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return "/admin/books";
    }

    @GetMapping("/admin/books/add")
    public String goToAddBookPage(Model model) {
        model.addAttribute("bookDTO", new BookDTO());
        return "/admin/book_add";
    }

    @GetMapping("/admin/books/update/{id}")
    public String goToUpdateBookPage(@PathVariable("id") int id,
                                     Model model) {
        Book bookToUpdate = bookService.getBookById(id);
        model.addAttribute("id", id);
        model.addAttribute("bookDTO", new BookDTO(bookToUpdate));
        return "/admin/book_update";
    }

    @GetMapping("/admin/books/delete/{id}")
    public String goToDeleteBookPage(@PathVariable("id") int id,
                                     Model model) {
        model.addAttribute("bookToDelete", bookService.getBookById(id));
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return "/admin/book_delete";
    }

    @PostMapping("/admin/books/add")
    public String addBook(@Valid @ModelAttribute("bookDTO") BookDTO bookDTO,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/admin/book_add";
        bookService.insertBookFromDTO(bookDTO);
        return "redirect:/admin/books";
    }

    @PostMapping("/admin/books/update/{id}")
    public String updateBook(@PathVariable("id") int id,
                             @Valid @ModelAttribute("bookDTO") BookDTO bookDTO,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "/admin/book_update";
        }
        bookService.updateBookFromDTO(bookDTO, id);
        return "redirect:/admin/books";
    }

    @PostMapping("/admin/books/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    //users

    @GetMapping("/admin/users")
    public String goToUsersPage(Model model) {
        return goToUsersPage(1, model);
    }

    @GetMapping(value = "/admin/users/{pageNo}")
    public String goToUsersPage(@PathVariable("pageNo") int page,
                                Model model) {
        Page<User> usersPage = userService.findUsersByPage(page, USERS_PER_PAGE);
        List<User> users = usersPage.getContent();

        model.addAttribute("page", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("users", users);

        return "/admin/users";
    }

    @PostMapping("/admin/users/setRole/{id}")
    public String changeRole(@PathVariable int id,
                    @RequestParam("roleId") int roleId) {
        userService.changeUserRole(id, roleId);
        return "redirect:/admin/users";
    }
}
