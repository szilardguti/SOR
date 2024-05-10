package hu.unideb.inf.segitsegosszesitorendszer.config;

import hu.unideb.inf.segitsegosszesitorendszer.mapper.PubRegisterRequestToPub;
import hu.unideb.inf.segitsegosszesitorendszer.mapper.RegisterRequestToUser;
import hu.unideb.inf.segitsegosszesitorendszer.mapper.UserToRegisterResponse;
import hu.unideb.inf.segitsegosszesitorendszer.request.PubRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final RegisterRequestToUser registerRequestToUser;
    private final UserToRegisterResponse userToRegisterResponse;
    private final PubRegisterRequestToPub pubRegisterRequestToPub;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(registerRequestToUser);
        modelMapper.addConverter(userToRegisterResponse);
        modelMapper.addConverter(pubRegisterRequestToPub);
        return modelMapper;
    }
}
