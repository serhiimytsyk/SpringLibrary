package smytsyk.final_project.spring.library.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LibrarianController {
    @GetMapping("/librarian/cabinet")
    public String goToCabinet() {
        return "/librarian/cabinet";
    }
}
