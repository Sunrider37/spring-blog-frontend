package com.sunrider.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.Jwts.parserBuilder;

@Service
public class JwtTokenProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
       keyStore = KeyStore.getInstance("JKS");
       InputStream inputStream = getClass().getResourceAsStream("/springblog.jks");
       keyStore.load(inputStream, "secret".toCharArray());
    }

    public String generateToken(Authentication authentication) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
      User principal = (User) authentication.getPrincipal();
      return Jwts.builder().setSubject(principal.getUsername())
              .signWith(getPrivateKey()).compact();
    }

    private PrivateKey getPrivateKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
       return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());

    }

    public boolean validateToken(String token) throws KeyStoreException {
        Jwts.parserBuilder().setSigningKey(getPublicKey()).requireSubject(token);
        return true;
    }

    private PublicKey getPublicKey() throws KeyStoreException {
       return keyStore.getCertificate("springblog").getPublicKey();

    }

    public String getUsernameFromJwt(String token) throws KeyStoreException {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
