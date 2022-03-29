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
 * 优惠券核销列表
 * </p>
 *
 * @author simon
 * @since 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouponWriteOff implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 状态
     */
    private Integer types;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 我的优惠券ID
     */
    private Integer userCouponId;

    /**
     * 优惠券ID
     */
    private Integer couponId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     * 商户ID
     */
    private Integer merchantId;

    /**
     * 金额
     */
    private String money;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 失败原因
     */
    private String errorText;
}
