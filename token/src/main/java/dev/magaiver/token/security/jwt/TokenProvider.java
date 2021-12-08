package dev.magaiver.token.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.magaiver.token.property.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenProvider implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String AUTHORITIES = "authorities";

    private final JwtConfiguration jwtConfiguration;

    @SneakyThrows
    public String generateEncryptedToken(Authentication authentication) {
        SignedJWT signedJWT = createSignedJWT(authentication);
        log.info("Token Encrypted generated '{}'", signedJWT.serialize());
        return encryptToken(signedJWT);
    }

    @SneakyThrows
    public String generateSignedToken(Authentication authentication) {
        SignedJWT signedJWT = createSignedJWT(authentication);
        log.info("Token Signed generated '{}'", signedJWT.serialize());
        return signedJWT.serialize();
    }

    public boolean validateToken(String token) {
        log.info("validate token");
        return !isTokenExpired(token);
    }

    @SneakyThrows
    public void validateTokenSignature(String signedToken) {
        log.info("Starting method to validate token signature...");
        SignedJWT signedJWT = SignedJWT.parse(signedToken);
        log.info("Token Parsed! Retrieving public key from signed token");
        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
        log.info("Public key retrieved, validating signature. . . ");

        if (!signedJWT.verify(new RSASSAVerifier(publicKey)))
            throw new AccessDeniedException("Invalid token signature!");

        log.info("The token has a valid signature");
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, JWTClaimsSet::getSubject);
    }

    @SneakyThrows
    public Collection<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        getAllClaimsFromToken(token)
                .getStringListClaim(AUTHORITIES)
                .forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));
        return authorities;
    }

    @SneakyThrows
    public List<String> getStringListAuthorities(String token) {
        return getAllClaimsFromToken(token)
                .getStringListClaim(AUTHORITIES);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, JWTClaimsSet::getExpirationTime);
    }

    public <T> T getClaimFromToken(String token, Function<JWTClaimsSet, T> claimsResolver) {
        final JWTClaimsSet claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @SneakyThrows
    private JWTClaimsSet getAllClaimsFromToken(String token) {
        return SignedJWT.parse(token).getJWTClaimsSet();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private JWTClaimsSet generateClaimSet(Authentication authentication) {
        return new JWTClaimsSet.Builder()
                .subject(authentication.getName())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtConfiguration.getExpirationTimeMS() * 100000L))
                .claim(AUTHORITIES, getAuthorities(authentication))
                .build();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        log.info("Generate keyPairs ");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.genKeyPair();
    }

    private List<String> getAuthorities(Authentication userAuthentication) {
        return userAuthentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private SignedJWT createSignedJWT(Authentication authentication) throws NoSuchAlgorithmException, JOSEException {
        KeyPair rsaKeyPair = generateKeyPair();
        JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeyPair.getPublic()).keyID(UUID.randomUUID().toString()).build();
        log.info("Add key public on header request '{}' ", jwk);
        SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS512)
                .jwk(jwk)
                .type(JOSEObjectType.JWT)
                .build(), generateClaimSet(authentication));
        log.info("Signing the token with the private RSA Key");
        RSASSASigner signer = new RSASSASigner(rsaKeyPair.getPrivate());
        jwt.sign(signer);
        return jwt;
    }

    private String encryptToken(SignedJWT signedJWTToken) throws JOSEException {
        log.info("Starting the encryptToken method");
        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
                .contentType("JWT")
                .build(), new Payload(signedJWTToken));

        log.info("Encrypting token with system's private key");
        jweObject.encrypt(directEncrypter);
        log.info("Token encrypted  '{}' ", jweObject.serialize());
        return jweObject.serialize();
    }

    @SneakyThrows
    public String decryptToken(String encryptedToken) {
        log.info("Decrypting token and validating");
        JWEObject jweObject = JWEObject.parse(encryptedToken);
        DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
        jweObject.decrypt(directDecrypter);
        log.info("Token decrypted, returning signed token . . . ");
        return jweObject.getPayload().toSignedJWT().serialize();
    }

}
