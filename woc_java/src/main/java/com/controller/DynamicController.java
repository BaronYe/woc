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
import io.netty.handler.codec.DateFormatter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户发布的动态 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-11
 */
@RestController
@RequestMapping("/dynamic")
@CrossOrigin
public class DynamicController {
    @Autowired
    private DynamicServiceImpl dynamicService;
    @Autowired
    private DynamicZanServiceImpl zanService;
    @Autowired
    private UserFollowServiceImpl followService;
    @Autowired
    private UserPointServiceImpl pointService;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 添加和修改
     *
     * @param request
     * @return
     */
    @RequestMapping("/setDynamic")
    public Object setDynamic(HttpServletRequest request) {
        Dynamic data = (Dynamic) MapUnite.getEntity(Util.getJSONParam(request), Dynamic.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = dynamicService.save(data);
            if (b) {
                // 判断用户每天发布动态
                UserPoint point = pointService.getOne(new QueryWrapper<UserPoint>().lambda()
                        .eq(UserPoint::getUserId, data.getUserId())
                        .eq(UserPoint::getTypes, Code.FIVE)
                        .apply("TO_DAYS(user_point.create_time) = TO_DAYS(NOW())"));
                if (point == null) {
                    pointService.setUserPoint(Code.FIVE, data.getUserId(), Code.ZERO);
                }
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = dynamicService.updateById(data);
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
    @RequestMapping("/delDynamicById")
    public Object delDynamicById(Integer id) {
        boolean b = dynamicService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 获取动态详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/getDynamicById")
    public Object getDynamicById(Integer id, Integer userId) {
        Dynamic dynamic = dynamicService.getById(id);
        Map map = MapUnite.getMap(dynamic);
        if (!Code.zero.equals(userId)) {
            DynamicZan zan = zanService.getOne(new QueryWrapper<DynamicZan>().lambda()
                    .eq(DynamicZan::getUserId, userId)
                    .eq(DynamicZan::getDynamicId, dynamic.getId())
                    .eq(DynamicZan::getState, Code.ONE));
            if (zan != null) {
                map.put("isZan", Code.one);
            } else {
                map.put("isZan", Code.zero);
            }
            UserFollow follow = followService.getOne(new QueryWrapper<UserFollow>().lambda()
                    .eq(UserFollow::getUserId, userId)
                    .eq(UserFollow::getState,Code.ONE)
                    .eq(UserFollow::getFollowId, dynamic.getUserId()));
            if (follow != null) {
                map.put("isFollow", Code.one);
            } else {
                map.put("isFollow", Code.zero);
            }
        } else {
            map.put("isZan", Code.zero);
            map.put("isFollow", Code.zero);
        }
        Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                .eq(Users::getId, dynamic.getUserId())
                .select(Users::getAvatar, Users::getNickname));
        map.put("users", users);
        int zanCount = zanService.count(new QueryWrapper<DynamicZan>().lambda()
                .eq(DynamicZan::getDynamicId, dynamic.getId())
                .eq(DynamicZan::getState, Code.ONE));
        map.put("zanCount", zanCount);
        int commentCount = commentService.count(new QueryWrapper<Comment>().lambda()
                .eq(Comment::getDynamicId, dynamic.getId()));
        map.put("commentCount", commentCount);
        return Result.ok(map);
    }


    /**
     * 获取我的动态
     *
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @RequestMapping("/getDynamicByUserId")
    public Object getDynamicByUserId(Integer page, Integer pagesize, Integer userId) {
        Page<Dynamic> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Dynamic> query = new QueryWrapper<Dynamic>().lambda();
        query.orderByDesc(Dynamic::getCreateTime);
        query.eq(Dynamic::getUserId, userId);
        IPage iPage = dynamicService.page(uPage, query);
        List<Dynamic> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Dynamic data : dataList) {
            Map map = MapUnite.getMap(data);
            if (!Code.zero.equals(userId)) {
                DynamicZan zan = zanService.getOne(new QueryWrapper<DynamicZan>().lambda()
                        .eq(DynamicZan::getUserId, userId)
                        .eq(DynamicZan::getDynamicId, data.getId())
                        .eq(DynamicZan::getState, Code.ONE));
                if (zan != null) {
                    map.put("isZan", Code.one);
                } else {
                    map.put("isZan", Code.zero);
                }
            } else {
                map.put("isZan", Code.zero);
            }
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);
            int zanCount = zanService.count(new QueryWrapper<DynamicZan>().lambda()
                    .eq(DynamicZan::getDynamicId, data.getId())
                    .eq(DynamicZan::getState, Code.ONE));
            map.put("zanCount", zanCount);
            int commentCount = commentService.count(new QueryWrapper<Comment>().lambda()
                    .eq(Comment::getDynamicId, data.getId()));
            map.put("commentCount", commentCount);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 获取动态列表
     *
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getDynamic")
    public Object getDynamic(Integer page, Integer pagesize, Integer userId,String title) {
        Page<Dynamic> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Dynamic> query = new QueryWrapper<Dynamic>().lambda();
        query.orderByDesc(Dynamic::getCreateTime);
        if (!Code.KONG.equals(title)){
            query.like(Dynamic::getContent,title);
        }
        IPage iPage = dynamicService.page(uPage, query);
        List<Dynamic> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Dynamic data : dataList) {
            Map map = MapUnite.getMap(data);
            if (!Code.zero.equals(userId)) {
                DynamicZan zan = zanService.getOne(new QueryWrapper<DynamicZan>().lambda()
                        .eq(DynamicZan::getUserId, userId)
                        .eq(DynamicZan::getDynamicId, data.getId())
                        .eq(DynamicZan::getState, Code.ONE));
                if (zan != null) {
                    map.put("isZan", Code.one);
                } else {
                    map.put("isZan", Code.zero);
                }
                UserFollow follow = followService.getOne(new QueryWrapper<UserFollow>().lambda()
                        .eq(UserFollow::getUserId, userId)
                        .eq(UserFollow::getState,Code.ONE)
                        .eq(UserFollow::getFollowId, data.getUserId()));
                if (follow != null) {
                    map.put("isFollow", Code.one);
                } else {
                    map.put("isFollow", Code.zero);
                }
            } else {
                map.put("isZan", Code.zero);
                map.put("isFollow", Code.zero);
            }
            int zanCount = zanService.count(new QueryWrapper<DynamicZan>().lambda()
                    .eq(DynamicZan::getDynamicId, data.getId())
                    .eq(DynamicZan::getState, Code.ONE));
            map.put("zanCount", zanCount);
            int commentCount = commentService.count(new QueryWrapper<Comment>().lambda()
                    .eq(Comment::getDynamicId, data.getId()));
            map.put("commentCount", commentCount);
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 首页查询动态
     *
     * @param page
     * @param pagesize
     * @param userId
     * @param types
     * @return
     */
    @RequestMapping("/getHomeDynaic")
    public Object getHomeDynaic(Integer page, Integer pagesize, Integer userId, Integer types,String lat,String lng) {
        List<Dynamic> dataList = null;
        if (Code.zero.equals(types)) {
            List<UserFollow> list = followService.list(new QueryWrapper<UserFollow>().lambda()
                    .eq(UserFollow::getUserId, userId).eq(UserFollow::getState, Code.ONE)
                    .select(UserFollow::getFollowId));
            List<Object> aList = new ArrayList();
            for (UserFollow follow : list) {
                aList.add(follow.getFollowId());
            }
            String join = StringUtils.join(aList, ",");
            Page<Dynamic> uPage = new Page<>(page, pagesize);
            LambdaQueryWrapper<Dynamic> query = new QueryWrapper<Dynamic>().lambda();
            query.orderByDesc(Dynamic::getCreateTime);
            query.inSql(Dynamic::getUserId, join);
            IPage iPage = dynamicService.page(uPage, query);
            dataList = iPage.getRecords();
        } else if (Code.one.equals(types)) {
            Page<Dynamic> uPage = new Page<>(page, pagesize);
            LambdaQueryWrapper<Dynamic> query = new QueryWrapper<Dynamic>().lambda();
            query.orderByDesc(Dynamic::getCreateTime);
            IPage iPage = dynamicService.page(uPage, query);
            dataList = iPage.getRecords();
        } else {
            Integer a = ( page - 1 ) * pagesize;
            dataList = dynamicService.getDynamicLat(lat, lng, a, pagesize);
        }
        List<Object> list = new ArrayList<>();
        for (Dynamic data : dataList) {
            Map map = MapUnite.getMap(data);
            if (!Code.zero.equals(userId)) {
                DynamicZan zan = zanService.getOne(new QueryWrapper<DynamicZan>().lambda()
                        .eq(DynamicZan::getUserId, userId)
                        .eq(DynamicZan::getDynamicId, data.getId())
                        .eq(DynamicZan::getState, Code.ONE));
                if (zan != null) {
                    map.put("isZan", Code.one);
                } else {
                    map.put("isZan", Code.zero);
                }
                UserFollow follow = followService.getOne(new QueryWrapper<UserFollow>().lambda()
                        .eq(UserFollow::getUserId, userId)
                        .eq(UserFollow::getState,Code.ONE)
                        .eq(UserFollow::getFollowId, data.getUserId()));
                if (follow != null) {
                    map.put("isFollow", Code.one);
                } else {
                    map.put("isFollow", Code.zero);
                }
            } else {
                map.put("isZan", Code.zero);
                map.put("isFollow", Code.zero);
            }
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);
            int zanCount = zanService.count(new QueryWrapper<DynamicZan>().lambda()
                    .eq(DynamicZan::getDynamicId, data.getId())
                    .eq(DynamicZan::getState, Code.ONE));
            map.put("zanCount", zanCount);
            int commentCount = commentService.count(new QueryWrapper<Comment>().lambda()
                    .eq(Comment::getDynamicId, data.getId()));
            map.put("commentCount", commentCount);
            list.add(map);
        }
        return Result.ok(list);
    }

    /**
     * 求两点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        double d = (x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1);
        //绝对值
        double number = Math.abs(d);
        return Math.sqrt(number);
    }

    /**
     * 点赞
     *
     * @param request
     * @return
     */
    @RequestMapping("/setDynamicZan")
    public Object setDynamicZan(HttpServletRequest request) {
        DynamicZan data = (DynamicZan) MapUnite.getEntity(Util.getJSONParam(request), DynamicZan.class);
        DynamicZan zan = zanService.getOne(new QueryWrapper<DynamicZan>().lambda()
                .eq(DynamicZan::getUserId, data.getUserId())
                .eq(DynamicZan::getDynamicId, data.getDynamicId()));
        if (zan == null) {
            Dynamic dynamic = dynamicService.getOne(new QueryWrapper<Dynamic>().lambda()
                    .eq(Dynamic::getId, data.getDynamicId()).select(Dynamic::getUserId));
            data.setDynamicUserId(dynamic.getUserId());
            data.setState(Code.ONE);
            data.setCreateTime(LocalDateTime.now());
            boolean b = zanService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.OPERATE_INFO);
        }
        data.setId(zan.getId());
        if (Code.ZERO.equals(zan.getState())) {
            data.setState(Code.ONE);
        } else {
            data.setState(Code.ZERO);
        }
        boolean b = zanService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 查看我收到的赞
     *
     * @param userId
     * @param page
     * @param pagesize
     * @return
     * @throws ParseException
     */
    @RequestMapping("/getDynamicZan")
    public Object getDynamicZan(Integer userId, Integer page, Integer pagesize) throws ParseException {
        Page<DynamicZan> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<DynamicZan> query = new QueryWrapper<DynamicZan>().lambda();
        query.orderByDesc(DynamicZan::getCreateTime);
        IPage iPage = zanService.page(uPage, query);
        List<DynamicZan> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (DynamicZan data : dataList) {
            Map map = MapUnite.getObjMap(data);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            map.put("time",doTime(df.format(data.getCreateTime())));
            Dynamic dynamic = dynamicService.getOne(new QueryWrapper<Dynamic>().lambda()
                    .eq(Dynamic::getId, data.getDynamicId())
                    .select(Dynamic::getUserId, Dynamic::getImgs, Dynamic::getTitle));
            map.put("dynamic", MapUnite.getObjMap(dynamic));
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }


    /**
     * 判断几分钟前时间
     *
     * @param data
     * @return
     * @throws ParseException
     */
    public String doTime(String data) throws ParseException {
        String createDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(data);
        long differenceValue = new Date().getTime() - date.getTime();
        if (differenceValue <= 60000) {
            createDate = (differenceValue / 1000 / 60 / 60) + "秒前";
        } else if(60000 < differenceValue && differenceValue <= 3600000) {
            createDate = (differenceValue / 1000 / 60) + "分钟前";
        }  else {
            if (differenceValue < 86400000) {
                createDate = (differenceValue / 1000 / 60 / 60) + "小时前";
            } else {
                createDate = data;
            }
        }
        return createDate;
    }

}

