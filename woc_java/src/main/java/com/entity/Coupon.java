package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券
 * </p>
 *
 * @author simon
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Coupon implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 优惠券id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 优惠券名称
     */
    private String title;

    /**
     * 优惠券类型0满减1折扣
     */
    private String types;

    /**
     * 优惠券面额
     */
    private Integer couponPrice;

    /**
     * 优惠券折扣
     */
    private String discount;

    /**
     * 0 失效 1可以使用
     */
    private String couponStatus;

    /**
     * 优惠券的数量
     */
    private Integer totalCount;

    /**
     * 优惠领取数量
     */
    private Integer toCount;

    /**
     * 优惠券最小使用价格
     */
    private Integer useMinPrice;

    /**
     * 有效期类型 0 固定时间 1 领取之日起
     */
    private String timeStatus;

    /**
     * 优惠券使用截止时间
     */
    private String couponTime;

    /**
     * 多少天有效
     */
    private Integer couponDays;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否指定店铺
     */
    private String shopStatus;

    /**
     * 对应的店铺
     */
    private Integer shopId;

    /**
     * 店铺类型
     */
    private String shopType;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 是否显示抢
     */
    private String isRob;

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
     * 不映射到数据库字段
     */
    @TableField(exist = false)
    private String sTime;
    @TableField(exist = false)
    private String eTime;
}
