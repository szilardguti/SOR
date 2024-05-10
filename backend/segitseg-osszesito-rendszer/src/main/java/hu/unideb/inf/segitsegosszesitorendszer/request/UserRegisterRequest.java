package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank(message = "A felhasználónév nem lehet üres.")
    private String username;
    @NotBlank(message = "Az email cím nem lehet üres.")
    @Email(message = "Érvénytelen email cím formátum.")
    private String email;
    @NotBlank(message = "A jelszó nem lehet üres.")
    private String password;
}
