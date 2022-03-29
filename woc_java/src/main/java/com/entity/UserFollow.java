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
 * 我的关注用户
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserFollow implements Serializable {

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
     * 被关注用户id
     */
    private Integer followId;

    /**
     * 状态
     */
    private String state;

    /**
     * 是否查看
     */
    private String isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
