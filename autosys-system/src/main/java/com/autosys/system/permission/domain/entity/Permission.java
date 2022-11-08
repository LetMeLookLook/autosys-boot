package com.autosys.system.permission.domain.entity;

import com.autosys.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 角色实体
 * @author jingqiu.wang
 * @date 2022年9月14日 13点47分
 */
@Data
@TableName("sys_permission")
public class Permission extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 父id
	 */
	private String parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单标题
	 */
	private String title;

	/**
	 * 路径
	 */
	private String url;

	/**
	 * 组件
	 */
	private String component;

	/**
	 * 组件名字
	 */
	private String componentName;

	/**
	 * 权限标识
	 */
	private String permissionId;

	/**
	 * 菜单类型：CATALOG-目录, MENU-菜单, BUTTON-按钮
	 */
	private String menuType;

	/**
	 * 菜单排序
	 */
	private Integer sortNo;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 是否叶子节点:1-是, 0-否
	 */
	@TableField(value="is_leaf")
	private Integer leaf;

	/**
	 * 描述
	 */
	private String description;
}
