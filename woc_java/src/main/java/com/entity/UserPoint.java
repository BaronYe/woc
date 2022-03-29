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
 * 积分明细
 * </p>
 *
 * @author simon
 * @since 2021-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserPoint implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 积分id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 积分数量
     */
    private Integer num;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 类型
     */
    private String types;

    /**
     * 是否领取
     */
    private Integer isGet;

    /**
     * 正负号 0负 1正
     */
    private Integer sign;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;


}
