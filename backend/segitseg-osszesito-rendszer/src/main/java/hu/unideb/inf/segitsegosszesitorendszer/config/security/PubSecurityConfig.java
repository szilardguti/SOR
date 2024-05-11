package hu.unideb.inf.segitsegosszesitorendszer.config.security;

import hu.unideb.inf.segitsegosszesitorendszer.config.jwt.AuthEntryPointJwt;
import hu.unideb.inf.segitsegosszesitorendszer.config.jwt.JwtAuthFilter;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.security.PubDetailsServiceImpl;
import hu.unideb.inf.segitsegosszesitorendszer.service.security.UserServiceDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class PubSecurityConfig {

    private final PubDetailsServiceImpl pubDetailsServiceImpl;
    private final UserServiceDetailsImpl userServiceDetailsImpl;


    private final AuthEntryPointJwt unauthorizedHandler;

    private final JwtService jwtService;


    public PubSecurityConfig(PubDetailsServiceImpl pubDetailsService, UserServiceDetailsImpl userServiceDetailsImpl, AuthEntryPointJwt unauthorizedHandler, JwtService jwtService) {
        this.pubDetailsServiceImpl = pubDetailsService;
        this.userServiceDetailsImpl = userServiceDetailsImpl;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtService = jwtService;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain filterChainAdmin(HttpSecurity http) throws Exception {
        http.securityMatcher("/pub/**", "/item/**")
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().authenticated())
                .authenticationProvider(pubAuthenticationProvider())
                .addFilterBefore(pubAuthenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors().and().csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider pubAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(pubDetailsServiceImpl);
        authProvider.setPasswordEncoder(pubPasswordEncoder());

        return authProvider;
    }

    public JwtAuthFilter pubAuthenticationJwtTokenFilter() {
        return new JwtAuthFilter(jwtService, userServiceDetailsImpl, pubDetailsServiceImpl);
    }

    @Bean
    public PasswordEncoder pubPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
