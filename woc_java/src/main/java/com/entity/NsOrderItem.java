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
 * 订单盲盒表
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsOrderItem implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单项ID
     */
    @TableId(value = "order_item_id", type = IdType.AUTO)
    private Integer orderItemId;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 盲盒ID
     */
    private Integer boxId;

    /**
     * 盲盒名称
     */
    private String boxName;

    /**
     * 价格
     */
    private String price;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 盲盒总价
     */
    private String boxMoney;

    /**
     * 盲盒图片
     */
    private String boxPicture;

    /**
     * 购买人ID
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;


}
