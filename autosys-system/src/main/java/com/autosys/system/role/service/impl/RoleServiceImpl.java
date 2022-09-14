package com.autosys.system.role.service.impl;

import com.autosys.common.core.util.FieldUtil;
import com.autosys.system.role.domain.entity.Role;
import com.autosys.system.role.domain.model.RoleParamModel;
import com.autosys.system.role.mapper.IRoleMapper;
import com.autosys.system.role.service.IRoleService;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.autosys.system.user.mapper.IUserMapper;
import com.autosys.system.user.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;

@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<IRoleMapper, Role> implements IRoleService {

    @Resource
    private IRoleMapper roleMapper;

    /**
     * 保存用户信息
     * @param role
     * @return
     * @author jingqiu.wang
     * @date 2022年8月31日 15点32分
     */
    public void insertRole(Role role){
        roleMapper.insert(role);
    }

    /**
     * 修改角色信息ById
     * @param role
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    public boolean updateById(Role role){
        return roleMapper.updateById(role) > 0;
    }

    /**
     * 获取角色信息ById
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    public Role getById(String id){
        return roleMapper.selectById(id);
    }

    /**
     * 删除角色信息ById
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    public boolean deleteById(String id){
        return roleMapper.deleteById(id) > 0;
    }

    /**
     * 分页列表查询角色信息
     * @param page
     * @param paramModel
     * @return IPage<User>
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public IPage<Role> queryPageList(Page<Role> page, RoleParamModel paramModel){
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(paramModel.getRoleCode())){
            queryWrapper.like("role_code",paramModel.getRoleCode());
        }
        if(StringUtils.isNotBlank(paramModel.getRoleName())){
            queryWrapper.like("role_name",paramModel.getRoleName());
        }
        return roleMapper.selectPage(page, queryWrapper);
    }

    /**
     * 校验角色代码是否存在
     * @param roleCode
     * @return boolean
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    public boolean checkRoleCodeUnique(String roleCode){
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("role_code",roleCode);
        return roleMapper.exists(queryWrapper);
    }
}
