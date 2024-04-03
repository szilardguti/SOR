package hu.unideb.inf.segitsegosszesitorendszer.response;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String username;
    private String email;
    private Set<String> roles;
}
