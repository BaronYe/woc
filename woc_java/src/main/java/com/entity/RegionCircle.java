package com.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 地区商圈
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RegionCircle implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 商圈id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商圈名称
     */
    private String name;

    /**
     * 图片
     */
    private String img;

    /**
     * 地区ID
     */
    private Integer regionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 是否显示
     */
    private String isShow;


}
