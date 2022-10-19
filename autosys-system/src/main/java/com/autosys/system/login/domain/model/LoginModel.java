package com.autosys.system.login.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 登录参数实体
 * @author jingqiu.wang
 * @date 2022年9月23日 14点45分
 */
@ApiModel(value="登录参数实体", description="登录参数实体")
@Data
public class LoginModel {
	@ApiModelProperty(value = "账号")
    private String username;
	@ApiModelProperty(value = "密码")
    private String password;
}
