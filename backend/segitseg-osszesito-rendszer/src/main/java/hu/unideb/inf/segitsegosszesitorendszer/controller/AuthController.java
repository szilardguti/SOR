package hu.unideb.inf.segitsegosszesitorendszer.controller;


import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.PubRegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.UserRegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.UserService;
import hu.unideb.inf.segitsegosszesitorendszer.service.pub.IPubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final UserService userService;
    private final IPubService pubService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                               @RequestParam(defaultValue = "false", name = "pub") boolean isPub){
        log.info(String.format("Login query param: %s", isPub));

        LoginResponse response = isPub
                ? pubService.login(loginRequest)
                : userService.login(loginRequest);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register/user")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest) {
        RegisterResponse response = userService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @PostMapping("/register/pub")
    public ResponseEntity<RegisterResponse> registerPub(@Valid @RequestBody PubRegisterRequest registerRequest) {
        RegisterResponse response = pubService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
