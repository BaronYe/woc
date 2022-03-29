package com.util.pay;

import lombok.Data;

/**
 * 描述:
 * 微信支付实体类
 *
 * @author songxinyong
 * @ClassName WxPayConfiguration
 * @date 2020-01-02 13:19
 */
@Data
public class WxPayConfiguration {
    /**
     * AppId
     */
    private String appid;

    /**
     *  商户号ID
     */
    private String mchid;

    /**
     *  商户秘钥
     */
    private String mchsecret;

    /**
     * 时间戳
     */
    private String nonceStr;

    /**
     *  内容
     */
    private String body;

    /**
     * 订单编号
     */
    private String outTradeNo;

    /**
     * 金额（分）
     */
    private Integer totalFee;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 参数
     */
    private String attach;

    /**
     * 支付类型
     */
    private String tradeType;

    /**
     * openid
     */
    private  String openid;

}
