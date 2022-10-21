package com.autosys.system.user.service;

import com.autosys.common.core.api.CommonResult;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description 用户管理
 * @author jingqiu.wang
 * @date 2022年8月29日 16点13分
 */
public interface IUserService extends IService<User> {

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

    /**
     * 注册用户
     * @param userParam
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    User register(User userParam);

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    User getById(String id);

    /**
     * 根据用户ID修改用户信息
     * @param user
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    boolean updateById(User user);

    /**
     * 分页列表查询用户信息
     * @param page
     * @param paramModel
     * @return IPage<User>
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    IPage<User> queryPageList(Page<User> page, UserParamModel paramModel);

    /**
     * 根据用户ID删除用户信息
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    boolean deleteById(String id);

    /**
     * 校验用户用户有效性
     * @param user
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    CommonResult<Object> checkUserIsEffective(User user);
}
