package com.autosys.system.permission.mapper;

import com.autosys.system.permission.domain.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description 菜单管理
 * @author jingqiu.wang
 * @date 2022年9月14日 13点55分
 */
@Mapper
public interface IPermissionMapper extends BaseMapper<Permission> {

	/**
	 * 根据用户查询用户权限
	 * @param roleId
	 * @return
	 */
	public List<Permission> queryByRole(@Param("roleId") String roleId);

	/**
	 * 获取用户的按钮权限
	 * @param roleId
	 * @param parentId
	 * @return
	 */
	public List<String> queryButtonByRole(@Param("roleId") String roleId, @Param("parentId") String parentId);

	/**
	 * 根据用户查询用户权限
	 * @param username
	 * @return
	 */
	public List<Permission> queryByUser(@Param("username") String username);

	/**
	 *   修改菜单状态字段： 是否子节点
	 */
	/**
	 * 修改菜单状态字段： 是否子节点
	 * @param id 菜单ID
	 * @param leaf 是否叶子节点标识
	 * @return 被修改数据行数
	 */
	@Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
	public int setMenuLeaf(@Param("id") String id, @Param("leaf") int leaf);
}
