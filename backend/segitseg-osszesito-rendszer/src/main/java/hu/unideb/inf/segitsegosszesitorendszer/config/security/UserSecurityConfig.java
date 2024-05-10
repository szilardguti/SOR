package hu.unideb.inf.segitsegosszesitorendszer.config.security;

import hu.unideb.inf.segitsegosszesitorendszer.config.jwt.AuthEntryPointJwt;
import hu.unideb.inf.segitsegosszesitorendszer.config.jwt.JwtAuthFilter;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.security.PubDetailsServiceImpl;
import hu.unideb.inf.segitsegosszesitorendszer.service.security.UserServiceDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig {

    private final JwtService jwtService;

    private final UserServiceDetailsImpl userServiceDetailsImpl;
    private final PubDetailsServiceImpl pubDetailsServiceImpl;


    private final AuthEntryPointJwt unauthorizedHandler;

    public UserSecurityConfig(JwtService jwtService, UserServiceDetailsImpl userServiceDetailsImpl, PubDetailsServiceImpl pubDetailsServiceImpl, AuthEntryPointJwt unauthorizedHandler) {
        this.jwtService = jwtService;
        this.userServiceDetailsImpl = userServiceDetailsImpl;
        this.pubDetailsServiceImpl = pubDetailsServiceImpl;
        this.unauthorizedHandler = unauthorizedHandler;
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChainUser(HttpSecurity http) throws Exception {
        http.securityMatcher("/user/**")
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().authenticated())
                .authenticationProvider(userAuthenticationProvider())
                .addFilterBefore(userAuthenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userServiceDetailsImpl);
        authProvider.setPasswordEncoder(userPasswordEncoder());

        return authProvider;
    }

    public JwtAuthFilter userAuthenticationJwtTokenFilter() {
        return new JwtAuthFilter(jwtService, userServiceDetailsImpl, pubDetailsServiceImpl);
    }

    @Bean
    @Primary
    public PasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
