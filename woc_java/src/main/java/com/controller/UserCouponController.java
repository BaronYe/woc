package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Applets;
import com.entity.Coupon;
import com.entity.UserCoupon;
import com.entity.Users;
import com.enums.ExceptionEnum;
import com.service.impl.AppletsServiceImpl;
import com.service.impl.CouponServiceImpl;
import com.service.impl.UserCouponServiceImpl;
import com.service.impl.UsersServiceImpl;
import com.util.*;
import com.util.pay.WxApi;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 我的优惠券 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/userCoupon")
@CrossOrigin
public class UserCouponController {

    @Autowired
    private UserCouponServiceImpl userCouponService;
    @Autowired
    private CouponServiceImpl couponService;
    @Autowired
    private AppletsServiceImpl appletsService;
    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 领取优惠券
     * @param userId
     * @param couponId
     * @return
     * @throws ParseException
     */
    @RequestMapping("/doUserCoupon")
    public Object doUserCoupon(@NotNull(message = "用户ID不能为空") Integer userId,
                               @NotNull(message = "优惠券ID不能为空") Integer couponId) throws ParseException {
        UserCoupon userCoupon = userCouponService.getOne(new QueryWrapper<UserCoupon>().lambda()
                                                    .eq(UserCoupon::getUserId, userId)
                                                    .eq(UserCoupon::getCouponId, couponId));
        if (userCoupon != null){
            throw new MyException(ExceptionEnum.COUPON_USER_LINGQU);
        }
        Coupon coupon = couponService.getById(couponId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (coupon == null){
            throw new MyException(ExceptionEnum.COUPON_NOT);
        }
        if(!Util.toCompareTime(df.format(coupon.getEndTime()))){
            throw new MyException(ExceptionEnum.COUPON_TIME_OVER);
        }
        if (coupon.getTotalCount() <= coupon.getToCount()){
            throw new MyException(ExceptionEnum.COUPON_COUNT_ERROE);
        }
        LocalDateTime time;
        if (Code.ZERO.equals(coupon.getTimeStatus())){
            time = LocalDateTime.parse(coupon.getCouponTime(),df);
        }else{
            time = LocalDateTime.now().plusDays(coupon.getCouponDays());
        }
        UserCoupon uCoupon = new UserCoupon()
                            .setUserId(userId)
                            .setCouponId(couponId)
                            .setCouponTitle(coupon.getTitle())
                            .setCouponDiscount(coupon.getDiscount())
                            .setCouponPrice(coupon.getCouponPrice())
                            .setCouponTypes(coupon.getTypes())
                            .setCouponUseMinPrice(coupon.getUseMinPrice())
                            .setShopId(coupon.getShopId())
                            .setShopName(coupon.getShopName())
                            .setShopStatus(coupon.getShopStatus())
                            .setState(Code.ZERO)
                            .setStartTime(LocalDateTime.now())
                            .setCouponCode(Util.getRandom())
                            .setEndTime(time);
        boolean b = userCouponService.save(uCoupon);
        if (b) {
            couponService.updateById(new Coupon()
                                    .setId(coupon.getId())
                                    .setToCount(coupon.getToCount() + Code.one));
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.TO_COUPON_ERROE);
    }

    /**
     * 我的优惠可使用与不可使用的数量统计
     * @param userId
     * @return
     */
    @RequestMapping("/totalUserCoupon")
    public Object totalUserCoupon(@NotNull(message = "用户ID不能为空")  Integer userId){
        int can = userCouponService.count(new QueryWrapper<UserCoupon>().lambda().eq(UserCoupon::getState, Code.ZERO));
        int noCan = userCouponService.count(new QueryWrapper<UserCoupon>().lambda().ne(UserCoupon::getState, Code.ZERO));
        Map map = new HashMap();
        map.put("can",can);
        map.put("noCan",noCan);
        return Result.ok(map);
    }

    /**
     * 查看我的优惠券
     * @param userId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getUserCoupon")
    public Object getUserCoupon(@NotNull(message = "用户ID不能为空") Integer userId,
                                Integer page,
                                Integer pagesize,
                                String types){
        Page<UserCoupon> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<UserCoupon>().lambda();
        queryWrapper.eq(UserCoupon::getUserId,userId);
        if (Code.ZERO.equals(types)){
            queryWrapper.eq(UserCoupon::getState,Code.ZERO);
        }else if (Code.ONE.equals(types)){
            queryWrapper.ne(UserCoupon::getState,Code.ZERO);
        }
        IPage iPage = userCouponService.page(uPage, queryWrapper);
        List<UserCoupon> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (UserCoupon user : dataList) {
            list.add(MapUnite.getMap(user));
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查看优惠券的领取情况
     * @param couponId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getUserCouponToCouponId")
    public Object getUserCouponToCouponId(@NotNull(message = "优惠券ID不能为空") Integer couponId,
                                          Integer page,Integer pagesize){
        Page<UserCoupon> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<UserCoupon>().lambda();
        queryWrapper.eq(UserCoupon::getCouponId,couponId);
        IPage iPage = userCouponService.page(uPage, queryWrapper);
        List<UserCoupon> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (UserCoupon data : dataList) {
            Map map = MapUnite.getMap(data);
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 删除优惠券
     * @param id
     * @return
     */
    @RequestMapping("/delUserCouponById")
    public Object delUserCouponById(@NotNull(message = "ID不能为空") Integer id){
        boolean b = userCouponService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 使用优惠券
     * @param id
     * @return
     * @throws ParseException
     */
    @RequestMapping("/useUserCoupon")
    public Object useUserCoupon(@NotNull(message = "ID不能为空") Integer id) throws ParseException {
        UserCoupon userCoupon = userCouponService.getById(id);
        if(userCoupon == null){
            throw new MyException(ExceptionEnum.COUPON_NOT);
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if(!Util.toCompareTime(df.format(userCoupon.getEndTime()))){
            throw new MyException(ExceptionEnum.COUPON_USER_OVER);
        }
        Applets applets = appletsService.getById(1);
        if (applets.getAppId().equals("") || applets.getAppSecret().equals("")) {
            throw new MyException(ExceptionEnum.APPID_NULL);
        }
        String tok = CodeClient.getToken(applets.getAppId(),applets.getAppSecret());
        String url= WxApi.access_token + "=" + tok;
        String scene = String.format("id=%s", userCoupon.getId());
        String path = "pages/index/index";
        String qrcoreUrl = CodeClient.sendPost3(url, scene, path);
        return Result.ok(qrcoreUrl);
    }


}

