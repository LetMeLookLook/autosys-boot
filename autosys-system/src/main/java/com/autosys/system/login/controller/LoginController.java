package com.autosys.system.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.autosys.common.core.api.CommonResult;
import com.autosys.common.core.constants.CommonConstant;
import com.autosys.common.core.constants.enums.ResultCodeEnum;
import com.autosys.common.core.domain.vo.LoginUser;
import com.autosys.common.core.exception.ApiException;
import com.autosys.common.core.service.ICommonService;
import com.autosys.common.core.util.JwtUtil;
import com.autosys.common.core.util.RedisUtil;
import com.autosys.system.login.domain.model.LoginModel;
import com.autosys.system.role.domain.entity.Role;
import com.autosys.system.role.service.IRoleService;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.autosys.common.core.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 登录授权管理
 * @author jingqiu.wang
 * @date 2022年9月23日 14点45分
 */
@RestController
@RequestMapping("/auth")
@Api(tags="登录授权管理")
@Slf4j
public class LoginController {
	private final IUserService userService;
	private final IRoleService roleService;
    private final RedisUtil redisUtil;
    @Resource
    private ICommonService commonService;

	public LoginController(IUserService userService, IRoleService roleService, RedisUtil redisUtil) {
		this.userService = userService;
		this.roleService = roleService;
		this.redisUtil = redisUtil;
	}

	@ApiOperation("登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public CommonResult<Object> login(@RequestBody LoginModel loginModel){
		//校验用户是否有效
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getUsername,loginModel.getUsername());
		User sysUser = userService.getOne(queryWrapper);
		CommonResult<Object> effectiveResult = userService.checkUserIsEffective(sysUser);
		if(null != effectiveResult) {
			return effectiveResult;
		}
		return CommonResult.success(userInfo(sysUser));
	}

	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation("退出登录")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public CommonResult<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
	    String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
	    if(StringUtils.isEmpty(token)) {
	    	return CommonResult.failed("Token为空，退出登录失败！");
	    }
	    String username = JwtUtil.getUsername(token);
		LoginUser sysUser = commonService.getUserByName(username);
	    if(sysUser!=null) {
	    	log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
	    	//清空用户登录Token缓存
	    	redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
			//调用shiro的logout
			SecurityUtils.getSubject().logout();
	    	return CommonResult.success("退出登录成功！");
	    }else {
	    	return CommonResult.failed("Token无效!");
	    }
	}

	/**
	 * 用户信息处理
	 * @param user
	 * @return
	 */
	private JSONObject userInfo(User user) {
		String username = user.getUsername();
		String sysPassword = user.getPassword();
		JSONObject obj = new JSONObject(new LinkedHashMap<>());
		// 生成token
		String token = JwtUtil.sign(username, sysPassword);
		// 设置token缓存有效时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token,JwtUtil.EXPIRE_TIME * 2 / 1000);
		//获取用户当前角色
		List<Role> roleList = null;
		roleList = roleService.queryRoleByUserId(user.getId());
		if (roleList == null || roleList.size() == 0) {
			throw new ApiException(ResultCodeEnum.CONSUMER_LOGIN_ERROR_NOROLES);
		} else {
			Role sysRole = roleList.get(0);
			obj.put("roleInfo", sysRole);

/*			redisUtil.set(Constants.PREFIX_ROLE_ID_TOKEN + token, sysRole.getId());
			redisUtil.set(Constants.PREFIX_ROLE_CODE_TOKEN + token, sysRole.getRoleCode());
			redisUtil.set(Constants.PREFIX_ROLE_NAME_TOKEN + token, sysRole.getRoleName());
			redisUtil.set(Constants.PREFIX_USER_AD_TOKEN + token, sysUser.getUsername());
			redisUtil.set(Constants.PREFIX_USER_NAME_TOKEN + token, sysUser.getRealname());
			redisUtil.set(Constants.PREFIX_USER_ID_TOKEN + token, sysRole.getId());
			//设置超时时间
			redisUtil.expire(Constants.PREFIX_ROLE_ID_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
			redisUtil.expire(Constants.PREFIX_ROLE_CODE_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
			redisUtil.expire(Constants.PREFIX_ROLE_NAME_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
			redisUtil.expire(Constants.PREFIX_USER_AD_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
			redisUtil.expire(Constants.PREFIX_USER_NAME_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
			redisUtil.expire(Constants.PREFIX_USER_ID_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);*/

		}
		obj.put("token", token);
		obj.put("userInfo", user);
		return obj;
	}
}
