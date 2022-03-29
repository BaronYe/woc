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
 * 反馈信息
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Feedbacks implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 反馈ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 反馈状态 0 待处理 1 已处理 2 忽略
     */
    private String state;

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
