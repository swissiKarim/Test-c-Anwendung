package com.backend.devp.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

public class TokenAuthenticationServices {

	private static final String SECRET = "ThisASecret";

    public static void addAuthentication(HttpServletResponse response, String username){

        Date EXPIRATION = Date.from(Instant.now().plus(10, ChronoUnit.SECONDS));

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(EXPIRATION)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();


        response.addHeader("Authorization", "Bearer " + token);
    }


    public static Authentication getAuthentication(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        if (token != null){
            String username = Jwts.parser()
                                .setSigningKey(SECRET)
                                    .parseClaimsJws(token.replace("Bearer ", ""))
                                        .getBody()
                                            .getSubject();

            if (username != null)
                return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

            return null;

        }

        return null;
    }
}
