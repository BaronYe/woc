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
 * 购买订单转出售
 * </p>
 *
 * @author simon
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsOrderSell implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 出售订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 出售者ID
     */
    private Integer sellUserId;

    /**
     * 盲盒ID
     */
    private Integer boxId;

    /**
     * 盲盒分类ID
     */
    private Integer boxClassifyId;

    /**
     * 盲盒图片
     */
    private String boxCover;

    /**
     * 标题
     */
    private String title;

    /**
     * 价格
     */
    private String price;

    /**
     * 介绍
     */
    private String content;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已经出售
     */
    private Integer state;

    /**
     * 盲盒商品ID
     */
    private Integer boxItemId;
}
