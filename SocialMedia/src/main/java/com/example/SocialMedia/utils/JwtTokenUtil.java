package com.example.SocialMedia.utils;

import com.example.SocialMedia.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    @Autowired
    private UserDetailsService userDetailsService;
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    //used to verify a given JWT. It returns true if the JWT is verified, or false otherwise.
    public boolean validateAccessToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Signature validation failed", ex.getMessage());
        }
        return false;
    }

    public String getSubject(String token){
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(User user){
        return Jwts.builder().setSubject(String.format("%s,%s", user.getUsername(), user.getPhone()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public User getUserDetails(String token){
        User userDetails = new User();
        String[] jwtSubject = getSubject(token).split(",");
        userDetails = (User) userDetailsService.loadUserByUsername(jwtSubject[1]);
        return userDetails;
    }

    public static String getAccessToken(HttpServletRequest request){
        String token = null;
        try{
            String header = request.getHeader("Authorization");
            token = header.split(" ")[1].trim();
        } catch (ArrayIndexOutOfBoundsException exception){
            log.error("Bearer is null", exception.getMessage());
        }

        return token;
    }
}
