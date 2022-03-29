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
 * 用户发布的动态
 * </p>
 *
 * @author simon
 * @since 2021-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dynamic implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 动态标题
     */
    private String title;

    /**
     * 动态文字
     */
    private String content;

    /**
     * 动态发送的地址
     */
    private String address;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 点赞数
     */
    private Long thumbsUpCounts;

    /**
     * 评论数
     */
    private Long commentCounts;

    /**
     * 审核 0 默认  1过审 2审核失败
     */
    private Integer isAudit;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 视频
     */
    private String videoUrls;

    /**
     * 0是图片 1是视频
     */
    private String tyeps;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;


}
