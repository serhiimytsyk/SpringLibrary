package smytsyk.final_project.spring.library.library.entitiy;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Book order
 * Contains information about reader, book, return date, and status
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id", nullable = false)
    private Integer id;

    @Column(name = "reader_id", nullable = false)
    private int readerId;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "order_status_id", nullable = false)
    private int orderStatusId;
}
