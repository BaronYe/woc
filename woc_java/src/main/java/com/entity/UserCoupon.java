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
 * 我的优惠券
 * </p>
 *
 * @author simon
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserCoupon implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 优惠卷id
     */
    private Integer couponId;

    /**
     * 优惠券名称
     */
    private String couponTitle;

    /**
     * 优惠券类型0满减1折扣
     */
    private String couponTypes;

    /**
     * 优惠券面额
     */
    private Integer couponPrice;

    /**
     * 优惠券折扣
     */
    private String couponDiscount;

    /**
     * 优惠券最小使用价格
     */
    private Integer couponUseMinPrice;

    /**
     * 优惠券状态0未使用1已使用2已过期
     */
    private String state;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 是否指定店铺
     */
    private String shopStatus;

    /**
     * 对应的店铺
     */
    private Integer shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 优惠券码
     */
    private String couponCode;


}
