package smytsyk.final_project.spring.library.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smytsyk.final_project.spring.library.library.entitiy.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getByLogin(String login);
    Page<User> findAllByRoleIdIn(List<Integer> ids, Pageable pageable);
}
