package hu.unideb.inf.segitsegosszesitorendszer.service.security;

import hu.unideb.inf.segitsegosszesitorendszer.config.CustomUserDetails;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PubDetailsServiceImpl implements UserDetailsService {

    private final PubRepository pubRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Pub pub = pubRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nincs regisztrálva ilyen vendéglátó hely"));

        List<GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority(Roles.PUB.name().toUpperCase()));

        return CustomUserDetails.builder()
                .username(pub.getUsername())
                .password(pub.getPassword())
                .authorities(authorities)
                .build();
    }
}
