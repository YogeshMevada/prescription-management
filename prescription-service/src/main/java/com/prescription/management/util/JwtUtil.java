package com.prescription.management.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
public class JwtUtil {

    private static final int TOKEN_EXPIRY_DURATION_IN_MIN = 180;
    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String JWT_TOKEN_ISSUER = "Prescription Pvt. Ltd.";
    private static final String JWT_TOKEN_SECRET = "secret";

    public <T> T extractClaims(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserName(final String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(final String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractClaims(final String token) {
        return Jwts.parser().setSigningKey(JWT_TOKEN_SECRET).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(final UserDetails userDetails) throws Exception {
        final Map<String, Object> claims = new HashMap<>();
        return generateToken(userDetails.getUsername(), claims);
    }

    public String generateToken(@NotBlank final String username, final Map<String, Object> claims) throws Exception {
        try {
            return AUTHORIZATION_TOKEN_PREFIX.concat(
                    Jwts.builder()
                            .setHeader(getJstHeader(SIGNATURE_ALGORITHM.getValue()))
                            .setId(UUID.randomUUID().toString())
                            .setSubject(username)
                            .setIssuedAt(new Date())
                            .setIssuer(JWT_TOKEN_ISSUER)
                            .setExpiration(getExpiryDate())
                            .addClaims(getValidatedClaims(claims))
                            .signWith(SIGNATURE_ALGORITHM, JWT_TOKEN_SECRET)
                            .compact());
        } catch (final Exception e) {
            throw new Exception("Unable to generate JWT token");
        }
    }

    private Map<String, Object> getJstHeader(final String algorithm) {
        final Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", algorithm);
        return headers;
    }

    private Date getExpiryDate() {
        return Date.from(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_DURATION_IN_MIN).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> getValidatedClaims(final Map<String, Object> claims) {
        if (Objects.isNull(claims)) {
            return new HashMap<>();
        }
        return claims;
    }
}
