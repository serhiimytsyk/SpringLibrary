package smytsyk.final_project.spring.library.library.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @Size(min = 1, max = 30)
    private String login;

    @Size(min = 1, max = 300)
    private String password;

    @Size(min = 1, max = 30)
    private String firstName;

    @Size(min = 1, max = 30)
    private String lastName;

    @Size(min = 1, max = 30)
    @Email
    private String email;
}
