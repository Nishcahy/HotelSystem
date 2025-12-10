package com.hotelbooking.auth.config;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class JwtConfig {
    
    // 1. Inject the raw Base64 strings (as String type)
    @Value("${jwt.key.public-key-base64}")
    private String publicKeyBase64; // Corrected field name and type

    @Value("${jwt.key.private-key-base64}")
    private String privateKeyBase64; // Corrected field name and type

    private final KeyFactory keyFactory;

    public JwtConfig() throws NoSuchAlgorithmException {
        this.keyFactory = KeyFactory.getInstance("RSA");
    }

    // 2. Define the RSAPublicKey bean
    @Bean
    public RSAPublicKey rsaPublicKey() throws InvalidKeySpecException {
        try {
            // Decoding the raw Base64 String
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64); 
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (Exception e) {
             throw new RuntimeException("Error initializing RSA Public Key from configuration.", e);
        }
    }

    // 3. Define the RSAPrivateKey bean
    @Bean
    public RSAPrivateKey rsaPrivateKey() throws InvalidKeySpecException {
        try {
            // Decoding the raw Base64 String
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64); 
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey) keyFactory.generatePrivate(spec);
        } catch (Exception e) {
             throw new RuntimeException("Error initializing RSA Private Key from configuration.", e);
        }
    }
    
    // 4. Use the decoded key beans for the JwtDecoder
    // Spring automatically injects the RSAPublicKey bean created above
    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) { 
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    // 5. Use the decoded key beans for the JwtEncoder
    // Spring automatically injects both key beans created above
    @Bean
    public JwtEncoder jwtEncoder(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        // Wrap the keys in a JWK for the encoder
        RSAKey jwk = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}