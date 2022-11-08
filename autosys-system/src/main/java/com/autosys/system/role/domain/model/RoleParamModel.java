package com.autosys.system.role.domain.model;

import com.autosys.common.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleParamModel extends Page {

    private static final long serialVersionUID = 1L;

    /** 角色代码 */
    @ApiModelProperty(value = "角色代码")
    private String roleCode;

    /** 角色名称 */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    private String userId;
}
