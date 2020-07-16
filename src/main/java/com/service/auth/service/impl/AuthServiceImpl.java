package com.service.auth.service.impl;

import com.client.base.AreaFeignClient;
import com.module.base.entity.Area;
import com.scottxuan.base.result.ResultBo;
import com.scottxuan.web.result.ResultDto;
import com.service.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AreaFeignClient areaFeignClient;

    @Override
    public ResultBo<Area> test() {
        ResultDto<Area> resultDto = areaFeignClient.findByCode("370000");
        if (!resultDto.isSuccess()) {
            return ResultBo.of(resultDto.getError());
        }
        if (!resultDto.isPresent()) {
            return ResultBo.empty();
        }
        return ResultBo.of(resultDto.getData());
    }
}
