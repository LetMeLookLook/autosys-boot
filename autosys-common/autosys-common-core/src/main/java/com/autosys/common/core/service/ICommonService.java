package com.autosys.common.core.service;

import com.autosys.common.core.domain.vo.LoginUser;
import org.springframework.stereotype.Service;

/**
 * @description 通用服务接口
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
@Service
public interface ICommonService{

    /**
     * 通过用户账号查询用户信息
     * @param username
     * @return
     */
    LoginUser getUserByName(String username);

}
