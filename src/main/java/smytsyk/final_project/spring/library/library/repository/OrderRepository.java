package smytsyk.final_project.spring.library.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smytsyk.final_project.spring.library.library.entitiy.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByOrderStatusId(int id);
    Page<Order> findAllByOrderStatusId(int id, Pageable pageable);
    Page<Order> findAllByOrderStatusIdAndReaderId(int statusId, int readerId, Pageable pageable);
}
