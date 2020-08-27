package com.service.auth.service.impl;

import com.module.auth.enums.SystemType;
import com.module.common.constants.JwtConstant;
import com.service.auth.service.JwtService;
import com.service.auth.utils.RSAUtils;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : pc
 * @date : 2020/8/26
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    private static final AtomicBoolean PRIVATE_KEY_IS_LOAD = new AtomicBoolean(true);
    private static final AtomicBoolean PUBLIC_KEY_IS_LOAD = new AtomicBoolean(true);

    @Override
    public String createToken(Object userInfo, List<String> roles, List<String> permissions, Date expireDate) {
        try {
            if(PRIVATE_KEY_IS_LOAD.compareAndSet(true,false) || privateKey == null){
                privateKey = getPrivateKey();
            }
            JwtBuilder builder = Jwts.builder()
                    .setHeaderParam("alg", "RS256")
                    .setHeaderParam("typ", "JWT");
            builder.claim(JwtConstant.USER_INFO, userInfo);
            builder.claim(JwtConstant.ROLES, roles);
            builder.claim(JwtConstant.PERMISSIONS, permissions);
            builder.signWith(SignatureAlgorithm.RS256, privateKey)
                    .setExpiration(expireDate);
            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Claims parseToken(String token) {
        if(PUBLIC_KEY_IS_LOAD.compareAndSet(true,false) || publicKey == null){
            publicKey = getPublicKey();
        }
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public PrivateKey getPrivateKey() {
        BufferedReader bufferedReader = null;
        PrivateKey privateKey = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.getClass().getResource("/privateKey.key").getPath()));
            StringBuilder privateKeyBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
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
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return privateKey;
    }

    public PublicKey getPublicKey() {
        BufferedReader bufferedReader = null;
        PublicKey publicKey = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.getClass().getResource("/publicKey.key").getPath()));
            StringBuilder privateKeyBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
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
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return publicKey;
    }
}
