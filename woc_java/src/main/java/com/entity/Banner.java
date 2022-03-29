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
 * 轮播图数据表
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Banner implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片路径
     */
    private String img;

    /**
     * 0是跳转链接 1是跳转活动详情
     */
    private String type;

    /**
     * 公众号链接
     */
    private String link;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 是否删除
     */
    private String isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
