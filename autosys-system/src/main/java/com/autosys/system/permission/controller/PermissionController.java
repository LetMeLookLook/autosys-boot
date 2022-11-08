package com.autosys.system.permission.controller;

import com.autosys.common.core.api.CommonResult;
import com.autosys.common.core.constants.CommonConstant;
import com.autosys.common.core.exception.ApiException;
import com.autosys.common.core.util.StringUtils;
import com.autosys.system.permission.constant.PermissionConstant;
import com.autosys.system.permission.domain.entity.Permission;
import com.autosys.system.permission.domain.entity.RolePermission;
import com.autosys.system.permission.domain.model.AddPermissionModel;
import com.autosys.system.permission.domain.model.PermissionTree;
import com.autosys.system.permission.service.IPermissionService;
import com.autosys.system.permission.service.IRolePermissionService;
import com.autosys.system.role.domain.entity.Role;
import com.autosys.system.role.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 菜单管理
 * @author jingqiu.wang
 * @date 2022年9月23日 14点45分
 */
@Slf4j
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final IPermissionService permissionService;
    private final IRolePermissionService rolePermissionService;
    private final IRoleService roleService;

    public PermissionController(IPermissionService permissionService, IRolePermissionService rolePermissionService, IRoleService roleService) {
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
        this.roleService = roleService;
    }

    @ApiOperation(value = "加载全部菜单(通过角色id)", notes = "加载全部菜单（通过角色id）")
    @RequestMapping(value = "/listByRole", method = RequestMethod.POST)
    public CommonResult<List<PermissionTree>> listByRole(@RequestBody RolePermission bean) {
        List<Permission> list = permissionService.queryByRole(bean.getRoleId());
        List<PermissionTree> treeList = new ArrayList<>();
        getTreeList(treeList, list, null, bean.getRoleId());
        return CommonResult.success(treeList);
    }


    private void getTreeList(List<PermissionTree> treeList, List<Permission> metaList, PermissionTree temp, String roleId) {
        for (Permission permission : metaList) {
            String tempPid = permission.getParentId();
            PermissionTree tree = new PermissionTree(permission);
            if (temp == null && StringUtils.isEmpty(tempPid)) {
                if (StringUtils.isNotEmpty(permission.getParentId()) && PermissionConstant.MENU_TYPE_MENU.equals(permission.getMenuType())) {
                    List<String> buttonList = permissionService.queryButton(roleId, permission.getId());
                    tree.setBtnPermissions(buttonList);
                }
                treeList.add(tree);
                if (CommonConstant.LOGIC_NO == tree.getIsLeaf()) {
                    getTreeList(treeList, metaList, tree, roleId);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                if (StringUtils.isNotEmpty(permission.getParentId()) && PermissionConstant.MENU_TYPE_MENU.equals(permission.getMenuType())) {
                    List<String> buttonList = permissionService.queryButton(roleId, permission.getId());
                    tree.setBtnPermissions(buttonList);
                }
                temp.getChildren().add(tree);
                if (CommonConstant.LOGIC_NO == tree.getIsLeaf()) {
                    getTreeList(treeList, metaList, tree, roleId);
                }
            }

        }
    }

    /**
     * 加载全部菜单节点
     */
    @ApiOperation(value = "加载全部菜单节点", notes = "加载全部菜单节点")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<List<PermissionTree>> list() {
        LambdaQueryWrapper<Permission> query = new LambdaQueryWrapper<Permission>();
        query.eq(Permission::getDelFlag, CommonConstant.LOGIC_NO);
        query.orderByAsc(Permission::getSortNo);
        List<Permission> list = permissionService.list(query);
        List<PermissionTree> treeList = new ArrayList<>();
        getTreeList(treeList, list, null, null);
        return CommonResult.success(treeList);
    }

    /**
     * 添加菜单
     */
    @ApiOperation(value = "添加菜单", notes = "添加菜单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<Permission> add(@RequestBody @Validated AddPermissionModel bean) throws Exception {
        permissionService.addPermission(bean);
        return CommonResult.success(null);
    }

    /**
     * 编辑菜单
     */
    @ApiOperation(value = "编辑菜单", notes = "编辑菜单")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public CommonResult<Permission> edit(@RequestBody Permission permission) throws Exception {
        permissionService.editPermission(permission);
        return CommonResult.success(null);
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult<Permission> delete(@RequestBody Permission permission) throws Exception {
        StringBuilder errMessage = new StringBuilder("");
        // 校验菜单是否被角色占用，占用不能删除
        List<String> roleList = permissionService.getRoleByPermission(permission.getId());
        if (roleList != null && roleList.size() > 0) {
            Role sysRole = roleService.getById(roleList.get(0));
            errMessage.append("角色：" + sysRole.getRoleName() + "正在使用此菜单，不能删除！");
        }else{
            permissionService.deletePermission(permission.getId());
        }
        if (!errMessage.toString().equals("")) {
            return CommonResult.failed(new ApiException(500, errMessage.toString()));
        }
        return CommonResult.success(null);
    }

    /**
     * 批量删除菜单
     */
    @ApiOperation(value = "批量删除菜单", notes = "批量删除菜单")
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public CommonResult<Permission> deleteBatch(@RequestParam(name = "ids", required = true) String ids) throws Exception {
        String arr[] = ids.split(",");
        for (String id : arr) {
            if (StringUtils.isNotEmpty(id)) {
                permissionService.deletePermission(id);
            }
        }
        return CommonResult.success(null);
    }

    /**
     * 查询角色授权
     */
    @ApiOperation(value = "查询角色授权", notes = "查询角色授权")
    @RequestMapping(value = "/queryRolePermission", method = RequestMethod.GET)
    public CommonResult<List<String>> queryRolePermission(@RequestParam(name = "roleId", required = true) String roleId) {
		List<RolePermission> list = rolePermissionService.list(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId));
		return CommonResult.success(list.stream().map(RolePermission -> String.valueOf(RolePermission.getPermissionId())).collect(Collectors.toList()));
    }

    /**
     * 保存角色授权
     */
    @ApiOperation(value = "保存角色授权", notes = "保存角色授权")
    @RequestMapping(value = "/saveRolePermission", method = RequestMethod.POST)
    @RequiresRoles({"admin"})
    public CommonResult<String> saveRolePermission(@RequestBody Map<String,Object> paramMap) {
		String roleId = (String) paramMap.get("roleId");
		String permissionIds = (String) paramMap.get("permissionIds");
		String lastPermissionIds = (String) paramMap.get("lastpermissionIds");
		this.rolePermissionService.saveRolePermission(roleId, permissionIds, lastPermissionIds);
		return CommonResult.success(null);
    }
}
