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
 * 商品
 * </p>
 *
 * @author simon
 * @since 2021-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Item implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名字
     */
    private String title;

    /**
     * 商品价格
     */
    private String price;

    /**
     * 商品类型
     */
    private String types;

    /**
     * 商品特价
     */
    private String specialPrice;

    /**
     * 商品描述
     */
    private String text;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 分类id
     */
    private Integer cid;

    /**
     * 2级分类id
     */
    private Integer sonCid;

    /**
     * 是的删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 商品主图
     */
    private String imgs;

    /**
     * 是否上架
     */
    private String isShelves;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 排序
     */
    private Integer isDesc;



}
