package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.enums.ExceptionEnum;
import com.service.impl.*;
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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@RestController
@RequestMapping("/shop")
@CrossOrigin
public class ShopController {
    @Autowired
    private ShopServiceImpl shopService;
    @Autowired
    private RegionServiceImpl regionService;
    @Autowired
    private RegionCircleServiceImpl circleService;
    @Autowired
    private MerchantServiceImpl merchantService;
    @Autowired
    private ShopCollectServiceImpl collectService;
    @Autowired
    private CouponServiceImpl couponService;
    @Autowired
    private UserCouponServiceImpl userCouponService;

    /**
     * 添加和修改
     *
     * @param request
     * @return
     */
    @RequestMapping("/setShop")
    public Object setShop(HttpServletRequest request) {
        Shop data = (Shop) MapUnite.getEntity(Util.getJSONParam(request), Shop.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = shopService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        boolean b = shopService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delShopById")
    public Object delShopById(Integer id) {
        boolean b = shopService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/getShopById")
    public Object getShopById(Integer id,Integer userId) {
        Shop shop = shopService.getById(id);
        Map map = MapUnite.getMap(shop);
        List<Coupon> couponList = couponService.list(new QueryWrapper<Coupon>().lambda()
                .eq(Coupon::getShopId,shop.getId())
                .eq(Coupon::getIsRob,Code.ZERO)
                .eq(Coupon::getShopStatus,Code.ONE)
                .eq(Coupon::getCouponStatus,Code.ONE)
                .lt(Coupon::getStartTime,LocalDateTime.now())
                .gt(Coupon::getEndTime,LocalDateTime.now()));
        List list = new ArrayList();
        for (Coupon coupon : couponList) {
            Map cMap = MapUnite.getMap(coupon);
            cMap.put("isGet",Code.load_one);
            if (!Code.zero.equals(userId)){
                UserCoupon userCoupon = userCouponService.getOne(new QueryWrapper<UserCoupon>().lambda()
                        .eq(UserCoupon::getUserId, userId)
                        .eq(UserCoupon::getCouponId, coupon.getId()));
                if (userCoupon != null){
                    cMap.put("isGet",userCoupon.getState());
                }
            }
            list.add(cMap);
        }
        map.put("couponList",list);
        ShopCollect collect = collectService.getOne(new QueryWrapper<ShopCollect>().lambda()
                .eq(ShopCollect::getUserId, userId)
                .eq(ShopCollect::getState, Code.ONE)
                .eq(ShopCollect::getShopId,id));
        if (collect == null){
            map.put("isCollect", Code.one);
        }else{
            map.put("isCollect", Code.zero);
        }
        return Result.ok(map);
    }

    /**
     * 好去处排行榜
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/rankingShop")
    public Object rankingShop(Integer page,Integer pagesize,Integer regionId,Integer circleId){
        Page<Shop> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Shop> query = new QueryWrapper<Shop>().lambda();
        query.orderByDesc(Shop::getMoods);
        if (!Code.zero.equals(regionId)){
            query.eq(Shop::getRegionId,regionId);
        }
        if (!Code.zero.equals(circleId)){
            query.eq(Shop::getCircleId,circleId);
        }
        query.select(Shop::getBanner,Shop::getId,Shop::getRegionId,Shop::getCircleId,Shop::getName,
                Shop::getPhone,Shop::getMoods,Shop::getTypes,Shop::getAddress,Shop::getIsClosed,
                Shop::getIsFind,Shop::getStartTime,Shop::getEndTime,Shop::getMerchantId,Shop::getMerchantState,
                Shop::getPerCapita);
        IPage iPage = shopService.page(uPage, query);
        List<Shop> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Shop data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询
     * @param page
     * @param pagesize
     * @param regionId
     * @param circleId
     * @param name
     * @return
     */
    @RequestMapping("/getShop")
    public Object getShop(Integer page,Integer pagesize,Integer regionId,Integer circleId, String name,String types,
                          String isClosed){
        Page<Shop> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Shop> query = new QueryWrapper<Shop>().lambda();
        query.orderByDesc(Shop::getCreateTime);
        if (!Code.load_one.equals(regionId)){
            query.eq(Shop::getRegionId,regionId);
        }
        if (!Code.load_one.equals(circleId)){
            query.eq(Shop::getCircleId,circleId);
        }
        if (!Code.KONG.equals(name)){
            query.like(Shop::getName,name);
        }
        if (!Code.KONG.equals(isClosed)){
            query.eq(Shop::getIsClosed,isClosed);
        }
        if (!Code.LOAD_ONE.equals(types)){
            query.eq(Shop::getTypes,types);
        }
        query.select(Shop::getBanner,Shop::getId,Shop::getRegionId,Shop::getCircleId,Shop::getName,
                Shop::getPhone,Shop::getMoods,Shop::getTypes,Shop::getAddress,Shop::getIsClosed,
                Shop::getIsFind,Shop::getStartTime,Shop::getEndTime,Shop::getMerchantId,Shop::getMerchantState,
                Shop::getPerCapita);
        IPage iPage = shopService.page(uPage, query);
        List<Shop> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Shop data : dataList) {
            Map map = MapUnite.getMap(data);
            Region region = regionService.getById(data.getRegionId());
            RegionCircle circle = circleService.getById(data.getCircleId());
            map.put("region",region.getName());
            map.put("circle",circle.getName());
            if (Code.ONE.equals(data.getMerchantState())) {
                Merchant merchant = merchantService.getOne(new QueryWrapper<Merchant>().lambda()
                        .eq(Merchant::getId,data.getMerchantId())
                        .select(Merchant::getName,Merchant::getUsername,Merchant::getId));
                map.put("merchant",MapUnite.getObjMap(merchant));
            }
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 微信端获取店铺列表
     * @param page
     * @param pagesize
     * @param regionId
     * @param circleId
     * @return
     */
    @RequestMapping("/getShopDoWechat")
    public Object getShopDoWechat(Integer page,Integer pagesize,Integer regionId,Integer circleId,String types){
        Page<Shop> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Shop> query = new QueryWrapper<Shop>().lambda();
        query.orderByDesc(Shop::getCreateTime);
        if (!Code.load_one.equals(regionId)){
            query.eq(Shop::getRegionId,regionId);
        }
        if (!Code.load_one.equals(circleId)){
            query.eq(Shop::getCircleId,circleId);
        }
        if (!Code.LOAD_ONE.equals(types)){
            query.eq(Shop::getTypes,types);
        }
        query.select(Shop::getBanner,Shop::getId,Shop::getRegionId,Shop::getCircleId,Shop::getName,
                Shop::getPhone,Shop::getMoods,Shop::getTypes,Shop::getAddress,Shop::getIsClosed,Shop::getPerCapita,
                Shop::getIsFind,Shop::getStartTime,Shop::getEndTime,Shop::getMerchantId,Shop::getMerchantState);
        IPage iPage = shopService.page(uPage, query);
        List<Shop> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Shop data : dataList) {
            Map map = MapUnite.getMap(data);
            Region region = regionService.getById(data.getRegionId());
            RegionCircle circle = circleService.getById(data.getCircleId());
            map.put("region",region.getName());
            map.put("circle",circle.getName());
            if (Code.ONE.equals(data.getMerchantState())) {
                Merchant merchant = merchantService.getOne(new QueryWrapper<Merchant>().lambda()
                        .eq(Merchant::getId,data.getMerchantId())
                        .select(Merchant::getName,Merchant::getUsername,Merchant::getId));
                map.put("merchant",MapUnite.getObjMap(merchant));
            }
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 收藏
     * @param request
     * @return
     */
    @RequestMapping("/setShopCollect")
    public Object setShopCollect(HttpServletRequest request){
        ShopCollect data = (ShopCollect) MapUnite.getEntity(Util.getJSONParam(request), ShopCollect.class);
        ShopCollect collect = collectService.getOne(new QueryWrapper<ShopCollect>().lambda()
                .eq(ShopCollect::getUserId, data.getUserId())
                .eq(ShopCollect::getShopId,data.getShopId()));
        if (collect == null){
            data.setState(Code.ONE);
            data.setCreateTime(LocalDateTime.now());
            boolean b = collectService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.OPERATE_INFO);
        }
        data.setId(collect.getId());
        if (Code.ZERO.equals(collect.getState())){
            data.setState(Code.ONE);
        }else{
            data.setState(Code.ZERO);
        }
        boolean b = collectService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 我收藏的店铺
     * @param userId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getShopCollectByUid")
    public Object getShopCollectByUid(@NotNull(message = "用户ID不能为空") Integer userId,
                                          Integer page,Integer pagesize){
        Page<ShopCollect> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<ShopCollect> query = new QueryWrapper<ShopCollect>().lambda();
        query.orderByDesc(ShopCollect::getCreateTime);
        query.eq(ShopCollect::getUserId,userId);
        IPage iPage = collectService.page(uPage, query);
        List<ShopCollect> usersList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (ShopCollect data : usersList) {
            Map map = MapUnite.getMap(data);
            Shop shop = shopService.getOne(new QueryWrapper<Shop>().lambda()
                    .eq(Shop::getId,data.getShopId())
                    .select(Shop::getBanner,Shop::getId,Shop::getRegionId,Shop::getCircleId,Shop::getName,
                            Shop::getPhone,Shop::getMoods,Shop::getTypes,Shop::getAddress,Shop::getIsClosed,Shop::getPerCapita,
                            Shop::getIsFind,Shop::getStartTime,Shop::getEndTime,Shop::getMerchantId,Shop::getMerchantState));
            map.put("shop", MapUnite.getMap(shop));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }



}

