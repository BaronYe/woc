package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.CouponWriteOff;
import com.entity.Dynamic;
import com.entity.UserCoupon;
import com.enums.ExceptionEnum;
import com.service.impl.CouponWriteOffServiceImpl;
import com.service.impl.UserCouponServiceImpl;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import com.util.Util;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券核销列表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-07-07
 */
@RestController
@RequestMapping("/couponWriteOff")
@CrossOrigin
public class CouponWriteOffController {
    @Autowired
    private CouponWriteOffServiceImpl couponWriteOffService;
    @Autowired
    private UserCouponServiceImpl userCouponService;

    /**
     * 优惠券核销
     * @param id
     * @param shopId
     * @param merchantId
     * @return
     * @throws ParseException
     */
    @RequestMapping("/couponWriteOff")
    public Object couponWriteOff(Integer id,Integer shopId,Integer merchantId) throws ParseException {
        Map map = new HashMap();
        CouponWriteOff couponWriteOff = new CouponWriteOff();
        UserCoupon userCoupon = userCouponService.getById(id);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 判断该优惠券有没有失效
        if(!Util.toCompareTime(df.format(userCoupon.getEndTime()))){
            couponWriteOff.setTypes(Code.zero);
            couponWriteOff.setErrorText("该优惠券已失效");
        }else if (!Code.ONE.equals(userCoupon.getState())){// 该优惠券有没有被使用
            couponWriteOff.setTypes(Code.zero);
            couponWriteOff.setErrorText("该优惠券已被使用");
        }else{
            couponWriteOff.setTypes(Code.one);
        }
        // 判断使用优惠券的类型 折扣还是满减
        if(Code.ONE.equals(userCoupon.getCouponTypes())){
            couponWriteOff.setTitle(userCoupon.getCouponDiscount() + "折扣券");
        }else{
            couponWriteOff.setTitle(userCoupon.getCouponPrice() + "元优惠券");
        }
        couponWriteOff.setCouponCode(userCoupon.getCouponCode());
        couponWriteOff.setCouponId(userCoupon.getCouponId());
        couponWriteOff.setUserCouponId(userCoupon.getId());
        couponWriteOff.setCreateTime(LocalDateTime.now());
        couponWriteOff.setMerchantId(merchantId);
        couponWriteOff.setShopId(shopId);
        couponWriteOff.setUserId(userCoupon.getUserId());

        boolean b = couponWriteOffService.save(couponWriteOff);
        if (b) {
            if (Code.one.equals(couponWriteOff.getTypes())){
                userCouponService.updateById(new UserCoupon().setId(userCoupon.getId()).setState(Code.ONE));
            }
            map.put("userCoupon",userCoupon);
            map.put("couponWriteOff",couponWriteOff);
            return Result.ok(map);
        }
        throw new MyException(ExceptionEnum.ADD_INFO);
    }

    /**
     * 核销明细
     * @param types
     * @param page
     * @param pagesize
     * @param shopId
     * @param merchantId
     * @return
     */
    @RequestMapping("/getCouponWriteOff")
    public Object getCouponWriteOff(Integer types,Integer page,Integer pagesize,Integer shopId,Integer merchantId){
        Page<CouponWriteOff> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<CouponWriteOff> query = new QueryWrapper<CouponWriteOff>().lambda();
        query.orderByDesc(CouponWriteOff::getCreateTime);
        if (!Code.load_one.equals(types)){
            query.eq(CouponWriteOff::getTypes,types);
        }
        if (!Code.load_one.equals(shopId)){
            query.eq(CouponWriteOff::getShopId,shopId);
        }
        if (!Code.load_one.equals(merchantId)){
            query.eq(CouponWriteOff::getMerchantId,merchantId);
        }
        IPage iPage = couponWriteOffService.page(uPage, query);
        List<CouponWriteOff> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (CouponWriteOff writeOff : dataList) {
            list.add(MapUnite.getObjMap(writeOff));
        }
        return Result.ok(list,(int)iPage.getTotal());
    }
}

