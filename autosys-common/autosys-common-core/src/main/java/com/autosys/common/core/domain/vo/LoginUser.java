package com.autosys.common.core.domain.vo;

import com.autosys.common.core.desensitization.annotation.SensitiveField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 公共用户信息
 * @author jingqiu.wang
 * @date 2022年9月20日 10点09分
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginUser implements Serializable {

	/**
	 * 登录人id
	 */
	@SensitiveField
	private String id;

	/**
	 * 登录人账号
	 */
	@SensitiveField
	private String username;

	/**
	 * 登录人名字
	 */
	@SensitiveField
	private String realname;

	/**
	 * 登录人密码
	 */
	@SensitiveField
	private String password;


	/**
	 * 用户性别（男-MAN,女-WOMAN,未知-UNKNOW）
	 */
	private String sex;

	/**
	 * 电子邮件
	 */
	@SensitiveField
	private String email;

	/**
	 * 电话
	 */
	@SensitiveField
	private String phone;

	/**
	 * 帐号状态（正常-ENABLE,停用-DISABLE,锁定-LOCKED）
	 */
	private String status;

	/**
	 * 删除标志（0-未删除，1-已删除）
	 */
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
