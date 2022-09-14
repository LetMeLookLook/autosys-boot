package com.autosys.system.user.mapper;

import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IUserMapper extends BaseMapper<User> {

    Integer checkUserNameUnique(String username);

    Integer checkPhoneUnique(String phone);

    Integer checkEmailUnique(String email);

    void insertUser(User user);

    IPage<User> queryPageList(Page<User> page, @Param("param") UserParamModel paramModel);
}
