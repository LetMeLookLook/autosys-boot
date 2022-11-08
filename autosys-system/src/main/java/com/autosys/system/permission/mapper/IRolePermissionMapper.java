package com.autosys.system.permission.mapper;

import com.autosys.system.permission.domain.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @description 角色与菜单关系 Mapper
 * @author jingqiu.wang
 * @date 2022年9月14日 13点55分
 */
@Mapper
public interface IRolePermissionMapper extends BaseMapper<RolePermission> {

    void deleteByRole(@Param("roleId") String roleId);

    List<String> searchRoleIdsByPermissionId(@Param("permissionId") String permissionId);

 	List<String>  getPermissionIdsByRole(@Param("roleId") String roleId);
}
