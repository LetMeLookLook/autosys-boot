package com.autosys.system.user.service.impl;

import com.autosys.common.core.api.CommonResult;
import com.autosys.common.core.constants.CommonConstant;
import com.autosys.common.core.constants.enums.ResultCodeEnum;
import com.autosys.common.core.util.FieldUtil;
import com.autosys.common.core.util.StringUtils;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.autosys.system.user.mapper.IUserMapper;
import com.autosys.system.user.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<IUserMapper, User> implements IUserService {

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
        userMapper.insert(user);
//        userMapper.insertUser(user);
    }

    /**
     * 注册用户
     * @param userParam
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public User register(User userParam){
        this.insertUser(userParam);
        return userParam;
    }

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public User getById(String id){
        return getById(id);
    }

    /**
     * 根据用户ID修改用户信息
     * @param user
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public boolean updateById(User user){
        return userMapper.updateById(user) > 0;
    }

    /**
     * 分页列表查询用户信息
     * @param page
     * @param paramModel
     * @return IPage<User>
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public IPage<User> queryPageList(Page<User> page, UserParamModel paramModel){
        if(StringUtils.isNotBlank(paramModel.getOrderKey())){
            paramModel.setOrderKey(FieldUtil.convertFromCamel(paramModel.getOrderKey()));
        }
        return userMapper.queryPageList(page, paramModel);
    }

    /**
     * 根据用户ID删除用户信息
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public boolean deleteById(String id){
        return userMapper.deleteById(id) > 0;
    }

    /**
     * 校验用户有效性
     * @param user
     * @return
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    @Override
    public CommonResult<Object> checkUserIsEffective(User user){
        //根据用户信息查询，该用户不存在
        if (user == null) {
            return CommonResult.failed(ResultCodeEnum.CONSUMER_LOGIN_ERROR_NOTEXITS);
        }
        //根据用户信息查询，该用户已禁用
        if (CommonConstant.SYS_USER_STATUS_DISABLE.equals(user.getStatus())) {
            return CommonResult.failed(ResultCodeEnum.CONSUMER_LOGIN_ERROR_DISABLED);
        }
        //根据用户信息查询，该用户已锁定
        if (CommonConstant.SYS_USER_STATUS_LOCKED.equals(user.getStatus())) {
            return CommonResult.failed(ResultCodeEnum.CONSUMER_LOGIN_ERROR_LOCKED);
        }
        //根据用户信息查询，密码是否正确
        if (CommonConstant.SYS_USER_STATUS_LOCKED.equals(user.getStatus())) {
            String userPassword = DigestUtils.md5DigestAsHex((user.getUsername() + user.getPassword() + user.getSalt()).getBytes());
            String sysPassword = user.getPassword();
            if (!sysPassword.equals(userPassword)) {
                return CommonResult.failed(ResultCodeEnum.CONSUMER_LOGIN_ERROR_PASSWORD);
            }
        }
        return null;
    }

}
