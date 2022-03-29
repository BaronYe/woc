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
 * 积分商品
 * </p>
 *
 * @author simon
 * @since 2021-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IntegralGoods implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 积分商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 积分分类ID
     */
    private Integer cid;

    /**
     * 积分数量
     */
    private Integer totalCount;

    /**
     * 标题
     */
    private String title;

    /**
     * 商品ID
     */
    private Integer itemId;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 是否上架
     */
    private String isShelves;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 限购数量 0 时不限购
     */
    private Integer isPurchasing;

    /**
     * 积分简介
     */
    private String content;

    /**
     * 销量
     */
    private Integer sales;


}
