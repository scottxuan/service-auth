package com.service.auth.service;

import com.module.auth.enums.SystemType;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.List;

/**
 * @author : pc
 * @date : 2020/8/26
 */
public interface JwtService {
    /**
     * 创建Token
     * @param userInfo
     * @param roles
     * @param permissions
     * @param expireDate
     * @return
     */
    String createToken(Object userInfo, List<String> roles, List<String> permissions, Date expireDate);

    /**
     * token解析
     * @param token
     * @return
     */
    Claims parseToken(String token);
}
