package com.enums;

import com.util.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/***
 * @Description: //TODO 错误返回信息枚举类
 * @param:
 * @author: songxinyong
 * @date: 2020/11/4 22:54
 * @return:
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {

    UPDATE_INFO(Code.FAIL,"修改失败"),
    DELETE_INFO(Code.FAIL,"删除失败"),
    ADD_INFO(Code.FAIL,"添加失败"),
    FIND_INFO(Code.FAIL,"查询失败"),

    CANCEL_INFO(Code.FAIL,"取消失败"),
    OPERATE_INFO(Code.FAIL,"操作失败"),

    PARAM_INFO(Code.FAIL,"参数出错"),

    DATA_NULL(Code.FAIL,"数据不存在"),
    DATA_EXIST(Code.FAIL,"数据已存在"),

    ACCOUNT_EXIST(Code.FAIL,"账号已存在"),
    ACCOUNT_ERROR(Code.FAIL,"账号错误"),
    PASSWORD_ERROR(Code.FAIL,"密码错误"),
    USER_NULL(Code.FAIL,"用户不存在"),
    USER_EXIST(Code.FAIL,"用户已存在"),
    USER_REGISTER_ERROR(Code.FAIL,"用户注册失败"),
    USER_BANLANCE_ERROR(Code.FAIL,"您的余额不足"),

    APPID_ERROR(Code.FAIL,"小程序AppID或秘钥错误"),
    APPID_NULL(Code.FAIL,"小程序AppID或秘钥没有填写"),
    SHOUQUAN_ERROR(Code.FAIL,"授权失败，请重新授权！"),
    OPENID_ERROR(Code.FAIL,"用户openid为空！"),

    ADMIN_NULL(Code.ROUTE_ERROR,"用户身份不存在，请重新登录"),
    TOKEN_ERROR(Code.ROUTE_ERROR,"无效的token,请重新登录"),
    TOKEN_NULL(Code.ROUTE_ERROR,"无token，请重新登录"),

    FILE_ERROR(Code.FAIL,"文件上传失败"),
    FILE_MAX_50(Code.FAIL,"上传文件大小不能超过50M"),

    SHOP_NOT(Code.FAIL,"该店铺已经被绑定了，不能重复绑定"),

    COUPON_NOT(Code.FAIL,"该优惠券不存在或该优惠券被删除了"),
    COUPON_TIME_OVER(Code.FAIL,"该优惠券领取时间已过"),
    COUPON_USER_OVER(Code.FAIL,"该优惠券已失效"),
    TO_COUPON_ERROE(Code.FAIL,"优惠券领取失败"),
    COUPON_COUNT_ERROE(Code.FAIL,"优惠券已被领取完"),
    COUPON_STATE(Code.FAIL,"优惠券已被使用"),
    COUPON_USER_LINGQU(Code.FAIL,"优惠券已被领取"),

    ITEM_NOT(Code.FAIL,"盲盒库存不足"),

    BALANCE_NOT_ENOUGH(Code.FAIL,"您的余额未达到最低提现金额"),
    CASHOUT_ERROR(Code.FAIL,"申请提现失败"),

    GET_POINT_ERROR(Code.FAIL,"积分领取失败"),
    POINT_NOT(Code.FAIL,"您的积分不足以兑换该物品"),
    POINT_MAX(Code.FAIL,"兑换该物品已上限"),

    ORDER_ERROR(Code.FAIL,"订单创建失败"),
    ORDER_NULL(Code.FAIL,"订单不存在"),
    SELL_NULL(Code.FAIL,"该商品已经被出售了"),

    ADDRESS_NOT(Code.FAIL,"请先选择地址"),
    NSORDER_NULL(Code.FAIL,"该在售订单已被删除，请联系管理员"),


    BOX_NO_ITEM(Code.FAIL,"盲盒开启失败"),
    BOX_ITEM_NOT(Code.FAIL,"盲盒已开启过了"),

    FILE_NULL(Code.FAIL,"文件不能为空");

    private Integer code;
    private String msg;

}
