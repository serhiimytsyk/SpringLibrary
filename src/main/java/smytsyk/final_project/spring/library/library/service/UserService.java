package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.dto.UserDTO;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.repository.UserRepository;

import java.util.Optional;

@Service
@Qualifier("userDetailsService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean insertUserFromDTO(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.getByLogin(userDTO.getLogin());
        if (optionalUser.isPresent()) return false;

        User user = User.builder().login(userDTO.getLogin())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getLastName())
                .roleId(1).build();
        userRepository.saveAndFlush(user);
        return true;
    }

    public boolean updateUserFromDTO(User user, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.getByLogin(userDTO.getLogin());
        if (optionalUser.isEmpty()) return false;

        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        userRepository.saveAndFlush(user);
        return true;
    }


    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.getByLogin(s).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
    }
}
