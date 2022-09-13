package com.autosys.system.user.mapper;

import com.autosys.system.user.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper extends BaseMapper<User> {

    Integer checkUserNameUnique(String username);

    Integer checkPhoneUnique(String phone);

    Integer checkEmailUnique(String email);

    void insertUser(User user);
}
