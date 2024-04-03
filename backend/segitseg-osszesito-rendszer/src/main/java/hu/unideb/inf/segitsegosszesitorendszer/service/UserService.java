package hu.unideb.inf.segitsegosszesitorendszer.service;

import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.repository.RoleRepository;
import hu.unideb.inf.segitsegosszesitorendszer.repository.UserRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.RegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public LoginResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return LoginResponse.builder()
                    .accessToken(jwtService.GenerateToken(loginRequest.getUsername()))
                    .build();
        }
        throw new UsernameNotFoundException("Nincs regisztrálva ilyen felhasználó.");
    }

    public RegisterResponse register(RegisterRequest registerRequest){
        User user = modelMapper.map(registerRequest, User.class);
        User savedUser = userRepository.save(user);
        log.info(savedUser.getRoles().toString());
        return modelMapper.map(savedUser, RegisterResponse.class);
    }
}
