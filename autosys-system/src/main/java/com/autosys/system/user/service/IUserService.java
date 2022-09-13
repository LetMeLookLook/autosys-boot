package com.autosys.system.user.service;

import com.autosys.system.user.domain.entity.User;

/**
 * @description 用户管理
 * @author jingqiu.wang
 * @date 2022年8月29日 16点13分
 */
public interface IUserService {

    /**
     * 验证用户名是否存在
     * @param userName
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    boolean checkUserNameUnique(String userName);

    /**
     * 验证手机号是否存在
     * @param phone
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    boolean checkPhoneUnique(String phone);

    /**
     * 验证邮箱是否存在
     * @param email
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    boolean checkEmailUnique(String email);

    /**
     * 保存用户信息
     * @param user
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    void insertUser(User user);
}
