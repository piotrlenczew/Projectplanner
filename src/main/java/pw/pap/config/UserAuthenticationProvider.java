package pw.pap.config;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pw.pap.model.User;
import pw.pap.service.UserService;

import java.util.*;

//@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final UserService userService;

    public UserAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());

        return Jwts.builder()
                .setSubject(user.getName())
                .setIssuedAt(now)
                .setExpiration(validity)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(username, null, null);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            // Handle exceptions or return null
            return null;
        }
    }

    public Authentication validateTokenStrongly(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            String username = claims.getSubject();

            // Additional checks if needed
            // For example, check if the user is still valid in the system (e.g., not disabled)

            return new UsernamePasswordAuthenticationToken(username, null, null);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            // Handle exceptions or return null
            return null;
        }
    }

}