package hu.unideb.inf.segitsegosszesitorendszer.service.pub;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.repository.PubRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.PubRegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.SearchPubStockRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.PubResponse;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PubService implements IPubService {

    private final PubRepository pubRepository;
    private final DaoAuthenticationProvider pubAuthenticationProvider;

    private final ModelMapper modelMapper;
    private final JwtService jwtService;


    @Override
    public RegisterResponse register(PubRegisterRequest registerRequest) {
        if (pubExists(
                registerRequest.username(),
                registerRequest.email())
        )
            throw new UsernameNotFoundException("Már használatban lévő felhasználói adatok!");

        Pub newPub = modelMapper.map(registerRequest, Pub.class);
        newPub.setPubStatus(PubStatus.CREATED);

        pubRepository.save(newPub);

        return new RegisterResponse(
                newPub.getUsername(),
                newPub.getEmail(),
                new HashSet<>(List.of(Roles.PUB.name().toUpperCase()))
        );
    }

    @Override
    public boolean pubExists(String username, String email) {
        if (username != null) {
            if (pubRepository.existsByUsername(username))
                return true;
        }

        if (email != null) {
            if (pubRepository.existsByEmail(email))
                return true;
        }

        return false;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = pubAuthenticationProvider
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
                );

        if(authentication.isAuthenticated()){
            return LoginResponse.builder()
                    .accessToken(jwtService.GenerateToken(loginRequest.getUsername(), Roles.PUB))
                    .build();
        }

        throw new UsernameNotFoundException("Nincs regisztrálva ilyen vendéglátó hely.");
    }

    @Override
    public List<Pub> getAll(PubStatus status) {
        if (status != null)
            return pubRepository.findAllByPubStatus(status);

        return pubRepository.findAll();
    }

    @Override
    public List<PubResponse> transformPubToPubResponse(List<Pub> pubs) {
        List<PubResponse> responses = new ArrayList<>();

        for (Pub pub :
                pubs) {
            PubResponse response = new PubResponse(
                    pub.getPub_id(),
                    pub.getEmail(),
                    pub.getName(),
                    pub.getLocation(),
                    pub.getPubStatus(),

                    pub.getOpenMonday(),
                    pub.getOpenTuesday(),
                    pub.getOpenWednesday(),
                    pub.getOpenThursday(),
                    pub.getOpenFriday(),
                    pub.getOpenSaturday(),
                    pub.getOpenSunday()
            );
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void updateStatus(UUID pubUUID, PubStatus status) {
        Pub pub = getById(pubUUID);

        pub.setPubStatus(status);
        pubRepository.save(pub);
    }

    @Override
    public Pub getById(UUID id) {
        Optional<Pub> pub = pubRepository.findById(id);

        if (pub.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A kiszolgáló hely nem található az azonosítóval: %s", id)
            );
        return pub.get();
    }

    @Override
    public List<Pub> getAllByStock(SearchPubStockRequest request) {
        List<Pub> pubs = getAll(PubStatus.ACCEPTED);

        return pubs.stream()
                .filter(pub
                        -> pub.getStocks().stream()
                        .map(stock -> stock.getStockItem().getItem_id())
                        .collect(Collectors.toSet())
                        .containsAll(request.items())
                )
                .toList();
    }

}
