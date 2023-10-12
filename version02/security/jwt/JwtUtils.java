package com.DisplayMakers.version02.security.jwt;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import com.DisplayMakers.version02.security.services.UserDetailsImpl;

//JWT's are means of exhanging info a payload containing info for authorization and a signature that verifies its integrity
@Component
//this class is used for creating and using them
public class JwtUtils {
    //creates instance to log messages
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  //secret key for jwt verification
  @Value("${DisplayMakers.app.jwtSecret}")
  private String jwtSecret;

  //expiration of jwt
  @Value("${DisplayMakers.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  //generates  jwt from auth object 
  public String generateJwtToken(Authentication authentication) {
    //gets details from current principal
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    //sets time issues at and expiration  + gives signaature
    return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }
  
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  //veryifying token using secret key, 
  public boolean validateJwtToken(String authToken) {
    try {
      //attempts to parse and verify, with key
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
