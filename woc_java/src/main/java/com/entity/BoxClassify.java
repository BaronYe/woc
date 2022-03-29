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
 * 盲盒分类
 * </p>
 *
 * @author simon
 * @since 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BoxClassify implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 盲盒分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
