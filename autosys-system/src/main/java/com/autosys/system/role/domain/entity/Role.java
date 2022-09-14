package com.autosys.system.role.domain.entity;

import com.autosys.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 角色实体
 * @author jingqiu.wang
 * @date 2022年9月14日 13点47分
 */
@Data
@TableName("sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 数据范围（全部数据权限-ALL,自定数据权限-CUSTOM,本部门数据权限-DEPT,本部门及以下数据权限-SUBORDINATE）
     */
    private String dataScope;

    /**
     * 角色状态（正常-ENABLE,停用-DISABLE）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
