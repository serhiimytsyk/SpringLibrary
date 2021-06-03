package smytsyk.final_project.spring.library.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class CommonController {
    private final UserService userService;

    @Autowired
    public CommonController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String goToMainPage() {
        return "/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String goToLoginPage(Model model) {
        User user = (User) model.getAttribute("user");
        if (user != null) {
            switch (user.getRoleId()) {
                case 0: return "redirect:/banned/cabinet";
                case 1: return "redirect:/reader/cabinet";
                case 2: return "redirect:/librarian/cabinet";
                case 3: return "redirect:/admin/cabinet";
                default: return "redirect:/error";
            }
        }
        return "/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String goToRegisterPage() {
        return "/register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, Model model) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        Optional<User> optionalUser = userService.getUserByLoginAndPassword(login, password);
        return "redirect:/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req, Model model) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String email = req.getParameter("email");
        if (true) {
            User user = userService.insertUser(login, password, firstName, lastName, email);
            if (user != null) {
                model.addAttribute("user", user);
                return "redirect:/login";
            }
        }
        return "redirect:/register";
    }
}
