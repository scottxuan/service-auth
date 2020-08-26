package com.service.auth.service;

import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author : pc
 * @date : 2020/8/26
 */
public interface JwtService {
    /**
     * 创建accessToken
     * @param map
     * @return
     */
    String createAccessToken(Map<String, Object> map);

    /**
     * 创建refreshToken
     * @param map
     * @return
     */
    String createRefreshToken(Map<String, Object> map);

    /**
     * token解析
     * @param token
     * @return
     */
    Claims parseToken(String token);
}
