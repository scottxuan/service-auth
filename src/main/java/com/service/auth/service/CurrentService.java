package com.service.auth.service;

import com.module.common.vo.Identity;
import com.scottxuan.base.result.ResultBo;

public interface CurrentService {

    ResultBo<Identity> getCurrentUserId(String accessToken);
}
