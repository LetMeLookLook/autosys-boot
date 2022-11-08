package com.autosys.system.permission.domain.model;

import com.autosys.common.core.annotation.EnumValid;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description 新增菜单参数模型
 * @author jingqiu.wang
 * @date 2022年10月28日 14点50分
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AddPermissionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "父id")
	private String parentId;

	@ApiModelProperty(value = "菜单名称")
	@NotBlank(message = "菜单名称不能为空")
	private String name;
	@ApiModelProperty(value = "菜单Title")
	@NotBlank(message = "菜单Title不能为空")
	private String title;
	@ApiModelProperty(value = "请求路径")
	@NotBlank(message = "路径不能为空")
	private String url;
	@ApiModelProperty(value = "对应前台组件component")
	private String component;
	@ApiModelProperty(value = "组件名称")
	@NotBlank(message = "组件名称不能为空")
	private String componentName;
	@ApiModelProperty(value = "权限标识")
	private String permissionId;
	@ApiModelProperty(value = "菜单类型：CATALOG-目录, MENU-菜单, BUTTON-按钮")
	@EnumValid(message = "菜单类型为:菜单类型：CATALOG-目录, MENU-菜单, BUTTON-按钮", value = {"CATALOG","MENU","BUTTON"})
	private String menuType;
	@ApiModelProperty(value = "菜单排序")
	private Integer sortNo;
	@ApiModelProperty(value = "菜单图标")
	private String icon;
	@ApiModelProperty(value = "是否叶子节点:1-是, 0-否")
	@TableField(value="is_leaf")
	private Integer leaf;
	@ApiModelProperty(value = "描述")
	private String description;
}
