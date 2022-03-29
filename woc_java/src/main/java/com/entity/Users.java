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
 * 用户数据表
 * </p>
 *
 * @author simon
 * @since 2020-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Users implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户所在地地址
     */
    private String localAddress;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 唯一标识
     */
    private String openId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     *更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private String isDelete;

    /**
     * union_id
     */
    private String unionId;

    /**
     * 用户生日
     */
    private String birthday;

    /**
     * 用户简介
     */
    private String userDesc;

    /**
     * 总积分
     */
    private Integer point;

    /**
     * 余额
     */
    private String balance;

    /**
     * 邀请人ID
     */
    private Integer  inviterId;

    /**
     * 不映射到数据库字段
     */
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String encryptedData;
    @TableField(exist = false)
    private String iv;
}
