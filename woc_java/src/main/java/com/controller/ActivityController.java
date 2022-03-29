package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Activity;
import com.entity.ActivityCollect;
import com.entity.DynamicZan;
import com.entity.ShopCollect;
import com.enums.ExceptionEnum;
import com.service.impl.ActivityCollectServiceImpl;
import com.service.impl.ActivityServiceImpl;
import com.tool.Token;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import com.util.Util;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 活动 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-07
 */
@RestController
@RequestMapping("/activity")
@CrossOrigin
public class ActivityController {
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private ActivityCollectServiceImpl collectService;

    /**
     * 活动的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setActivity")
    @Token.LoginToken
    public Object setActivity(HttpServletRequest request) throws ParseException {
        Activity data = (Activity) MapUnite.getEntity(Util.getJSONParam(request), Activity.class);
        if(!Util.toCompareTime(data.getActivityStartTime())){
            if (!Util.toCompareTime(data.getActivityEndTime())){
                data.setState(Code.TWO);
            }else{
                data.setState(Code.ONE);
            }
        }else{
            data.setState(Code.ZERO);
        }
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = activityService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        boolean b = activityService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询活动列表
     * @param page
     * @param pagesize
     * @param activityShow
     * @return
     */
    @RequestMapping("/getActivity")
    @Token.PassToken
    public Object getActivity(Integer page,Integer pagesize,String activityShow, String name){
        Page<Activity> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Activity> query = new QueryWrapper<Activity>().lambda();
        query.orderByDesc(Activity::getCreateTime);
        if (!Code.LOAD_ONE.equals(activityShow)){
            query.eq(Activity::getActivityShow,activityShow);
        }
        if (!Code.KONG.equals(name)){
            query.like(Activity::getActivityName,name);
        }
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

    /**
     * 查询详情
     * @param id
     * @return
     */
    @RequestMapping("/getActivityById")
    @Token.PassToken
    public Object getActivityById(Integer id,Integer userId){
        Activity data = activityService.getById(id);
        Map map = MapUnite.getMap(data);
        // 判断用户有没有收藏
        ActivityCollect collect = collectService.getOne(new QueryWrapper<ActivityCollect>().lambda()
                .eq(ActivityCollect::getUserId,userId)
                .eq(ActivityCollect::getState,Code.ONE)
                .eq(ActivityCollect::getActivityId, id));
        if (collect == null){
            map.put("isCollect", Code.zero);
        }else{
            map.put("isCollect", Code.one);
        }
        // 关注人数及收藏人数
        int i = collectService.count(new QueryWrapper<ActivityCollect>().lambda()
                .eq(ActivityCollect::getActivityId, id).eq(ActivityCollect::getState,Code.ONE));
        map.put("collectNum",i);
        return Result.ok(map);
    }

    /**
     * 删除活动
     * @param id
     * @return
     */
    @RequestMapping("/delActivityById")
    @Token.LoginToken
    public Object delActivityById(Integer id){
        boolean b = activityService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 收藏
     * @param request
     * @return
     */
    @RequestMapping("/setActivityCollect")
    public Object setActivityCollect(HttpServletRequest request){
        ActivityCollect data = (ActivityCollect) MapUnite.getEntity(Util.getJSONParam(request), ActivityCollect.class);
        ActivityCollect collect = collectService.getOne(new QueryWrapper<ActivityCollect>().lambda()
                .eq(ActivityCollect::getUserId, data.getUserId())
                .eq(ActivityCollect::getActivityId,data.getActivityId()));
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
     * 我收藏的活动
     * @param userId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getActivityCollectByUid")
    public Object getActivityCollectByUid(@NotNull(message = "用户ID不能为空") Integer userId,
                                          Integer page,Integer pagesize){
        Page<ActivityCollect> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<ActivityCollect> query = new QueryWrapper<ActivityCollect>().lambda();
        query.orderByDesc(ActivityCollect::getCreateTime);
        query.eq(ActivityCollect::getState, Code.ONE);
        query.eq(ActivityCollect::getUserId,userId);
        IPage iPage = collectService.page(uPage, query);
        List<ActivityCollect> usersList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (ActivityCollect data : usersList) {
            Map map = MapUnite.getMap(data);
            Activity activity = activityService.getOne(new QueryWrapper<Activity>().lambda()
                    .eq(Activity::getId,data.getActivityId())
                    .select(Activity::getId,Activity::getActivityName,Activity::getActivityImages,
                    Activity::getActivityAddress,Activity::getLabel,Activity::getState,Activity::getActivityPhone,
                    Activity::getActivityStartTime,Activity::getActivityEndTime,Activity::getCreateTime,
                    Activity::getActivityShow));
            map.put("activity", MapUnite.getMap(activity));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

}

