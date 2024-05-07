package hu.unideb.inf.segitsegosszesitorendszer.controller;


import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.RegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(userService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequest));

    }
}
