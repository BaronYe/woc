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
 * 成团列表
 * </p>
 *
 * @author simon
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NsOrderGroupList implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 拼团状态 0 未成团 1已成团
     */
    private Integer state;

    /**
     * 成团人数
     */
    private Integer num;

    /**
     * 剩余人数
     */
    private Integer remainNum;

    /**
     * 发团人ID
     */
    private Integer mainId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是不是自己开团。0 自己开团 !0 衍生开团
     */
    private Integer fromId;

    /**
     * 盲盒ID
     */
    private Integer boxId;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;


}
