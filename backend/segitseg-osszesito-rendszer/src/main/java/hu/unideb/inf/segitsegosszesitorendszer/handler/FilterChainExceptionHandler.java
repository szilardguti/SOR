package hu.unideb.inf.segitsegosszesitorendszer.handler;

import hu.unideb.inf.segitsegosszesitorendszer.enums.ErrorCodes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private GlobalExceptionHandler resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Spring Security Filter Chain Exception: {}", e.getMessage());
            resolver.resolveException(request, response, e, ErrorCodes.NEW_JWT.ordinal());
        }
    }
}
