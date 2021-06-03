package smytsyk.final_project.spring.library.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smytsyk.final_project.spring.library.library.entitiy.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
