package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Coupon;
import com.entity.Shop;
import com.entity.UserCoupon;
import com.enums.ExceptionEnum;
import com.service.impl.CouponServiceImpl;
import com.service.impl.ShopServiceImpl;
import com.service.impl.UserCouponServiceImpl;
import com.tool.Token;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import com.util.Util;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-18
 */
@RestController
@RequestMapping("/coupon")
@CrossOrigin
public class CouponController {
    @Autowired
    private CouponServiceImpl couponService;
    @Autowired
    private ShopServiceImpl shopService;
    @Autowired
    private UserCouponServiceImpl userCouponService;

    /**
     * 添加和修改
     * @param request
     * @return
     */
    @RequestMapping("/setCoupon")
    @Token.LoginToken
    public Object setCoupon(HttpServletRequest request) throws ParseException {
        Coupon data = (Coupon) MapUnite.getEntity(Util.getJSONParam(request), Coupon.class);
        data.setStartTime(LocalDateTime.parse(String.valueOf(data.getSTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        data.setEndTime(LocalDateTime.parse(String.valueOf(data.getETime()), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        if(!Util.toCompareTime(data.getSTime())){
            if (!Util.toCompareTime(data.getETime())){
                data.setCouponStatus(Code.ZERO);
            }else{
                data.setCouponStatus(Code.ONE);
            }
        }else{
            data.setCouponStatus(Code.TWO);
        }

        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = couponService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = couponService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 关闭优惠券
     * @param id
     * @return
     */
    @RequestMapping("/offCouponById")
    @Token.LoginToken
    public Object offCouponById(Integer id){
        boolean b = couponService.updateById(new Coupon().setCouponStatus(Code.ZERO).setId(id));
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 获取优惠券
     * @param page
     * @param pagesize
     * @param name
     * @return
     */
    @RequestMapping("/getCoupon")
    public Object getCoupon(Integer page,Integer pagesize,String name,String types,
                            String startTime, String endTime,
                            String state, String shopStatus,String isRob){
        Page<Coupon> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Coupon> query = new QueryWrapper<Coupon>().lambda();
        if (!Code.KONG.equals(name)){
            query.like(Coupon::getTitle,name);
        }
        if (!Code.KONG.equals(types)){
            query.eq(Coupon::getTypes,types);
        }
        if (!Code.KONG.equals(startTime)){
            query.ge(Coupon::getStartTime,startTime).lt(Coupon::getEndTime,endTime);
        }
        if (!Code.KONG.equals(state)){
            query.eq(Coupon::getCouponStatus,state);
        }
        if (!Code.KONG.equals(shopStatus)){
            query.eq(Coupon::getShopStatus,shopStatus);
        }
        if (!Code.KONG.equals(isRob)){
            query.eq(Coupon::getIsRob,isRob);
        }
        IPage iPage = couponService.page(uPage, query);
        List<Coupon> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Coupon data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询通用券
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getCouponDoSpecial")
    public Object getCouponDoSpecial(Integer page,Integer pagesize,Integer userId){
        Page<Coupon> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Coupon> query = new QueryWrapper<Coupon>().lambda();
        query.eq(Coupon::getShopStatus,Code.ZERO);
        query.eq(Coupon::getCouponStatus,Code.ONE);
        query.lt(Coupon::getStartTime,LocalDateTime.now());
        query.gt(Coupon::getEndTime,LocalDateTime.now());
        IPage iPage = couponService.page(uPage, query);
        List<Coupon> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Coupon data : dataList) {
            Map map = MapUnite.getMap(data);
            map.put("isGet",Code.load_one);
            if (!Code.zero.equals(userId)){
                UserCoupon userCoupon = userCouponService.getOne(new QueryWrapper<UserCoupon>().lambda()
                        .eq(UserCoupon::getUserId, userId)
                        .eq(UserCoupon::getCouponId, data.getId()));
                if (userCoupon != null){
                    map.put("isGet",userCoupon.getState());
                }
            }
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询限时专用券
     * @return
     */
    @RequestMapping("/getCouponDoRob")
    public Object getCouponDoRob(Integer userId){
        LambdaQueryWrapper<Coupon> query = new QueryWrapper<Coupon>().lambda();
        query.eq(Coupon::getShopStatus,Code.ONE);
        query.eq(Coupon::getCouponStatus,Code.ONE);
        query.eq(Coupon::getIsRob,Code.ONE);
        query.lt(Coupon::getStartTime,LocalDateTime.now());
        query.gt(Coupon::getEndTime,LocalDateTime.now());
        List<Coupon> dataList = couponService.list(query);
        List<Object> list = new ArrayList<>();
        for (Coupon data : dataList) {
            Map map = MapUnite.getMap(data);
            Shop shop = shopService.getOne(new QueryWrapper<Shop>().lambda()
                                        .eq(Shop::getId, data.getShopId())
                                        .select(Shop::getName,Shop::getAddress,Shop::getBanner));
            map.put("shop",MapUnite.getMap(shop));
            map.put("isGet",Code.load_one);
            if (!Code.zero.equals(userId)){
                UserCoupon userCoupon = userCouponService.getOne(new QueryWrapper<UserCoupon>().lambda()
                        .eq(UserCoupon::getUserId, userId)
                        .eq(UserCoupon::getCouponId, data.getId()));
                if (userCoupon != null){
                    map.put("isGet",userCoupon.getState());
                }
            }
            list.add(map);
        }
        return Result.ok(list);
    }

    /**
     * 吃喝跟游玩类的优惠券查询
     * @param page
     * @param pagesize
     * @param types
     * @return
     */
    @RequestMapping("/getCouponDoTyps")
    public Object getCouponDoTyps(Integer page,Integer pagesize,String types,Integer userId){
        Page<Coupon> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Coupon> query = new QueryWrapper<Coupon>().lambda();
        query.eq(Coupon::getShopStatus,Code.ONE);
        query.eq(Coupon::getCouponStatus,Code.ONE);
        query.lt(Coupon::getStartTime,LocalDateTime.now());
        query.gt(Coupon::getEndTime,LocalDateTime.now());
        query.eq(Coupon::getShopType,types);
        IPage iPage = couponService.page(uPage, query);
        List<Coupon> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Coupon data : dataList) {
            Map map = MapUnite.getMap(data);
            map.put("isGet",Code.load_one);
            if (!Code.zero.equals(userId)){
                UserCoupon userCoupon = userCouponService.getOne(new QueryWrapper<UserCoupon>().lambda()
                        .eq(UserCoupon::getUserId, userId)
                        .eq(UserCoupon::getCouponId, data.getId()));
                if (userCoupon != null){
                    map.put("isGet",userCoupon.getState());
                }
            }
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询优惠券详情
     * @param id
     * @return
     */
    @RequestMapping("/getCouponById")
    public Object getCouponById(Integer id){
        Coupon coupon = couponService.getById(id);
        return Result.ok(MapUnite.getMap(coupon));
    }

    /**
     * 删除优惠券
     * @param id
     * @return
     */
    @RequestMapping("/delCouponById")
    @Token.LoginToken
    public Object delCouponById(Integer id){
        boolean b = couponService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

}

