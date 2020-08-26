package com.service.auth.service.impl;

import com.service.auth.service.JwtService;
import com.service.auth.utils.RSAUtils;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @author : pc
 * @date : 2020/8/26
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static final long ACCESS_TOKEN_OUT_TIME = 2 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_OUT_TIME = 2 * 24 * 60 * 60 * 1000L;
    private static final PrivateKey PRIVATE_KEY;
    private static final PublicKey PUBLIC_KEY;

    static {
        PRIVATE_KEY = getPrivateKey();
        PUBLIC_KEY = getPublicKey();
    }

    @Override
    public String createAccessToken(Map<String, Object> map) {
        try {
            long millis = System.currentTimeMillis();
            JwtBuilder builder = Jwts.builder()
                    .setHeaderParam("alg", "RS256")
                    .setHeaderParam("typ", "JWT");
            for (String key : map.keySet()) {
                builder.claim(key, map.get(key));
            }
            builder.signWith(SignatureAlgorithm.RS256, PRIVATE_KEY)
                    .setExpiration(new Date(millis + ACCESS_TOKEN_OUT_TIME));
            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String createRefreshToken(Map<String, Object> map) {
        try {
            long millis = System.currentTimeMillis();
            JwtBuilder builder = Jwts.builder()
                    .setHeaderParam("alg", "RS256")
                    .setHeaderParam("typ", "JWT");
            for (String key : map.keySet()) {
                builder.claim(key, map.get(key));
            }
            builder.signWith(SignatureAlgorithm.RS256, PRIVATE_KEY)
                    .setExpiration(new Date(millis + REFRESH_TOKEN_OUT_TIME));
            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Claims parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(PUBLIC_KEY)
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public static PrivateKey getPrivateKey() {
        FileReader reader = null;
        PrivateKey privateKey = null;
        try {
            File file = new File(JwtServiceImpl.class.getResource("/privateKey.key").getPath());
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder privateKeyBuilder = new StringBuilder();
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                privateKeyBuilder.append(line);
            }
            String privateKeyString = privateKeyBuilder.toString().trim();
            privateKeyString = privateKeyString.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "")
                    .replaceAll("\r\n", "");
            privateKey = RSAUtils.getPrivateKey(privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return privateKey;
    }

    public static PublicKey getPublicKey() {
        FileReader reader = null;
        PublicKey publicKey = null;
        try {
            File file = new File(JwtServiceImpl.class.getResource("/publicKey.key").getPath());
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder privateKeyBuilder = new StringBuilder();
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                privateKeyBuilder.append(line);
            }
            String publicKeyString = privateKeyBuilder.toString().trim();
            publicKeyString = publicKeyString.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "")
                    .replaceAll("\r\n", "");
            publicKey = RSAUtils.getPublicKey(publicKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return publicKey;
    }
}
