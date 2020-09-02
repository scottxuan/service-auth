package com.service.auth.api;

import com.module.auth.api.CurrentApi;
import com.module.common.constants.JwtConstant;
import com.module.common.vo.Identity;
import com.scottxuan.web.base.BaseController;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.CurrentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author scottxuan
 */
@Api(tags = "20000--当前用户")
@RestController
@RequestMapping(CurrentApi.MAPPING)
public class CurrentController extends BaseController implements CurrentApi {

    @Autowired
    private CurrentService currentService;

    @Override
    public ResultDto<Identity> getCurrentUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return getResultDto();
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String accessToken = request.getHeader(JwtConstant.ACCESS_TOKEN);
        return getResultDto(currentService.getCurrentUser(accessToken));
    }
}
