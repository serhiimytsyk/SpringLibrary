package smytsyk.final_project.spring.library.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smytsyk.final_project.spring.library.library.entitiy.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getByLoginAndPassword(String login, String password);
}
