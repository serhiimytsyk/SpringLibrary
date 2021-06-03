package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User insertUser(String login, String password, String firstName, String lastName, String email) {
        User user = User.builder().
                login(login).
                password(password).
                firstName(firstName).
                lastName(lastName).
                email(email).
                roleId(1).build();
        return userRepository.save(user);
    }

    public Optional<User> getUserByLoginAndPassword(String login, String password) {
        return userRepository.getByLoginAndPassword(login, password);
    }
}
