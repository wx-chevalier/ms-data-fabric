package com.zhuxun.dmc.apim.security.jwt;

import com.zhuxun.dmc.apim.config.properties.ApplicationProperties;
import com.zhuxun.dmc.apim.config.properties.SecurityProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String USERNAME_KEY = "username";

  private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
  private final ApplicationProperties applicationProperties;

  private String secretKey;

  private long tokenValidityInMilliseconds;

  private long tokenValidityInMillisecondsForRememberMe;

  @Autowired
  public TokenProvider(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @PostConstruct
  void init() {
    SecurityProperties.Authentication.Jwt jwt =
        this.applicationProperties.getSecurity().getAuthentication().getJwt();

    this.secretKey = jwt.getSecret();
    this.tokenValidityInMilliseconds = jwt.getTokenValidityInSeconds() * 1000;
    this.tokenValidityInMillisecondsForRememberMe =
        jwt.getTokenValidityInSecondsForRememberMe() * 1000;
  }

  public String createToken(Authentication authentication, Boolean rememberMe) {
    if (!(authentication instanceof JWTAuthenticationToken)) {
      return null;
    }
    JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) authentication;
    rememberMe = rememberMe == null ? false : rememberMe;

    String authorities = jwtAuthenticationToken.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity;
    if (rememberMe) {
      validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
    } else {
      validity = new Date(now + this.tokenValidityInMilliseconds);
    }

    return Jwts.builder()
        .setSubject(jwtAuthenticationToken.getUserId())
        .claim(AUTHORITIES_KEY, authorities)
        .claim(USERNAME_KEY, jwtAuthenticationToken.getUsername())
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .setExpiration(validity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    return new JWTAuthenticationToken(
        claims.getSubject(),
        claims.get(USERNAME_KEY, String.class),
        authorities);
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      log.info("Invalid JWT signature.");
      log.trace("Invalid JWT signature trace: {}", e);
    } catch (MalformedJwtException e) {
      log.info("Invalid JWT token.");
      log.trace("Invalid JWT token trace: {}", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
      log.trace("Expired JWT token trace: {}", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
      log.trace("Unsupported JWT token trace: {}", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
      log.trace("JWT token compact of handler are invalid trace: {}", e);
    }
    return false;
  }
}
