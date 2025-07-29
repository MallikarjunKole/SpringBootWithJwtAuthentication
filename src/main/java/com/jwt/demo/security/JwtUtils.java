package com.jwt.demo.security;

import com.jwt.demo.registration.controller.AuthController;
import com.jwt.demo.registration.serviceImpl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

  @Value("${jwt.tokenExpireInMinutes}")
  public Duration jwt_token_expire_validity;

  @Value("${jwt.secret}")
  private String secret;

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  public String generateJwtToken(UserDetailsImpl userPrincipal) {
    Map<String, Object> claims = new HashMap<>();
    return generateTokenFromUsername(claims, userPrincipal.getUsername());

  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public String generateTokenFromUsername(Map<String, Object> claims, String subject) {
    String token = null;
    try {
      token = Jwts.builder()
              .setClaims(claims)
              .setSubject(subject)
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwt_token_expire_validity.toMillis()))
              .signWith(key(), SignatureAlgorithm.HS256)
              .compact();
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
      return token;
  }


  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public Boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

}

