package smytsyk.final_project.spring.library.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReaderController {
    @GetMapping("/reader/cabinet")
    public String goToCabinet() {
        return "/reader/cabinet";
    }
}
