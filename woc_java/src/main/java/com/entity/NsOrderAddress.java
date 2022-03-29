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
 * 订单地址管理
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsOrderAddress implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单地址ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 地址ID
     */
    private Integer addressId;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 收货人的手机号码
     */
    private String receiverMobile;

    /**
     * 收货人所在省
     */
    private String receiverProvince;

    /**
     * 收货人所在城市
     */
    private String receiverCity;

    /**
     * 收货人所在街道
     */
    private String receiverRegion;

    /**
     * 收货人详细地址
     */
    private String receiverDetailed;

    /**
     * 收货人邮编
     */
    private String receiverZip;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
