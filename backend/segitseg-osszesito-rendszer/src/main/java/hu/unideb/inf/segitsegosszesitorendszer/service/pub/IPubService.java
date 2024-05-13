package hu.unideb.inf.segitsegosszesitorendszer.service.pub;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import hu.unideb.inf.segitsegosszesitorendszer.request.LoginRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.PubRegisterRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.SearchPubStockRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.LoginResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.PubResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.RegisterResponse;

import java.util.List;
import java.util.UUID;

public interface IPubService {
    RegisterResponse register(PubRegisterRequest registerRequest);

    boolean pubExists(String username,  String email);

    LoginResponse login(LoginRequest loginRequest);

    List<Pub> getAll(PubStatus status);

    List<PubResponse> transformPubToPubResponse(List<Pub> pubs);

    void updateStatus(UUID pubUUID, PubStatus status);

    Pub getById(UUID id);

    List<Pub> getAllByStock(SearchPubStockRequest request);
}
