package smytsyk.final_project.spring.library.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import smytsyk.final_project.spring.library.library.dto.UserDTO;
import smytsyk.final_project.spring.library.library.entitiy.Role;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class CommonController {
    private final UserService userService;

    @Autowired
    public CommonController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String goToMainPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/index";
        }
        return redirectToCabinet(authentication.getAuthorities());
    }

    @GetMapping("/login")
    public String goToLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/login";
        }
        return redirectToCabinet(authentication.getAuthorities());
    }

    @PostMapping("/login")
    public String login() {
        return goToCabinet();
    }

    private String redirectToCabinet(Collection<? extends GrantedAuthority> authorities) {
        if (authorities.contains(Role.BANNED)) return "redirect:/banned/cabinet";
        if (authorities.contains(Role.READER)) return "redirect:/reader/cabinet";
        if (authorities.contains(Role.LIBRARIAN)) return "redirect:/librarian/cabinet";
        if (authorities.contains(Role.ADMIN)) return "redirect:/admin/cabinet";
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String goToRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/register";
        } else if (!userService.insertUserFromDTO(userDTO)) {
            model.addAttribute("loginExists", 1);
            return "/register";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/update_reg")
    public String goToChangeInfoPage(Model model,
                                     @AuthenticationPrincipal User user) {
        model.addAttribute("userDTO", new UserDTO(user));
        return "/update_reg";
    }

    @PostMapping("/update_reg")
    public String updateUser(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute("userDTO") UserDTO userDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/update_reg";
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        userService.updateUserFromDTO(user, userDTO);
        return redirectToCabinet(authorities);
    }

    @GetMapping("/go_to_cabinet")
    public String goToCabinet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return redirectToCabinet(auth.getAuthorities());
    }
}
