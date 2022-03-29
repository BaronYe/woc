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
 * 店铺
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Shop implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 店铺id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 与商户绑定状态 0 未绑定 1 已绑定
     */
    private String merchantState;

    /**
     * 店铺名字
     */
    private String name;

    /**
     * 店铺图片轮播图
     */
    private String banner;

    /**
     * 店铺描述
     */
    private String shopDesc;

    /**
     * 店铺地点
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 店铺联系方式
     */
    private String phone;

    /**
     * 0：不在 ，1 在
     */
    private String pick;

    /**
     * 0：不在 ，1 在 在发现页展示
     */
    private String isFind;

    /**
     * 0：不在 ，1~9 显示排行榜里的位置在
     */
    private Integer rank;

    /**
     * 0：不显示 ，1 显示
     */
    private String isShow;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 开始时间 如：09:00
     */
    private String startTime;

    /**
     * 截止时间
     */
    private String endTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 类型， 0吃喝类 1游玩类
     */
    private String types;

    /**
     * 人气
     */
    private Integer moods;

    /**
     * 地区id
     */
    private Integer regionId;

    /**
     * 商圈id
     */
    private Integer circleId;

    /**
     * 是否停业
     */
    private String isClosed;
    /**
     * 人均
     */
    private String perCapita;

}
