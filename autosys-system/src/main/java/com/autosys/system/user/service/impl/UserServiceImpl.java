package com.autosys.system.user.service.impl;

import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.mapper.IUserMapper;
import com.autosys.system.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    /**
     * 验证用户名是否存在
     * @param username
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    public boolean checkUserNameUnique(String username){
        return userMapper.checkUserNameUnique(username) > 0;
    }

    /**
     * 验证手机号是否存在
     * @param phone
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    public boolean checkPhoneUnique(String phone){
        return userMapper.checkPhoneUnique(phone) > 0;
    }

    /**
     * 验证邮箱是否存在
     * @param email
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    public boolean checkEmailUnique(String email){
        return userMapper.checkEmailUnique(email) > 0;
    }

    /**
     * 保存用户信息
     * @param user
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    public void insertUser(User user){
        userMapper.insertUser(user);
    }
}
