package com.feri.sipv.sipvserver.utils;

import com.feri.sipv.sipvserver.models.Session;
import com.feri.sipv.sipvserver.repositories.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 168 * 60 * 60 * 1000;

    @Autowired
    private SessionRepository sessionRepository;


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        List<Session> allSessions = sessionRepository.findAll();
        if (allSessions.size()!=0){
            for (Session sessionFromList : allSessions) {
                if (sessionFromList.getSessionToken().equals(token)){
                    String secret = sessionFromList.getSessionSecret();
                    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                }
            }
        }
        return Jwts.parser().setSigningKey("").parseClaimsJws("").getBody();
    }

    public String[] generateToken(UserDetails userDetails) {
        int secretLenght = 64;
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghigklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        while (secretLenght-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        String secret = builder.toString();
        Map<String, Object> claims = new HashMap<>();
        String[] info = new String[2];
        info[0] = Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        info[1] = secret;
        return info;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
