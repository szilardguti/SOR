package hu.unideb.inf.segitsegosszesitorendszer.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class GuestSecurityConfig {

    @Bean
    @Order(3)
    public SecurityFilterChain filterChainGuest(HttpSecurity http) throws Exception {
        http.securityMatcher("/auth/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
