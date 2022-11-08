package com.autosys.common.core.util;

import com.autosys.common.core.constants.CommonConstant;
import com.autosys.common.core.desensitization.util.SensitiveInfoUtil;
import com.autosys.common.core.domain.vo.LoginUser;
import com.autosys.common.core.service.ICommonService;
import lombok.extern.slf4j.Slf4j;
import com.autosys.common.core.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @description Token工具类
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Slf4j
public class TokenUtil {

    /**
     * 获取 request 里传递的 token
     *
     * @param request
     * @return
     */
    public static String getTokenByRequest(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token == null) {
            token = request.getHeader("X-Access-Token");
        }
        return token;
    }

    /**
     * 验证Token
     */
    public static boolean verifyToken(HttpServletRequest request, ICommonService commonService, RedisUtil redisUtil) {
        log.debug(" -- url --" + request.getRequestURL());
        String token = getTokenByRequest(request);
        return TokenUtil.verifyToken(token, commonService, redisUtil);
    }

    /**
     * 验证Token
     */
    public static boolean verifyToken(String token, ICommonService commonService, RedisUtil redisUtil) {
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("token不能为空!");
        }

        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new RuntimeException("token非法无效!");
        }

        // 查询用户信息
        LoginUser user = TokenUtil.getLoginUser(username, commonService, redisUtil);
        if (user == null) {
            throw new RuntimeException("用户不存在!");
        }
        // 判断用户状态-锁定
        if (CommonConstant.SYS_USER_STATUS_LOCKED.equals(user.getStatus())) {
            throw new RuntimeException("账号已被锁定,请联系管理员!");
        }
        // 判断用户状态-停用
        if (CommonConstant.SYS_USER_STATUS_DISABLE.equals(user.getStatus())) {
            throw new RuntimeException("账号已被停用,请联系管理员!");
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, user.getPassword(), redisUtil)) {
            throw new RuntimeException(CommonConstant.MSG_TOKEN_IS_INVALID);
        }
        return true;
    }

    /**
     * 刷新token（保证用户在线操作不掉线）
     * @param token
     * @param userName
     * @param passWord
     * @param redisUtil
     * @return
     */
    private static boolean jwtTokenRefresh(String token, String userName, String passWord, RedisUtil redisUtil) {
        if (redisUtil.hasKey(CommonConstant.PREFIX_USER_TOKEN + token)) {
            // 校验token有效性
            if (!JwtUtil.verify(token, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                // 设置Toekn缓存有效时间
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization,JwtUtil.EXPIRE_TIME * 2 / 1000);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取登录用户
     *
     * @param commonService
     * @param username
     * @param redisUtil
     * @return
     */
    public static LoginUser getLoginUser(String username, ICommonService commonService, RedisUtil redisUtil) {
        LoginUser loginUser = null;
        String loginUserKey = CommonConstant.CACHE_SYS_USER + "::" + username;
        //【重要】此处通过redis原生获取缓存用户，是为了解决微服务下system服务挂了，其他服务互调不通问题---
        if (redisUtil.hasKey(loginUserKey)) {
//        if (false) {
            try {
                loginUser = (LoginUser) redisUtil.get(loginUserKey);
                //解密用户
                SensitiveInfoUtil.handlerObject(loginUser, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            // 查询用户信息
            loginUser = commonService.getUserByName(username);
        }
        return loginUser;
    }
}
