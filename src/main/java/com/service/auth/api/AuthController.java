package com.service.auth.api;

import com.module.auth.api.AuthApi;
import com.module.auth.dto.LoginDto;
import com.module.common.error.ErrorCodes;
import com.module.common.wechat.condition.WxCode2Session;
import com.module.common.wechat.core.WxClient;
import com.module.common.wechat.request.WxCode2SessionRequest;
import com.module.common.wechat.response.WxCode2SessionResponse;
import com.scottxuan.web.base.BaseController;
import com.scottxuan.web.result.ResultDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author : scottxuan
 */
@Api(tags = "10000--认证管理")
@RequestMapping("${api}/auth")
@RestController
public class AuthController extends BaseController implements AuthApi {

    @Override
    public ResultDto<LoginDto> login(String account, String password) {
        return getResultDto();
    }

    @Override
    public ResultDto<LoginDto> miniLogin(String code) {
        String appId = "wxd7285bcf42c3836e";
        String secret = "28f0e6ca22962a20866127e134e5f835";
        WxCode2Session wxCode2Session = new WxCode2Session(appId, secret, code);
        WxCode2SessionResponse response = WxClient.request(new WxCode2SessionRequest(wxCode2Session));
        if (!response.isSuccess()) {
            return getFailedDto(ErrorCodes.WE_CHAT_APPLET_LOGIN_ERROR);
        }
        String openId = response.getOpenId();
        return getResultDto(openId);
    }

    @Override
    public ResultDto<LoginDto> customerLogin(String account, String password) {
        return getResultDto();
    }

    @Override
    public ResultDto<LoginDto> customerMiniLogin(String code) {
        return getResultDto();
    }
}
