package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.DynamicZan;
import com.entity.UserCoupon;
import com.entity.UserFollow;
import com.entity.Users;
import com.enums.ExceptionEnum;
import com.service.impl.UserFollowServiceImpl;
import com.service.impl.UsersServiceImpl;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 我的关注用户 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@RestController
@RequestMapping("/userFollow")
@CrossOrigin
public class UserFollowController {
    @Autowired
    private UserFollowServiceImpl followService;
    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 关注
     * @param request
     * @return
     */
    @RequestMapping("/setFollow")
    public Object setFollow(HttpServletRequest request){
        UserFollow data = (UserFollow) MapUnite.getEntity(Util.getJSONParam(request), UserFollow.class);
        UserFollow follow = followService.getOne(new QueryWrapper<UserFollow>().lambda()
                .eq(UserFollow::getUserId, data.getUserId())
                .eq(UserFollow::getFollowId,data.getFollowId()));
        if (follow == null){
            data.setState(Code.ONE);
            data.setCreateTime(LocalDateTime.now());
            boolean b = followService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.OPERATE_INFO);
        }
        data.setId(follow.getId());
        if (Code.ZERO.equals(follow.getState())){
            data.setState(Code.ONE);
        }else{
            data.setState(Code.ZERO);
        }
        boolean b = followService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 我的关注与粉丝
     * @param userId
     * @param types
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getFollow")
    public Object getFollow(@NotNull(message = "用户ID不能为空") Integer userId, String types,
                            Integer page,Integer pagesize) throws ParseException {
        Page<UserFollow> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<UserFollow> query = new QueryWrapper<UserFollow>().lambda();
        query.eq(UserFollow::getState,Code.ONE);
        if (Code.ZERO.equals(types)){
            query.eq(UserFollow::getUserId,userId);
            IPage iPage = followService.page(uPage, query);
            List<UserFollow> dataList = iPage.getRecords();
            List<Object> list = new ArrayList<>();
            for (UserFollow user : dataList) {
                Map map = MapUnite.getMap(user);
                Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                        .eq(Users::getId, user.getFollowId())
                        .select(Users::getAvatar, Users::getNickname));
                map.put("followUser",users);
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                map.put("time",doTime(df.format(user.getCreateTime())));
                list.add(map);
            }
            return Result.ok(list, (int) iPage.getTotal());
        }else{
            query.eq(UserFollow::getFollowId,userId);
            IPage iPage = followService.page(uPage, query);
            List<UserFollow> dataList = iPage.getRecords();
            List<Object> list = new ArrayList<>();
            for (UserFollow user : dataList) {
                Map map = MapUnite.getMap(user);
                UserFollow follow = followService.getOne(new QueryWrapper<UserFollow>().lambda()
                        .eq(UserFollow::getUserId, user.getUserId())
                        .eq(UserFollow::getState,Code.ONE)
                        .eq(UserFollow::getFollowId,userId));
                if (follow != null){
                    map.put("isFollow", Code.one);
                }else{
                    map.put("isFollow", Code.zero);
                }
                Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                        .eq(Users::getId, user.getUserId())
                        .select(Users::getAvatar, Users::getNickname));
                map.put("user",users);
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                map.put("time",doTime(df.format(user.getCreateTime())));
                list.add(map);
            }
            return Result.ok(list, (int) iPage.getTotal());
        }
    }

    /**
     *  判断几分钟前时间
     * @param data
     * @return
     * @throws ParseException
     */
    public String doTime(String data) throws ParseException {
        String createDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(data);
        long differenceValue = new Date().getTime() - date.getTime();
        if (differenceValue <= 3600000) {
            createDate = (differenceValue / 1000 / 60) + "分钟前";
        } else {
            if (differenceValue < 86400000) {
                createDate = (differenceValue / 1000 / 60 / 60) + "小时前";
            } else {
                createDate = data;
            }
        }
        return createDate;
    }
}

