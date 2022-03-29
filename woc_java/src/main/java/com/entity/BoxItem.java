package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 盲盒下的商品
 * </p>
 *
 * @author simon
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BoxItem implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 盲盒id
     */
    private Integer boxId;

    /**
     * 规格ID
     */
    private Integer spescId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 概率百分比
     */
    private String probability;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;


}
