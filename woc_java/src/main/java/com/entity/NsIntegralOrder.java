package com.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分商城订单
 * </p>
 *
 * @author simon
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsIntegralOrder implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 积分商品ID
     */
    private Integer integralGoodsId;

    /**
     * 积分商品名称
     */
    private String integralGoodsName;

    /**
     * 积分商品图片
     */
    private String integralGoodsPicture;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 积分数量
     */
    private Integer point;

    /**
     * 订单状态
     */
    private Integer orderStatus;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    @TableField(exist = false)
    private Integer addressId;
}
