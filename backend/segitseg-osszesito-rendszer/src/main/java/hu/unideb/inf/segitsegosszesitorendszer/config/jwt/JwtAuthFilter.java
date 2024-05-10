package hu.unideb.inf.segitsegosszesitorendszer.config.jwt;

import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.security.PubDetailsServiceImpl;
import hu.unideb.inf.segitsegosszesitorendszer.service.security.UserServiceDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceDetailsImpl userServiceDetails;
    private final PubDetailsServiceImpl pubDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String roleGroup = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
            roleGroup = jwtService.extractRoleGroup(token);
        }

        if(username != null
                && roleGroup != null
                && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails
                    = roleGroup.equals(Roles.PUB.name().toUpperCase())
                    ? pubDetailsService.loadUserByUsername(username)
                    : userServiceDetails.loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request, response);
    }
}
