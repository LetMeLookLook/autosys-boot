package com.autosys.system.user.controller;

import com.autosys.common.core.api.CommonResult;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.autosys.system.user.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 新增用户
     */
    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    public CommonResult<Object> add(@RequestBody User user)
    {
        String validateInfo = validateUser(user);
        if(null != validateInfo){
            return CommonResult.failed(validateInfo);
        }
        user.setId(UUID.randomUUID().toString());
        //随机字符串
        String salt = UUID.randomUUID().toString();
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5DigestAsHex((user.getUsername() + user.getPassword() + salt).getBytes()));
        userService.insertUser(user);
        return CommonResult.success();
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    @ResponseBody
    public CommonResult<Object> register( @RequestBody User userParam) {
        String validateInfo = validateUser(userParam);
        if(null != validateInfo){
            return CommonResult.failed(validateInfo);
        }
        User user = userService.register(userParam);
        return CommonResult.success(user);
    }

    @ApiOperation("获取用户信息ById")
    @GetMapping("/getById")
    @ResponseBody
    public CommonResult<User> getById(String id) {
        if(StringUtils.isBlank(id)){
            return CommonResult.failed("用户ID为空");
        }
        return CommonResult.success(userService.getById(id));
    }

    @ApiOperation("修改用户信息")
    @GetMapping("/updateById")
    @ResponseBody
    public CommonResult<Object> updateById(User user) {
        if(null == user  || StringUtils.isBlank(user.getId())){
            return CommonResult.failed("用户ID为空");
        }
        return CommonResult.success(userService.updateById(user));
    }

    @ApiOperation("删除用户信息")
    @GetMapping("/deleteById")
    @ResponseBody
    public CommonResult<Object> deleteById(String id) {
        if(StringUtils.isBlank(id)){
            return CommonResult.failed("用户ID为空");
        }
        return CommonResult.success(userService.deleteById(id));
    }

    @ApiOperation("列表分页查询")
    @GetMapping("/queryPageList")
    @ResponseBody
    public CommonResult<Object> queryPageList(UserParamModel paramModel) {
        Page<User> page = new Page<>(paramModel.getPageNo(), paramModel.getPageSize());
        IPage<User> demandOrderModelList = userService.queryPageList(page,paramModel);
        return CommonResult.success(demandOrderModelList);
    }

    private String validateUser(User user){
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()))
        {
            return "新增用户'" + user.getUsername() + "'失败，用户名或密码为空";
        }else if (userService.checkUserNameUnique(user.getUsername()))
        {
            return "新增用户'" + user.getUsername() + "'失败，登录账号已存在";
        }
        else if (StringUtils.isNotEmpty(user.getPhone())
                && userService.checkPhoneUnique(user.getPhone()))
        {
            return "新增用户'" + user.getUsername() + "'失败，手机号码已存在";
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && userService.checkEmailUnique(user.getEmail()))
        {
            return "新增用户'" + user.getUsername() + "'失败，邮箱账号已存在";
        }
        return null;
    }

}
