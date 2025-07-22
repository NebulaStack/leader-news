package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dto.LoginDto;
import com.heima.model.user.pojos.ApUser;

public interface ApUserService extends IService<ApUser> {
    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    ResponseResult login(LoginDto loginDto);
}
