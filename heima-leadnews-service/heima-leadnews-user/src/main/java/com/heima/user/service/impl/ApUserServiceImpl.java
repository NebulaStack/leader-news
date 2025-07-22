package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dto.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.vo.LoginVo;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    @Override
    public ResponseResult login(LoginDto loginDto) {
        // 1.正常登录
        // 1.1 判断手机号和密码是否为空
        if (StringUtils.isNotBlank(loginDto.getPhone()) &&
                StringUtils.isNotBlank(loginDto.getPassword())){
            // 1.2 通过手机号查询用户信息
            ApUser dbUser = getOne(new LambdaQueryWrapper<ApUser>().eq(ApUser::getPhone, loginDto.getPhone()));
            // 1.3 用户不存在
            if (dbUser == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
            }
            // 1.4 用户存在，将用户输入的密码与数据库中的密码进行比较
            String salt = dbUser.getSalt();
            String dbUserPassword = dbUser.getPassword();
            String dtoPassword = loginDto.getPassword();
            String md5Hex = DigestUtils.md5DigestAsHex((dtoPassword + salt).getBytes());
            if (!dbUserPassword.equals(md5Hex)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            // 1.5 登录成功，返回token
            LoginVo loginVo = new LoginVo();
            BeanUtils.copyProperties(dbUser,loginVo);
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String, Object> result = new HashMap<>();
            result.put("user", loginVo);
            result.put("token", token);
            return ResponseResult.okResult(result);
        }
        // 2.游客  同样返回token  id = 0
        Map<String, Object> map = new HashMap<>();
        map.put("token", AppJwtUtil.getToken(0L));
        return ResponseResult.okResult(map);
    }
}
