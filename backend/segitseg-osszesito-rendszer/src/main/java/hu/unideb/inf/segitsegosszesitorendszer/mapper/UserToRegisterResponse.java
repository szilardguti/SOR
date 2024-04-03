package hu.unideb.inf.segitsegosszesitorendszer.mapper;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Role;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserToRegisterResponse implements Converter<User, RegisterResponse> {

    @Override
    public RegisterResponse convert(MappingContext<User, RegisterResponse> mappingContext) {

        User source = mappingContext.getSource();
        RegisterResponse destination = new RegisterResponse();
        destination.setUsername(source.getUsername());
        destination.setEmail(source.getEmail());
        destination.setRoles(source.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()));

        return destination;
    }
}
