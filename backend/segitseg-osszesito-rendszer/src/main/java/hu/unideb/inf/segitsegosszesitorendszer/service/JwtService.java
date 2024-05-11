package hu.unideb.inf.segitsegosszesitorendszer.service;

import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.JwtNotFoundException;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.NewJwtRequiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    public String extractUsername(String token) throws NewJwtRequiredException {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws NewJwtRequiredException {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractRoleGroup(String token)
            throws NewJwtRequiredException
    { return extractClaim(token, claims -> claims.get("rg", String.class)); }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
            throws NewJwtRequiredException {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }
        catch (ExpiredJwtException jwtEx) {
            throw new NewJwtRequiredException();
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) throws NewJwtRequiredException {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws NewJwtRequiredException {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String GenerateToken(String username, Roles role){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, role);
    }

    private String createToken(Map<String, Object> claims, String username, Roles role) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 100))
                .claim("rg", role.name().toUpperCase())
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String token = null;
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        return Optional.ofNullable(token);
    }

    public String getUsernameFromRequest(HttpServletRequest request)
            throws JwtNotFoundException, NewJwtRequiredException {
        Optional<String> token = getTokenFromRequest(request);

        if (token.isEmpty())
            throw new JwtNotFoundException("A JWT token nem található!");

        return extractUsername(token.get());
    }
}
