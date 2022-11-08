package com.autosys.redis;

import com.autosys.common.core.domain.vo.LoginUser;
import com.autosys.common.core.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTestMethod {

    @Autowired
    public RedisUtil redisUtil;

    /**
     * 测试 Redis Value Json序列化
     */
    @Test
    public void testMethod1(){
        LoginUser testUser = new LoginUser();
        testUser.setUsername("testUser");
        redisUtil.set(testUser.getUsername(),testUser);
        LoginUser  resultUser = (LoginUser)redisUtil.get(testUser.getUsername());
        System.out.println(resultUser);
    }
}
