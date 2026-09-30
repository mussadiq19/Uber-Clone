package com.example.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    private Key getSignKey(){
        return  Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    //This method creates a brand new token for us based on a payload
    public String createToken(Map<String, Object>payload,String email){
        Date now = new Date();
        Date expiryDate= new Date(now.getTime()+expiry*1000L);
        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate).subject(email)
                .signWith(getSignKey())
                .compact();
    }

    public String createToken(String email){
        System.out.println(createToken(new HashMap<>(),email));
        return createToken(new HashMap<>(),email);

    }

    public Claims extractAllPayloads(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey()) // Use verifyWith instead of setSigningKey
                .build()
                .parseSignedClaims(token) // Use parseSignedClaims for JWS
                .getPayload(); // Get payload instead of getBody()
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllPayloads(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token ){
        return extractClaim(token,Claims::getExpiration);

    }
    public Boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());
    }

    public String extractEmail(String token){

        return extractClaim(token,Claims::getSubject);
    }

    public Boolean validateToken(String token , String email){
        final  String userEmailFetchedFromToken=extractEmail(token);
        return (userEmailFetchedFromToken.equals(email))&& !isTokenExpired(token);
    }
}
