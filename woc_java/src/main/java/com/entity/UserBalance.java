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
 * 我的余额明细
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserBalance implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 余额id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 金额
     */
    private String price;

    /**
     * 金额的状态 0 为减少 1为增加
     */
    private String state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否提现 0不是提现记录
     */
    private String isCashOut;

    /**
     * 兑现码
     */
    private String cashOutCode;

    /**
     * 提现手续费
     */
    private String cashOutPoundage;
    /**
     *凭证
     */
    private String voucher;
    /**
     * 说明
     */
    private String explains;
    /**
     * 提现状态
     */
    private String cashOutState;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;


}
