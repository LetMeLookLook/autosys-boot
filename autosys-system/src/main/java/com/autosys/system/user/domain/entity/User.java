package com.autosys.system.user.domain.entity;

import com.autosys.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @description 用户实体
 * @author jingqiu.wang
 * @date 2022年8月29日 16点13分
 */
@Data
@TableName("sys_user")
public class User extends BaseEntity
{

    private static final long serialVersionUID = 1L;

    /** 用户账号 */
    @ApiModelProperty(value = "用户账号")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String username;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    @Size(min = 0, max = 50, message = "用户名称长度不能超过50个字符")
    private String realname;

    /** 用户类型 */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /** 用户邮箱 */
    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /** 手机号码 */
    @ApiModelProperty(value = "手机号码")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phone;

    /** 用户性别 */
    @ApiModelProperty(value = "用户性别")
    @NotBlank
    private String sex;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    /** 帐号状态（正常-ENABLE,停用-DISABLE,锁定-LOCKED） */
    @ApiModelProperty(value = "帐号状态（正常-ENABLE,停用-DISABLE,锁定-LOCKED）")
    @NotBlank
    private String status;

    /** salt */
    private String salt;
}
