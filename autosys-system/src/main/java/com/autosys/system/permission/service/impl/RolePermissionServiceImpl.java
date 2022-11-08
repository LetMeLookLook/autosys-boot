package com.autosys.system.permission.service.impl;


import com.autosys.common.core.util.StringUtils;
import com.autosys.system.permission.domain.entity.RolePermission;
import com.autosys.system.permission.mapper.IRolePermissionMapper;
import com.autosys.system.permission.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class RolePermissionServiceImpl extends ServiceImpl<IRolePermissionMapper, RolePermission> implements IRolePermissionService {
	@Resource
	private IRolePermissionMapper rolePermissionMapper;

	@Override
	public List<String> getPermissionByRole(String roleId) {
		List<String> permissionIds=rolePermissionMapper.getPermissionIdsByRole(roleId);
		return permissionIds;
	}

	@Override
	@CacheEvict(value="loginUser_cacheRules", allEntries=true)
	public void saveRolePermission(String roleId, String permissionIds) {
		LambdaQueryWrapper<RolePermission> query = new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId);
		this.remove(query);
		List<RolePermission> list = new ArrayList<RolePermission>();
		String arr[] = permissionIds.split(",");
		for (String p : arr) {
			if(StringUtils.isNotEmpty(p)) {
				RolePermission rolepms = new RolePermission(roleId, p);
				list.add(rolepms);
			}
		}
		this.saveBatch(list);
	}

	@Override
	@CacheEvict(value="loginUser_cacheRules", allEntries=true)
	public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
		List<String> add = getDiff(lastPermissionIds,permissionIds);
		if(add!=null && add.size()>0) {
			List<RolePermission> list = new ArrayList<RolePermission>();
			for (String p : add) {
				if(StringUtils.isNotEmpty(p)) {
					RolePermission rolepms = new RolePermission(roleId, p);
					list.add(rolepms);
				}
			}
			this.saveBatch(list);
		}

		List<String> delete = getDiff(permissionIds,lastPermissionIds);
		if(delete!=null && delete.size()>0) {
			for (String permissionId : delete) {
				this.remove(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId).eq(RolePermission::getPermissionId, permissionId));
			}
		}
	}

	/**
	 * 从diff中找出main中没有的元素
	 * @param main
	 * @param diff
	 * @return
	 */
	private List<String> getDiff(String main,String diff){
		if(StringUtils.isEmpty(diff)) {
			return Collections.emptyList();
		}
		if(StringUtils.isEmpty(main)) {
			return Arrays.asList(diff.split(","));
		}

		String[] mainArr = main.split(",");
		String[] diffArr = diff.split(",");
		Map<String, Integer> map = new HashMap<>();
		for (String string : mainArr) {
			map.put(string, 1);
		}
		List<String> res = new ArrayList<String>();
		for (String key : diffArr) {
			if(StringUtils.isNotEmpty(key) && !map.containsKey(key)) {
				res.add(key);
			}
		}
		return res;
	}
}
