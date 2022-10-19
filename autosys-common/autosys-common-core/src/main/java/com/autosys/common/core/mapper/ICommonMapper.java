package com.autosys.common.core.mapper;


import com.autosys.common.core.domain.vo.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description 通用服务Mapper
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Mapper
public interface ICommonMapper {

	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public LoginUser getUserByName(@Param("username") String username);
}
