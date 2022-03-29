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
 * 二级菜单栏（后台的导航栏）
 * </p>
 *
 * @author simon
 * @since 2021-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MenuBarSon implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 二级菜单栏id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单栏
     */
    private String name;

    /**
     * 一级菜单栏id
     */
    private Integer menuBarId;

    /**
     * 跳转路径
     */
    private String link;

    /**
     * 小图标
     */
    private String icon;

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
