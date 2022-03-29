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
 * 盲盒
 * </p>
 *
 * @author simon
 * @since 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Box implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类ID
     */
    private Integer cid;

    /**
     * 盲盒名称
     */
    private String boxName;

    /**
     * 盲盒图片
     */
    private String cover;

    /**
     * 盲盒价格
     */
    private String price;

    /**
     * 盲盒标签
     */
    private String label;

    /**
     * 盲盒数量
     */
    private Integer counts;

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
     * 是否上架
     */
    private String isShelves;

    /**
     * 拼单价格
     */
    private String spellPrice;

    /**
     * 销量
     */
    private Integer sales;
}
