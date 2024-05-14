package hu.unideb.inf.segitsegosszesitorendszer.service.user;

import hu.unideb.inf.segitsegosszesitorendszer.entity.PubEvent;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Role;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.repository.RoleRepository;
import hu.unideb.inf.segitsegosszesitorendszer.repository.UserRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.UserRegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventAttenderResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final DaoAuthenticationProvider userAuthenticationProvider;
    private final ModelMapper modelMapper;



    public LoginResponse login(LoginRequest loginRequest){
        Authentication authentication = userAuthenticationProvider
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
                );

        if(authentication.isAuthenticated()){
            return LoginResponse.builder()
                    .accessToken(jwtService.GenerateToken(loginRequest.getUsername(), Roles.USER))
                    .build();
        }

        throw new UsernameNotFoundException("Nincs regisztrálva ilyen felhasználó.");
    }

    public RegisterResponse register(UserRegisterRequest registerRequest){
        if (userExists(
                registerRequest.getUsername(),
                registerRequest.getEmail())
        )
            throw new UsernameNotFoundException("Már használatban lévő felhasználói adatok!");

        User user = modelMapper.map(registerRequest, User.class);
        User savedUser = userRepository.save(user);
        log.info(savedUser.getRoles().toString());
        return modelMapper.map(savedUser, RegisterResponse.class);
    }

    public boolean userExists(String username, String email) {
        if (username != null) {
            if (userRepository.existsByUsername(username))
                return true;
        }

        if (email != null) {
            if (userRepository.existsByEmail(email))
                return true;
        }

        return false;
    }

    @Override
    public User getByUsername(String username) throws EntityNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A felhasználó nem található a megadott névvel: %s", username)
            );
        return user.get();
    }

    @Override
    public User getByEmail(String email) throws EntityNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A felhasználó nem található a megadott emaillel: %s", email)
            );
        return user.get();
    }

    @Override
    public User getById(UUID uuid) {
        Optional<User> user = userRepository.findById(uuid);

        if (user.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A felhasználó nem található az azonosítóval: %s", uuid)
            );
        return user.get();
    }

    @Override
    public void addRole(UUID userUUID, Roles roleEnum) {
        User user = getById(userUUID);
        Optional<Role> role = roleRepository.findByRole(roleEnum);

        if (role.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A jogosultság nem található a névvel: %s", roleEnum)
            );
        Set<Role> userRoles = user.getRoles();
        userRoles.add(role.get());
        user.setRoles(userRoles);

        userRepository.save(user);
    }

    @Override
    public List<EventAttenderResponse> transformUserToEventAttenderResponse(List<User> users) {
        List<EventAttenderResponse> responses = new ArrayList<>();

        for (User user:
                users) {
            EventAttenderResponse response = new EventAttenderResponse(
                    user.getUser_id(),
                    user.getUsername(),
                    user.getEmail()
            );
            responses.add(response);
        }
        return responses;
    }
}
