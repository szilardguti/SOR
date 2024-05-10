package hu.unideb.inf.segitsegosszesitorendszer.service.pub;

import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.PubRegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;

import java.util.Optional;

public interface IPubService {
    RegisterResponse register(PubRegisterRequest registerRequest);

    boolean pubExists(String username,  String email);

    LoginResponse login(LoginRequest loginRequest);
}
