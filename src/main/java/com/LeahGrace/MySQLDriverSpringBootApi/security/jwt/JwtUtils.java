package com.LeahGrace.MySQLDriverSpringBootApi.security.jwt;


import com.LeahGrace.MySQLDriverSpringBootApi.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Date;

@Component
public class JwtUtils {
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);  //Provides additional information

    @Value("${LeahGrace.MySQLDriverSpringBootApi.jwtSecret}")
    private String jwtSecret;

//   @Value("${LeahGrace.MySQLDriverSpringBootApi.jwtExpirationMs}")
 //   private int jwtExpirationsMs;

    //VALIDATOR
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken); //parses claimbs object or Throws specific exceptions   //Jws - designates different/external server
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT Signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT  is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT Claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    //GENERATOR
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()  //Initializes the builder (created and returns) the following methods preform configuration changes to set properties
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
 //               .setExpiration(new Date((new Date()).getTime() + jwtExpirationsMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();    // runs the builder
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();  //gets the username of the user making the request
    }



}
