package com.util.pay;

/*** 
 * @Description: // TODO  微信接口链接
 * @author: songxinyong
 * @date: 2020/11/10 15:59 
 */
public class WxApi {
  /**
   * 微信统一下单
   */
  public static String pay_unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";

  /**
   * 获取用户信息
   */
  public static String jscode2session = "https://api.weixin.qq.com/sns/jscode2session";


  /**
   * 小程序获取token
   */
  public static String access_token = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token";
}
