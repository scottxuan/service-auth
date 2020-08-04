package com.service.auth.api;

import com.service.auth.utils.RsaUtils;
import com.module.common.error.ErrorCodes;
import com.module.common.wechat.condition.WxCode2Session;
import com.module.common.wechat.core.WxClient;
import com.module.common.wechat.request.WxCode2SessionRequest;
import com.module.common.wechat.response.WxCode2SessionResponse;
import com.scottxuan.web.base.BaseController;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author : scottxuan
 */
@Api(tags = "10000--认证管理")
@RequestMapping("${api}/auth")
@RestController
public class AuthController extends BaseController {

    @ApiOperation("001--微信小程序登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "id", required = true, example = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "微信登录code", required = true, dataType = "string", paramType = "query")})
    @GetMapping("/mini/login")
    public ResultDto miniLogin(@RequestParam String storeId, @RequestParam Integer id, @RequestParam String code) {
        String appId = "wxd7285bcf42c3836e";
        String secret = "28f0e6ca22962a20866127e134e5f835";
        WxCode2Session wxCode2Session = new WxCode2Session(appId, secret, code);
        WxCode2SessionResponse response = WxClient.request(new WxCode2SessionRequest(wxCode2Session));
        if (!response.isSuccess()) {
            return getFailedDto(ErrorCodes.WE_CHAT_APPLET_LOGIN_ERROR);
        }
        String openId = response.getOpenId();
//        ResultDto resultDto = cusUserFeignClient.selectByParams(storeId, openId);
//        if (!resultDto.isSuccess()) {
//            return resultDto;
//        }
//        if (!resultDto.isPresent()) {
//            CusUserDto dto = new CusUserDto();
//            dto.setStoreId(storeId);
//            dto.setOpenId(openId);
//            ResultDto add = cusUserFeignClient.save(dto);
//            if (!add.isSuccess()) {
//                return getResultDto(add);
//            }
//        }
        return getResultDto(openId);
    }

    @Autowired
    private AuthService authService;

    @ApiOperation("002--系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账户", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query")})
    @GetMapping("/login")
    public ResultDto login(@RequestParam String account, @RequestParam String password) {
        return getResultDto();
    }

        /**
         *  测试使用
         * @param args
         */
    public static void main(String[] args) throws Exception {
        // 调用generateKey方法，输入公私钥的地址，生成秘钥的明文（原料）。即可在指定的地方生成秘钥
        RsaUtils.generateKey("E:\\service-auth\\src\\main\\resources\\key.pub","E:\\service-auth\\src\\main\\resources\\key.pri","scottxuan");

        // 这是输入地址获取公有秘钥的，还有一个是输入字节对象获取的。下面获取私有秘钥同理
        PublicKey publicKey = RsaUtils.getPublicKey("E:\\service-auth\\src\\main\\resources\\key.pub");
        System.out.println("这是公有的秘钥："+publicKey);

        PrivateKey privateKey = RsaUtils.getPrivateKey("E:\\service-auth\\src\\main\\resources\\key.pri");
        System.out.println("这是私有的秘钥："+privateKey);


        System.out.println("ok");
    }
}
