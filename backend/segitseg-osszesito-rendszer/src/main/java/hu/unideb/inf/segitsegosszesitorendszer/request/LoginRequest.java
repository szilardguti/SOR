package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "A felhasználónév nem lehet üres!")
    @NotNull(message = "A felhasználónév nem lehet null")
    private String username;

    @NotBlank(message = "A jelszó nem lehet üres!")
    @NotNull(message = "A jelszó nem lehet null")
    private String password;
}
