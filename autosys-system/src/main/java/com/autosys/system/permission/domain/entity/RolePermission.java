package com.autosys.system.permission.domain.entity;

import com.autosys.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description 角色实体
 * @author jingqiu.wang
 * @date 2022年9月14日 13点47分
 */
@Data
@AllArgsConstructor
@TableName("sys_role_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RolePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;
}
