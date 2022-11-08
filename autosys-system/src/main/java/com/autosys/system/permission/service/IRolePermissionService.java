package com.autosys.system.permission.service;


import com.autosys.system.permission.domain.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @description 角色与权限关系
 * @author jingqiu.wang
 * @date 2022年9月14日 13点55分
 */
public interface IRolePermissionService extends IService<RolePermission> {

	List<String>  getPermissionByRole(String roleId);

	/**
	 * 保存授权/先删后增
	 * @param roleId
	 * @param permissionIds
	 */
	void saveRolePermission(String roleId, String permissionIds);

	/**
	 * 保存授权 将上次的权限和这次作比较 差异处理提高效率
	 * @param roleId
	 * @param permissionIds
	 * @param lastPermissionIds
	 */
	void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds);



}
