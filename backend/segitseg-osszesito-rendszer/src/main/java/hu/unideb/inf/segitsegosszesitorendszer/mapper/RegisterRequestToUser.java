package hu.unideb.inf.segitsegosszesitorendszer.mapper;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Role;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.repository.RoleRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.RegisterRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class RegisterRequestToUser implements Converter<RegisterRequest, User> {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User convert(MappingContext<RegisterRequest, User> mappingContext) {
        RegisterRequest source = mappingContext.getSource();
        User destination = new User();
        destination.setUsername(source.getUsername());
        destination.setEmail(source.getEmail());
        destination.setPassword(passwordEncoder.encode(source.getPassword()));

        Role role = roleRepository.findByRole("USER").orElseThrow(
                () -> new IllegalArgumentException("Role USER not found")
        );
        destination.setRoles(Set.of(role));

        return destination;
    }
}
