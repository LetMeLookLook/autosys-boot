package com.autosys.system.role.controller;

import com.autosys.common.core.api.CommonResult;
import com.autosys.system.role.domain.entity.Role;
import com.autosys.system.role.domain.model.RoleParamModel;
import com.autosys.system.role.service.IRoleService;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.autosys.system.user.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.autosys.common.core.util.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @description 角色管理
 * @author jingqiu.wang
 * @date 2022年9月14日 13点43分
 */
@Api(value = "角色管理", tags = {"角色管理"})
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController
{
    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 新增角色
     */
    @ApiOperation(value = "新增角色")
    @PostMapping("/add")
    public CommonResult<Object> add(@RequestBody Role role)
    {
        String validateInfo = validateRole(role);
        if(null != validateInfo){
            return CommonResult.failed(validateInfo);
        }
        role.setId(UUID.randomUUID().toString());
        roleService.insertRole(role);
        return CommonResult.success();
    }

    /**
     * 修改角色ById
     */
    @ApiOperation(value = "修改角色")
    @PostMapping("/updateById")
    public CommonResult<Object> updateById(@RequestBody Role role)
    {
        if(null == role || StringUtils.isBlank(role.getId())){
            return CommonResult.failed("角色ID为空");
        }
        return CommonResult.success(roleService.updateById(role));
    }

    /**
     * 获取角色信息ById
     */
    @ApiOperation(value = "获取角色信息ById")
    @GetMapping("/getById")
    public CommonResult<Object> getById(String id)
    {
        if(StringUtils.isBlank(id)){
            return CommonResult.failed("角色ID为空");
        }
        return CommonResult.success(roleService.getById(id));
    }

    /**
     * 删除角色信息ById
     */
    @ApiOperation(value = "删除角色信息ById")
    @PostMapping("/deleteById")
    public CommonResult<Object> deleteById(@RequestBody String id)
    {
        if(StringUtils.isBlank(id)){
            return CommonResult.failed("角色ID为空");
        }
        return CommonResult.success(roleService.deleteById(id));
    }

    @ApiOperation("列表分页查询")
    @GetMapping("/queryPageList")
    @ResponseBody
    public CommonResult<Object> queryPageList(RoleParamModel paramModel) {
        Page<Role> page = new Page<>(paramModel.getPageNo(), paramModel.getPageSize());
        IPage<Role> resultList = roleService.queryPageList(page,paramModel);
        return CommonResult.success(resultList);
    }

    private String validateRole(Role role){
        if (StringUtils.isBlank(role.getRoleCode()))
        {
            return "新增角色'" + role.getRoleCode() == null ? "" : role.getRoleCode() + "'失败，角色代码为空";
        }else if (roleService.checkRoleCodeUnique(role.getRoleCode()))
        {
            return "新增角色'" + role.getRoleCode() + "'失败，角色代码已存在";
        }
        return null;
    }

}
