package com.autosys.system.role.service;

import com.autosys.system.role.domain.entity.Role;
import com.autosys.system.role.domain.model.RoleParamModel;
import com.autosys.system.user.domain.entity.User;
import com.autosys.system.user.domain.model.UserParamModel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @description 角色管理
 * @author jingqiu.wang
 * @date 2022年9月14日 13点55分
 */
public interface IRoleService {

    /**
     * 保存角色信息
     * @param role
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    void insertRole(Role role);

    /**
     * 修改角色信息ById
     * @param role
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    boolean updateById(Role role);

    /**
     * 获取角色信息ById
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    Role getById(String id);

    /**
     * 删除角色信息ById
     * @param id
     * @return
     * @author jingqiu.wang
     * @date 2022年9月14日 13点56分
     */
    boolean deleteById(String id);

    /**
     * 分页列表查询角色信息
     * @param page
     * @param paramModel
     * @return IPage<User>
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    IPage<Role> queryPageList(Page<Role> page, RoleParamModel paramModel);

    /**
     * 校验角色代码是否存在
     * @param roleCode
     * @return boolean
     * @author jingqiu.wang
     * @date 2022年9月13日 14点23分
     */
    boolean checkRoleCodeUnique(String roleCode);
}
