package com.autosys.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description 分页参数模型
 * @author jingqiu.wang
 * @date 2022年9月13日 15点59分
 */
@Data
public class Page {

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    /**
     * 排序方式， 如果为空的默认排序方式为车型, 传参类型类 字段名|排序方式
     */
    @ApiModelProperty(value = "排序字段")
    private String sort;

    /**
     * 页记录数
     */
    @ApiModelProperty(value = "页记录数")
    private Integer pageSize;

    /**
     * 排序规则
     */
    @ApiModelProperty(value = "排序规则")
    private String orderRule;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String orderKey;

}
