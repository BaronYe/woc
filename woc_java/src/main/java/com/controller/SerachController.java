package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.service.impl.ActivityServiceImpl;
import com.service.impl.ShopServiceImpl;
import com.service.impl.UsersServiceImpl;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 搜索 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@RestController
@RequestMapping("/serach")
@CrossOrigin
public class SerachController {
    @Autowired
    private ShopServiceImpl shopService;
    @Autowired
    private UsersServiceImpl userService;
    @Autowired
    private ActivityServiceImpl activityService;

    /**
     * 搜索
     * @param types
     * @param page
     * @param pagesize
     * @param serach
     * @return
     */
    @RequestMapping("/doSerach")
    public Object doSerach(String types,Integer page,Integer pagesize,String serach){
        if (Code.ZERO.equals(types)){
            Page<Shop> uPage = new Page<>(page, pagesize);
            LambdaQueryWrapper<Shop> query = new QueryWrapper<Shop>().lambda();
            if (!Code.KONG.equals(serach)){
                query.like(Shop::getName,serach);
            }
            query.eq(Shop::getMerchantState,Code.ONE);
//            query.eq(Shop::getIsClosed,Code.ONE);
            query.select(Shop::getBanner,Shop::getId,Shop::getRegionId,Shop::getCircleId,Shop::getName,
                    Shop::getPhone,Shop::getMoods,Shop::getTypes,Shop::getAddress,Shop::getIsClosed,
                    Shop::getIsFind,Shop::getStartTime,Shop::getEndTime,Shop::getMerchantId,Shop::getMerchantState);
            IPage iPage = shopService.page(uPage, query);
            List<Shop> dataList = iPage.getRecords();
            List<Object> list = new ArrayList<>();
            for (Shop data : dataList) {
                Map map = MapUnite.getMap(data);
                list.add(map);
            }
            return Result.ok(list, (int) iPage.getTotal());
        }else if (Code.ONE.equals(types)){
            Page<Activity> uPage = new Page<>(page, pagesize);
            LambdaQueryWrapper<Activity> query = new QueryWrapper<Activity>().lambda();
            query.orderByDesc(Activity::getCreateTime);
            if (!Code.KONG.equals(serach)){
                query.like(Activity::getActivityName,serach);
            }
//            query.ne(Activity::getActivityShow,Code.TWO);
            query.ne(Activity::getState, Code.TWO);
            query.select(Activity::getId,Activity::getActivityName,Activity::getActivityImages,
                    Activity::getActivityAddress,Activity::getLabel,Activity::getState,Activity::getActivityPhone,
                    Activity::getActivityStartTime,Activity::getActivityEndTime,Activity::getCreateTime,
                    Activity::getActivityShow);
            IPage iPage = activityService.page(uPage, query);
            List<Activity> usersList = iPage.getRecords();
            List<Object> list = new ArrayList<>();
            for (Activity data : usersList) {
                list.add(MapUnite.getMap(data));
            }
            return Result.ok(list, (int) iPage.getTotal());
        }
        Page<Users> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Users> query = new QueryWrapper<Users>().lambda();
        if (!Code.KONG.equals(serach)){
            query.like(Users::getNickname,serach);
        }
        query.orderByDesc(Users::getCreateTime);
        IPage iPage = userService.page(uPage, query);
        List<Users> usersList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Users user : usersList) {
            list.add(MapUnite.getObjMap(user));
        }
        return Result.ok(list, (int) iPage.getTotal());
    }
}
