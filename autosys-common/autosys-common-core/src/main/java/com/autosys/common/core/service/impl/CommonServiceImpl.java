package com.autosys.common.core.service.impl;

import com.autosys.common.core.constants.CommonConstant;
import com.autosys.common.core.desensitization.annotation.SensitiveEncode;
import com.autosys.common.core.domain.vo.LoginUser;
import com.autosys.common.core.mapper.ICommonMapper;
import com.autosys.common.core.service.ICommonService;
import lombok.extern.slf4j.Slf4j;
import com.autosys.common.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description 通用服务接口实现类
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Service
@Slf4j
public class CommonServiceImpl implements ICommonService {

	@Resource
	private ICommonMapper commonMapper;

	@Override
	@Cacheable(cacheNames= CommonConstant.CACHE_SYS_USER, key="#username")
	public LoginUser getUserByName(String username) {
		if(StringUtils.isEmpty(username)) {
			return null;
		}
		return commonMapper.getUserByName(username);
	}
}
