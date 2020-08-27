package com.service.auth.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.module.auth.dto.LoginDto;
import com.module.auth.dto.LoginResult;
import com.module.auth.dto.TokenPair;
import com.module.auth.enums.SystemType;
import com.module.common.constants.JwtConstant;
import com.module.common.error.ErrorCodes;
import com.module.system.client.SysUserFeignClient;
import com.module.system.entity.SysUser;
import com.scottxuan.base.result.ResultBo;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.AuthService;
import com.service.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final long ACCESS_TOKEN_OUT_TIME = 2 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_OUT_TIME = 2 * 24 * 60 * 60 * 1000L;

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    @Autowired
    private JwtService jwtService;

    @Override
    public ResultBo<LoginResult> login(LoginDto dto) {
        ResultDto<SysUser> resultDto = sysUserFeignClient.findByAccount(dto.getAccount());
        if (!resultDto.isSuccess()) {
            return ResultBo.of(resultDto.getError(), resultDto.getArgs());
        }
        if (!resultDto.isPresent()) {
            return ResultBo.of(ErrorCodes.ACCOUNT_USERNAME_OR_PASSWORD_ERROR);
        }
        SysUser sysUser = resultDto.getData();
        if (sysUser.getIsLocked()) {
            return ResultBo.of(ErrorCodes.ACCOUNT_LOCKED);
        }
        List<String> roles = Lists.newArrayList();
        List<String> permissions = Lists.newArrayList();
        long startMillis = System.currentTimeMillis();
        Date accessTokenExpireDate = new Date(startMillis + ACCESS_TOKEN_OUT_TIME);
        String accessTokenNew = jwtService.createToken(sysUser,roles,permissions,accessTokenExpireDate);
        Date refreshTokenExpireDate = new Date(startMillis + REFRESH_TOKEN_OUT_TIME);
        String refreshTokenNew = jwtService.createToken(sysUser,roles,permissions,refreshTokenExpireDate);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserInfo(sysUser);
        loginResult.setAccessToken(accessTokenNew);
        loginResult.setAccessTokenExpireDate(accessTokenExpireDate);
        loginResult.setRefreshToken(refreshTokenNew);
        return ResultBo.of(loginResult);
    }

    @Override
    public ResultBo<LoginResult> miniLogin(String code) {
        return null;
    }

    @Override
    public ResultBo<LoginResult> customerLogin(LoginDto dto) {
        return null;
    }

    @Override
    public ResultBo<LoginResult> customerMiniLogin(String code) {
        return null;
    }

    @Override
    public ResultBo<Boolean> checkAccessToken(String accessToken) {
        try {
            jwtService.parseToken(accessToken);
            return ResultBo.of(Boolean.TRUE);
        } catch (ExpiredJwtException e) {
            return ResultBo.of(ErrorCodes.ACCESS_TOKEN_TIME_OUT);
        }
    }

    @Override
    public ResultBo<TokenPair> refreshToken(TokenPair tokenPair) {
        Claims claims;
        try {
            jwtService.parseToken(tokenPair.getAccessToken());
        } catch (ExpiredJwtException e1) {
            try {
                claims = jwtService.parseToken(tokenPair.getRefreshToken());
            } catch (ExpiredJwtException e2) {
                return ResultBo.of(ErrorCodes.SYS_ERROR_401);
            }
            Object userInfo = claims.get(JwtConstant.USER_INFO);
            List<String> roles = (List<String>)claims.get(JwtConstant.ROLES);
            List<String> permissions = (List<String>)claims.get(JwtConstant.PERMISSIONS);
            long startMillis = System.currentTimeMillis();
            Date accessTokenExpireDate = new Date(startMillis + ACCESS_TOKEN_OUT_TIME);
            String accessTokenNew = jwtService.createToken(userInfo,roles,permissions,accessTokenExpireDate);
            Date refreshTokenExpireDate = new Date(startMillis + REFRESH_TOKEN_OUT_TIME);
            String refreshTokenNew = jwtService.createToken(userInfo,roles,permissions,refreshTokenExpireDate);
            return ResultBo.of(new TokenPair(accessTokenNew,refreshTokenNew));
        }
        return ResultBo.of(tokenPair);
    }
}
