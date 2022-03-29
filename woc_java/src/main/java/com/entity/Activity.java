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
 * 活动
 * </p>
 *
 * @author simon
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Activity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 活动id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名字
     */
    private String activityName;

    /**
     * 活动图片地址
     */
    private String activityImages;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动地点
     */
    private String activityAddress;

    /**
     * 活动联系方式
     */
    private String activityPhone;

    /**
     * 活动结束时间
     */
    private String activityEndTime;

    /**
     * 活动开始时间
     */
    private String activityStartTime;

    /**
     * 0：不显示 ，1 显示
     */
    private String activityShow;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 人均
     */
    private String perCapita;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 标签
     */
    private String label;

    /**
     * 活动状态 2 已结束 1 进行中 0未开始
     */
    private String state;
}
