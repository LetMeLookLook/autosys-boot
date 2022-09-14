package com.autosys.common.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MybatisPlusMetaObjectHandler  implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.setFieldValByName("createTime",new Timestamp(System.currentTimeMillis()),metaObject);
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Timestamp(System.currentTimeMillis()),metaObject);
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }
}
