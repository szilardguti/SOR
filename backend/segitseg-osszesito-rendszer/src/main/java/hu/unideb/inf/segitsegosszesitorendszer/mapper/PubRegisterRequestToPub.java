package hu.unideb.inf.segitsegosszesitorendszer.mapper;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.request.PubRegisterRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PubRegisterRequestToPub implements Converter<PubRegisterRequest, Pub>  {

    private final PasswordEncoder passwordEncoder;

    public Pub convert(MappingContext<PubRegisterRequest, Pub> mappingContext) {
        PubRegisterRequest source = mappingContext.getSource();

        Pub destination = new Pub();

        destination.setUsername(source.username());
        destination.setEmail(source.email());
        destination.setPassword(passwordEncoder.encode(source.password()));
        destination.setName(source.name());
        destination.setLocation(source.location());

        destination.setOpenMonday(source.openMonday());
        destination.setOpenTuesday(source.openTuesday());
        destination.setOpenWednesday(source.openWednesday());
        destination.setOpenThursday(source.openThursday());
        destination.setOpenFriday(source.openFriday());
        destination.setOpenSaturday(source.openSaturday());
        destination.setOpenSunday(source.openSunday());

        return destination;
    }
}
