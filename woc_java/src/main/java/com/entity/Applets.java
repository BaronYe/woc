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
 * 小程序信息配置
 * </p>
 *
 * @author simon
 * @since 2020-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Applets implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小程序appid
     */
    private String appId;

    /**
     * 小程序秘钥
     */
    private String appSecret;

    /**
     * 商户id
     */
    private String payId;

    /**
     * 商户秘钥
     */
    private String paySecret;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
