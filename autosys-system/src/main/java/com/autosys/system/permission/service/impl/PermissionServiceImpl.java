package com.autosys.system.permission.service.impl;


import com.autosys.system.permission.domain.entity.Permission;
import com.autosys.system.permission.domain.model.AddPermissionModel;
import com.autosys.system.permission.mapper.IPermissionMapper;
import com.autosys.system.permission.mapper.IRolePermissionMapper;
import com.autosys.system.permission.service.IPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PermissionServiceImpl extends ServiceImpl<IPermissionMapper, Permission> implements IPermissionService {

	@Resource
	private IPermissionMapper permissionMapper;
	@Resource
	private IRolePermissionMapper rolePermissionMapper;

	@Override
	public List<Permission> queryByRole(String roleId) {
		return permissionMapper.queryByRole(roleId);
	}

	@Override
	public List<String> queryButton(String roleId, String parentId) {
		return permissionMapper.queryButtonByRole(roleId, parentId);
	}

	/**
	  * 真实删除
	 */
	@Override
	@Transactional
	@CacheEvict(value = "permission",allEntries=true)
	public void deletePermission(String id){
		Permission sysPermission = this.getById(id);
		String pid = sysPermission.getParentId();
		long count = this.count(new QueryWrapper<Permission>().lambda().eq(Permission::getParentId, pid));
		if(count==1) {
			//若父节点无其他子节点，则修改父节点为叶子节点
			permissionMapper.setMenuLeaf(pid, 1);
		}
		permissionMapper.deleteById(id);
		// 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
		this.removeChildrenBy(sysPermission.getId());
	}

	/**
	 * 根据父id删除其关联的子节点数据
	 */
	public void removeChildrenBy(String parentId) {
		LambdaQueryWrapper<Permission> query = new LambdaQueryWrapper<>();
		// 封装查询条件parentId为主键,
		query.eq(Permission::getParentId, parentId);
		// 查出该主键下的所有子级
		List<Permission> permissionList = this.list(query);
		if (permissionList != null && permissionList.size() > 0) {
			String id = ""; // id
			long num = 0; // 查出的子级数量
			// 如果查出的集合不为空, 则先删除所有
			this.remove(query);
			// 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
			for (int i = 0, len = permissionList.size(); i < len; i++) {
				id = permissionList.get(i).getId();
				num = this.count(new LambdaQueryWrapper<Permission>().eq(Permission::getParentId, id));
				// 如果有, 则递归
				if (num > 0) {
					this.removeChildrenBy(id);
				}
			}
		}
	}

	/**
	  * 逻辑删除
	 */
	@Override
	@CacheEvict(value = "permission",allEntries=true)
	//@CacheEvict(value = "permission",allEntries=true,condition="#sysPermission.menuType==2")
	public void deletePermissionLogical(String id){
		Permission sysPermission = this.getById(id);
		String pid = sysPermission.getParentId();
		long count = this.count(new QueryWrapper<Permission>().lambda().eq(Permission::getParentId, pid));
		if(count==1) {
			//若父节点无其他子节点，则该父节点是叶子节点
			permissionMapper.setMenuLeaf(pid, 1);
		}
		sysPermission.setDelFlag(1);
		this.updateById(sysPermission);
	}

	@Override
	@CacheEvict(value = "permission",allEntries=true)
	public void addPermission(AddPermissionModel bean){
		Permission sysPermission=new Permission();
		sysPermission.setName(bean.getName());
		sysPermission.setComponent(bean.getComponent());
		sysPermission.setComponentName(bean.getComponentName());
		sysPermission.setIcon(bean.getIcon());
		sysPermission.setUrl(bean.getUrl());
		sysPermission.setMenuType(bean.getMenuType());
		sysPermission.setPermissionId(bean.getPermissionId());
		sysPermission.setCreateTime(new Date());
		sysPermission.setDelFlag(0);
		sysPermission.setLeaf(bean.getLeaf());
		sysPermission.setTitle(bean.getTitle());
		sysPermission.setSortNo(bean.getSortNo());
		sysPermission.setParentId(bean.getParentId());
		this.save(sysPermission);
	}

	@Override
	@CacheEvict(value = "permission",allEntries=true)
	public void editPermission(Permission sysPermission){
		if(sysPermission.getParentId()!=null && sysPermission.getParentId().equalsIgnoreCase("0")){
			sysPermission.setParentId(null);
		}
		this.updateById(sysPermission);
	}

	@Override
	public List<Permission> queryByUser(String username) {
		return permissionMapper.queryByUser(username);
	}

	/**
	 * @Title: getMenuInfoByMenuCode
	 * @Description: 根据菜单名称获取菜单信息
	 * @param: menuCode
	 * @return: Result
	 * @date: 2020年4月22日 17点53分
	 * @throws: Exception
	 * @author: jingqiu.wang
	 */
	public Permission getMenuInfoByMenuCode(String menuCode){
		QueryWrapper<Permission> sysPermissionQueryWrapper = new QueryWrapper<Permission>();
		sysPermissionQueryWrapper.eq("NAME",menuCode);
		List<Permission> sysPermissionList = permissionMapper.selectList(sysPermissionQueryWrapper);
		if(null != sysPermissionList && sysPermissionList.size() > 0){
			return sysPermissionList.get(0);
		}else{
			return null;
		}
	}
	@Override
	public List<String> getRoleByPermission(String permissionId) {
		List<String> roleIdList = rolePermissionMapper.searchRoleIdsByPermissionId(permissionId);
		return roleIdList;
	}
}
