package com.autosys.system.user.domain.model;

import com.autosys.common.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserParamModel extends Page {

    private static final long serialVersionUID = 1L;

    /** 用户账号 */
    @ApiModelProperty(value = "用户账号")
    private String username;

    /** 帐号状态（正常-ENABLE,停用-DISABLE,锁定-LOCKED） */
    @ApiModelProperty(value = "帐号状态（正常-ENABLE,停用-DISABLE,锁定-LOCKED）")
    @NotBlank
    private String status;

}
