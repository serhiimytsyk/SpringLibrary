package smytsyk.final_project.spring.library.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smytsyk.final_project.spring.library.library.entitiy.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findAllByIdIn(List<Integer> ids, Pageable pageable);
}
