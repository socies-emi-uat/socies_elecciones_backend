package com.socies.voto.services;

import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

  private String secretkey = "4a436dd4a40284253c8686459bf654c9a7d7781847d112b0b0e14ded33fa10dd";

  public JWTService() {}

  public String generateToken(Long id, String username) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
        .claims()
        .add(claims)
        .add("id", id)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
        .and()
        .signWith(getKey())
        .compact();
  }

  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretkey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUserName(String token) {
    // extract the username from jwt token
    return extractClaim(token, Claims::getSubject);
  }

  public Long extractUserId(String token) {
    // Extraer el "id" del usuario desde el JWT
    return extractClaim(token, claims -> claims.get("id", Long.class));
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
  }

  public boolean validateToken(String token, UsuarioPrincipalDTO userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getCorreo()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
