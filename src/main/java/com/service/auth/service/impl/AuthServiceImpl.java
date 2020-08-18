package com.service.auth.service.impl;

import com.module.auth.dto.LoginDto;
import com.module.auth.dto.LoginResult;
import com.module.common.error.ErrorCodes;
import com.module.system.client.SysUserFeignClient;
import com.module.system.entity.SysUser;
import com.scottxuan.base.result.ResultBo;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pc
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

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
        LoginResult loginResult = new LoginResult();
        loginResult.setUserInfo(sysUser);
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
}
