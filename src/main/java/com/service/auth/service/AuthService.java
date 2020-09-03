package com.service.auth.service;


import com.module.auth.dto.LoginDto;
import com.module.auth.dto.LoginResult;
import com.module.auth.dto.TokenPair;
import com.scottxuan.base.result.ResultBo;

/**
 * @author pc
 */
public interface AuthService {
    /**
     * 系统用户账户密码登录
     * @param dto
     * @return
     */
    ResultBo<LoginResult> login(LoginDto dto);

    /**
     * 系统用户小程序邓丽
     * @param code
     * @return
     */
    ResultBo<LoginResult> miniLogin(String code);

    /**
     * 客户账户密码登录
     * @param dto
     * @return
     */
    ResultBo<LoginResult> customerLogin(LoginDto dto);

    /**
     * 客户小程序登录
     * @param code
     * @return
     */
    ResultBo<LoginResult> customerMiniLogin(String code);

    /**
     * token刷新
     * @param accessToken
     * @param refreshToken
     * @return
     */
    ResultBo<TokenPair> refreshToken(String accessToken,String refreshToken);

}
