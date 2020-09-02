package com.service.auth.service.impl;

import com.module.common.constants.JwtConstant;
import com.module.common.enums.UserSource;
import com.module.common.vo.Identity;
import com.module.system.client.SysUserFeignClient;
import com.module.system.entity.SysUser;
import com.scottxuan.base.result.ResultBo;
import com.scottxuan.core.utils.EntityUtils;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.CurrentService;
import com.service.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentServiceImpl implements CurrentService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    @Override
    public ResultBo<Identity> getCurrentUser(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return ResultBo.empty();
        }
        Claims claims;
        try{
            claims = jwtService.parseToken(accessToken);
        }catch (ExpiredJwtException e) {
            return ResultBo.empty();
        }
        Integer userId = (Integer)claims.get(JwtConstant.USER_ID);
        Integer userSource = (Integer)claims.get(JwtConstant.USER_SOURCE);
        Identity identity = null;
        if (UserSource.SYS.source == userSource){
            ResultDto<SysUser> dto = sysUserFeignClient.findById(userId);
            if (dto.isPresent()) {
                identity = new Identity();
                EntityUtils.copyPropertiesIgnoreNull(dto.get(),identity);
            }
        }
        return ResultBo.of(identity);
    }
}
