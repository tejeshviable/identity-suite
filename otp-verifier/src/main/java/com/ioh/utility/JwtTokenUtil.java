package com.ioh.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private static final String PUBLIC_KEY_PEM_FILE_NAME = "publicKey";
    private static final String PRIVATE_KEY_PEM_FILE_NAME = "privateKey";
    private static PrivateKey privateKey;

    public static String publicKeyContent;

    public static String privateKeyContent;

    public static PublicKey publicKey;
    @Value("${jwt.user.token.validity.sec: 259200}") // Defaults to 3 days
    public long JWT_USER_TOKEN_VALIDITY_IN_SEC;

    @Value("${application.id}")
    private String apllicationId;



    static {
        try {
            loadKeys();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            log.error("Exception occured:", e);
            System.exit(0);
        }
    }

    /**
     * loads private key and public key
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    private static void loadKeys() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.info("loadKeys :: initializing private key and public key loading");
        publicKeyContent = loadData(PUBLIC_KEY_PEM_FILE_NAME);
        privateKeyContent = loadData(PRIVATE_KEY_PEM_FILE_NAME);
        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        privateKeyContent = privateKeyContent.replaceAll(" ", ""); // Remove any white-spaces.
        privateKeyContent = privateKeyContent.replaceAll("(\\r\\n|\\n|\\r)", ""); // Remove any "\r\n".

        String publicKeyContentTrimmed = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        publicKeyContentTrimmed = publicKeyContentTrimmed.replaceAll(" ", ""); // Remove any white-spaces.
        publicKeyContentTrimmed = publicKeyContentTrimmed.replaceAll("(\\r\\n|\\n|\\r)", ""); // Remove any "\r\n".

        KeyFactory kf = KeyFactory.getInstance("RSA");

        try {
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(publicKeyContentTrimmed));
            publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
            log.info("public Key Loaded Successfully");

            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
            privateKey = kf.generatePrivate(keySpecPKCS8);
            log.info("private Key Loaded Successfully");

        } catch (Exception exp) {
            log.error("Exception occured:", exp);
        }
    }

    public static String loadData(String fileName) {
        log.info("loadData :: loading data from {}", fileName);
        String content = null;
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            StringBuilder contentBuilder = new StringBuilder();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                contentBuilder.append(line + System.lineSeparator());
            }
            content = contentBuilder.toString();
            log.info("{} file loaded successfully", fileName);
        } catch (Exception e) {
            log.error("Exception occured while reading file: {} Error Msg : ", fileName, e.getMessage());
        }
        return content;
    }



    /**
     * gets claims from token
     *
     * @param token          contains jwt token
     * @param claimsResolver contains higher order function
     * @param <T>            generic type
     * @return generic type
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        log.info("getClaimFromToken :: gets claims from token");
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * retrieving all claims from token
     *
     * @param token contains jwt token
     * @return Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        log.info("getAllClaimsFromToken :: retrieving all claims from token");
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Access code has been expired");
        }
        return claims;
    }


    public String generateToken(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("application_id", apllicationId);
        String jti = UUID.randomUUID().toString();
        claims.put("jti",jti);
        claims.put("acl","");
        return doGenerateToken(claims,"");
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return doGenerateToken(claims,subject,JWT_USER_TOKEN_VALIDITY_IN_SEC * 1000);
    }


    private String doGenerateToken(Map<String, Object> claims, String subject, long duration) {
        if (privateKey == null) {
            try {
                loadKeys();
            } catch (Exception e) {
                log.warn("Private key is null/not able to load");
                log.error("Exception occured:", e);

            }
        }
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(SignatureAlgorithm.RS256, privateKey).compact();

    }

}
