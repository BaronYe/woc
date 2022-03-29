package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 基础配置
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Configs implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 基础配置ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客服二维码
     */
    private String wechatCode;

    /**
     * 最低提现额度
     */
    private Integer cashOutMoney;

    /**
     * 提现手续费比率
     */
    private Integer cashOutPoundage;

    /**
     * 多少积分抵扣1元
     */
    private Integer integralDeduction;

    /**
     * 积分自定义名称
     */
    private String integralName;
    /**
     * 注册获得积分
     */
    private Integer registerIntegralNum;
    /**
     * 每天登陆获得积分
     */
    private Integer loginIntegralNum;
    /**
     * 消费1元等多少积分
     */
    private String consumeIntegralNum;
    /**
     * 完善信息获得积分
     */
    private Integer perfectIntegralNum;
    /**
     * 每天发布动态获得积分
     */
    private Integer dynamicIntegralNum;
    /**
     * 邀请好友获得积分
     */
    private Integer inviteIntegralNum;
    /**
     * 分享小程序获得积分
     */
    private Integer shareIntegralNum;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
