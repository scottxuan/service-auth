package com.service.auth.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.module.auth.dto.LoginDto;
import com.module.auth.dto.LoginResult;
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

import java.util.Map;

/**
 * @author pc
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final String USER_INFO = "userInfo";
    private static final String ROLES = "roles";
    private static final String PERMISSION = "permission";

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    @Autowired
    private JwtService jwtService;

    @Override
    public ResultBo<LoginResult> login(LoginDto dto) {
        ResultDto<SysUser> resultDto = sysUserFeignClient.findByAccount(dto.getAccount());
        if (!resultDto.isSuccess()) {
            return ResultBo.of(resultDto.getError(),resultDto.getArgs());
        }
        if (!resultDto.isPresent()) {
            return ResultBo.of(ErrorCodes.ACCOUNT_USERNAME_OR_PASSWORD_ERROR);
        }
        SysUser sysUser = resultDto.getData();
        if (sysUser.getIsLocked()) {
            return ResultBo.of(ErrorCodes.ACCOUNT_LOCKED);
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put(USER_INFO,sysUser);
        map.put(ROLES, Lists.newArrayList());
        map.put(PERMISSION,Lists.newArrayList());
        String accessToken = jwtService.createAccessToken(map);
        String refreshToken = jwtService.createRefreshToken(map);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserInfo(sysUser);
        loginResult.setAccessToken(accessToken);
        loginResult.setRefreshToken(refreshToken);
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
    public ResultBo<LoginResult> refreshToken(String refreshToken) {
        Claims claims;
        try {
            claims = jwtService.parseToken(refreshToken);
        } catch (ExpiredJwtException e) {
            return ResultBo.of(ErrorCodes.SYS_ERROR_401);
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put(USER_INFO,claims.get(USER_INFO));
        map.put(ROLES, claims.get(ROLES));
        map.put(PERMISSION,claims.get(PERMISSION));
        String accessTokenNew = jwtService.createAccessToken(map);
        String refreshTokenNew = jwtService.createRefreshToken(map);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserInfo(claims.get(USER_INFO));
        loginResult.setAccessToken(accessTokenNew);
        loginResult.setRefreshToken(refreshTokenNew);
        return ResultBo.of(loginResult);
    }
}
