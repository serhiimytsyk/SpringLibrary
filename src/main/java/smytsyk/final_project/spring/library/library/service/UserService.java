package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.dto.UserDTO;
import smytsyk.final_project.spring.library.library.entitiy.User;
import smytsyk.final_project.spring.library.library.repository.UserRepository;

import java.util.Arrays;
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

    public Page<User> findUsersByPage(int page, int booksPerPage) {
        Pageable pageable = PageRequest.of(page - 1, booksPerPage, Sort.by("id"));
        return userRepository.findAllByRoleIdIn(Arrays.asList(0, 1, 2), pageable);
    }

    public Page<User> findReadersByPage(int page, int booksPerPage) {
        Pageable pageable = PageRequest.of(page - 1, booksPerPage, Sort.by("id"));
        return userRepository.findAllByRoleIdIn(Arrays.asList(1), pageable);
    }

    public boolean insertUserFromDTO(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.getByLogin(userDTO.getLogin());
        if (optionalUser.isPresent()) return false;

        User user = User.builder().login(userDTO.getLogin())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .roleId(1).build();
        userRepository.saveAndFlush(user);
        return true;
    }

    public User updateUserFromDTO(User user, UserDTO userDTO) {
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        return userRepository.saveAndFlush(user);
    }

    public boolean changeUserRole(int userId, int roleId) {
        User user = userRepository.getById(userId);
        int oldId = user.getRoleId();
        user.setRoleId(roleId);
        userRepository.saveAndFlush(user);
        return oldId != roleId;
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.getByLogin(s).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
    }

    public User getUserById(int id) {
        return userRepository.getById(id);
    }
}
