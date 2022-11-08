package com.autosys.system.role.mapper;

import com.autosys.system.role.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface IRoleMapper extends BaseMapper<Role> {

    List<Role> queryRoleByUserId(@Param("userId") String userId);
}
