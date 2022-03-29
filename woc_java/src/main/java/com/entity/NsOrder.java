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
 * 订单管理
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsOrder implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

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
     * 支付类型 余额或微信还是积分
     */
    private Integer paymentType;

    /**
     * 订单总价
     */
    private String orderMoney;

    /**
     * 订单消耗积分
     */
    private Integer point;

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
     * 快递单号
     */
    private String shippingExpress;

    /**
     * 快递公司
     */
    private String shippingCom;

    /**
     * 订单付款时间
     */
    private LocalDateTime payTime;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 出售状态 1邮寄 2出售3暂存
     */
    private Integer isSellState;

    /**
     * 和你拼单的用户ID
     */
    private Integer tuangouUserId;

    /**
     * 与你拼单开团的订单ID
     */
    private Integer tuangouOrderId;

    /**
     * 是否拼单
     */
    private Integer isTuangou;

    /**
     * 拼单状态
     */
    private Integer tuangouState;

    /**
     * 交易单号
     */
    private String transactionId;

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
     * 拼团ID
     */
    private Integer groupId;

    /**
     * 盲盒商品ID
     */
    private Integer boxItemId;

}
