package com.autosys.system.permission.service;


import com.autosys.system.permission.domain.entity.Permission;
import com.autosys.system.permission.domain.model.AddPermissionModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @description 菜单管理
 * @author jingqiu.wang
 * @date 2022年9月14日 13点55分
 */
public interface IPermissionService extends IService<Permission> {

	/**
	 * 通过角色id查询角色的菜单权限
	 * @param: roleId 角色ID
	 * @return: List<Permission> 菜单集合
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	List<Permission> queryByRole(String roleId);

	/**
	 * 通过角色id和上级菜单id查询角色显示的按钮
	 * @param: roleId 角色ID
	 * @param: parentId 菜单ID
	 * @return: List<String> 按钮集合
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	List<String> queryButton(String roleId, String parentId);

	/**
	 * 真实删除菜单
	 * @param: id 菜单ID
	 * @return:
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	void deletePermission(String id);

	/**
	 * 逻辑删除菜单
	 * @param: id 菜单ID
	 * @return:
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	void deletePermissionLogical(String id);

	/**
	 * 新增菜单
	 * @param: bean 新增菜单入参模型
	 * @return:
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	public void addPermission(AddPermissionModel bean);

	/**
	 * 维护菜单
	 * @param: permission 维护菜单入参模型
	 * @return:
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	public void editPermission(Permission permission);

	/**
	 * 根据用户名获取用户对应菜单
	 * @param: username 用户名
	 * @return: List<Permission> 菜单集合
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	public List<Permission> queryByUser(String username);

	/**
	 * 查询菜单是否被角色占用
	 * @param: permissionId 菜单ID
	 * @return: List<String> 菜单对应角色集合
	 * @author jingqiu.wang
	 * @date 2022年10月28日 14点35分
	 */
	List<String> getRoleByPermission(String permissionId);
}
