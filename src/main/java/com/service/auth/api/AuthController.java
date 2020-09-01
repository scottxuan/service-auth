package com.service.auth.api;

import com.module.auth.api.AuthApi;
import com.module.auth.dto.LoginDto;
import com.module.auth.dto.LoginResult;
import com.module.auth.dto.TokenPair;
import com.module.common.error.ErrorCodes;
import com.module.common.wechat.condition.WxCode2Session;
import com.module.common.wechat.core.WxClient;
import com.module.common.wechat.request.WxCode2SessionRequest;
import com.module.common.wechat.response.WxCode2SessionResponse;
import com.module.system.entity.SysUser;
import com.scottxuan.base.pair.Pair;
import com.scottxuan.web.base.BaseController;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.AuthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author : scottxuan
 */
@Api(tags = "10000--认证管理")
@RestController
@RequestMapping(AuthApi.MAPPING)
public class AuthController extends BaseController implements AuthApi {

    @Autowired
    private AuthService authService;

    @Override
    public ResultDto<LoginResult> login(LoginDto dto) {
        return getResultDto(authService.login(dto));
    }

    @Override
    public ResultDto<LoginResult> miniLogin(String code) {
        String appId = "wxd7285bcf42c3836e";
        String secret = "28f0e6ca22962a20866127e134e5f835";
        WxCode2Session wxCode2Session = new WxCode2Session(appId, secret, code);
        WxCode2SessionResponse response = WxClient.request(new WxCode2SessionRequest(wxCode2Session));
        if (!response.isSuccess()) {
            return getFailedDto(ErrorCodes.WE_CHAT_APPLET_LOGIN_ERROR);
        }
        String openId = response.getOpenId();
        return getResultDto(authService.miniLogin(code));
    }

    @Override
    public ResultDto<LoginResult> customerLogin(LoginDto dto) {
        return getResultDto(authService.customerLogin(dto));
    }

    @Override
    public ResultDto<LoginResult> customerMiniLogin(String code) {
        return getResultDto(authService.customerMiniLogin(code));
    }

    @Override
    public ResultDto<TokenPair> refreshToken(String accessToken,String refreshToken) {
        return getResultDto(authService.refreshToken(accessToken, refreshToken));
    }

    @Override
    public ResultDto<Pair<Integer,Integer>> currentUserId(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        return getResultDto(authService.getCurrentUserIdAndSource(accessToken));
    }
}
