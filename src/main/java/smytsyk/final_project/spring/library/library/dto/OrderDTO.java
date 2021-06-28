package smytsyk.final_project.spring.library.library.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    @NonNull
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate returnDate;
}
