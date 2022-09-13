package com.autosys.system.user.controller;

import com.autosys.common.core.api.CommonResult;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @description 用户管理
 * @author jingqiu.wang
 * @date 2022年8月29日 16点13分
 */
@Api(value = "用户管理", tags = {"用户管理"})
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private IUserService userService;

    /**
     * 新增用户
     */
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @PostMapping("/add")
    public CommonResult<Object> add(@RequestBody User user)
    {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()))
        {
            return CommonResult.failed("新增用户'" + user.getUsername() + "'失败，用户名或密码为空");
        }else if (userService.checkUserNameUnique(user.getUsername()))
        {
            return CommonResult.failed("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhone())
                && userService.checkPhoneUnique(user.getPhone()))
        {
            return CommonResult.failed("新增用户'" + user.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && userService.checkEmailUnique(user.getEmail()))
        {
            return CommonResult.failed("新增用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setId(UUID.randomUUID().toString());
        //随机字符串
        String salt = UUID.randomUUID().toString();
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5DigestAsHex((user.getUsername() + user.getPassword() + salt).getBytes()));
        userService.insertUser(user);
        return CommonResult.success();
    }

}
