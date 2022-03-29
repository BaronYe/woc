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
 * 盲盒商城订单
 * </p>
 *
 * @author simon
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsOrderSellOrder implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盲盒商城订单
     */
    private Integer sellId;

    /**
     * 出售者订单ID
     */
    private Integer nsOrderId;

    /**
     * 买家id
     */
    private Integer userId;

    /**
     * 盲盒ID
     */
    private Integer boxId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付类型 余额或微信
     */
    private Integer paymentType;

    /**
     * 订单总价
     */
    private String orderMoney;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单付款状态
     */
    private Integer payStatus;

    /**
     * 订单配送状态
     */
    private Integer shippingStatus;

    /**
     * 快递公司
     */
    private String shippingCom;

    /**
     * 快递单号
     */
    private String shippingExpress;

    /**
     * 订单付款时间
     */
    private LocalDateTime payTime;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 交易单号
     */
    private String transactionId;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 盲盒名称
     */
    private String boxName;

    /**
     * 盲盒图片
     */
    private String boxPicture;

    /**
     * 盲盒数量
     */
    private Integer boxNum;

    /**
     * 盲盒商品ID
     */
    private Integer boxItemId;

    @TableField(exist = false)
    private Integer addressId;
}
