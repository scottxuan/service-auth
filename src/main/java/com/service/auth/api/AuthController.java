package com.service.auth.api;

import com.client.base.AreaFeignClient;
import com.module.base.entity.Area;
import com.module.common.wechat.condition.WxCode2Session;
import com.module.common.wechat.core.WxClient;
import com.module.common.wechat.request.WxCode2SessionRequest;
import com.module.common.wechat.response.WxCode2SessionResponse;
import com.scottxuan.base.result.ResultBo;
import com.scottxuan.web.base.BaseController;
import com.scottxuan.web.result.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : scottxuan
 */
@Api(tags = "10000--认证管理")
@RequestMapping("${api}/auth")
@RestController
public class AuthController extends BaseController {

    @ApiOperation("001--用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "id", required = true, example = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "微信登录code", required = true, dataType = "string", paramType = "query")})
    @GetMapping
    public ResultDto login(@RequestParam String storeId, @RequestParam Integer id, @RequestParam String code) {
//        String appId = "wxd7285bcf42c3836e";
//        String secret = "28f0e6ca22962a20866127e134e5f835";
//        WxCode2Session wxCode2Session = new WxCode2Session(appId, secret, code);
//        WxCode2SessionResponse response = WxClient.request(new WxCode2SessionRequest(wxCode2Session));
//        String openId = response.getOpenId();
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
//        return getResultDto(resultDto);
        return getResultDto();
    }

    @Autowired
    private AreaFeignClient areaFeignClient;

    @ApiOperation("002--测试用例")
    @GetMapping("/test")
    public ResultDto test() {
        ResultDto<Area> resultDto = areaFeignClient.findByCode("370000");
        if (!resultDto.isSuccess()) {
        }
        return getResultDto();
    }
}
