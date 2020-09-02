package com.service.auth.service;

import com.module.common.vo.Identity;
import com.scottxuan.base.result.ResultBo;

/**
 * @author pc
 */
public interface CurrentService {

    /**
     * 获取当前登录用户
     * @param accessToken
     * @return
     */
    ResultBo<Identity> getCurrentUser(String accessToken);
}
