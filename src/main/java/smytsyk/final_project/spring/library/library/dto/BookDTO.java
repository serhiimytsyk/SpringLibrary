package smytsyk.final_project.spring.library.library.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import smytsyk.final_project.spring.library.library.entitiy.Book;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    @Size(min = 1, max = 30)
    private String name;

    @Size(min = 1, max = 60)
    private String author;

    @Size(min = 1, max = 30)
    private String publisher;

    @NonNull
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate publicationDate;

    public BookDTO(Book book) {
        this(book.getName(), book.getAuthor(), book.getPublisher(), book.getPublicationDate());
    }
}
