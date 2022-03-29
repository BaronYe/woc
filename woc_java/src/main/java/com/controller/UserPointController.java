package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.UserPoint;
import com.entity.Users;
import com.enums.ExceptionEnum;
import com.service.impl.UserPointServiceImpl;
import com.service.impl.UsersServiceImpl;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分明细 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-25
 */
@RestController
@RequestMapping("/userPoint")
@CrossOrigin
public class UserPointController {
    @Autowired
    private UserPointServiceImpl pointService;
    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 分享
     * @param userId
     * @return
     */
    @RequestMapping("/share")
    public Object share(Integer userId){
        UserPoint point = pointService.getOne(new QueryWrapper<UserPoint>().lambda()
                .eq(UserPoint::getUserId, userId)
                .eq(UserPoint::getTypes,Code.SEVEN));
        if (point == null){
            pointService.setUserPoint(Code.SEVEN,userId,Code.ZERO);
        }
        return Result.ok();
    }

    /**
     * 获取积分明细
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @RequestMapping("/getUserPoint")
    private Object getUserPoint(Integer page, Integer pagesize,Integer userId,Integer sign){
        Page<UserPoint> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<UserPoint> query = new QueryWrapper<UserPoint>().lambda();
        query.eq(UserPoint::getUserId,userId);
        query.orderByAsc(UserPoint::getIsGet);
        query.orderByDesc(UserPoint::getCreateTime);
        if (!Code.load_one.equals(sign)){
            query.eq(UserPoint::getSign,sign);
        }
        IPage iPage = pointService.page(uPage, query);
        List<UserPoint> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (UserPoint data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 一键领取
     * @param userId
     * @return
     */
    @RequestMapping("/receivePoint")
    public Object receivePoint(@NotNull(message = "用户ID不能为空") Integer userId){
        LambdaQueryWrapper<UserPoint> query = new QueryWrapper<UserPoint>().lambda();
        query.eq(UserPoint::getUserId,userId);
        query.eq(UserPoint::getIsGet,Code.zero);
        query.select(UserPoint::getNum,UserPoint::getIsGet,UserPoint::getId);
        List<UserPoint> pointList = pointService.list(query);
        int point = 0;
        for (UserPoint userPoint : pointList) {
            if (Code.zero.equals(userPoint.getIsGet())){
                point = point + userPoint.getNum();
                pointService.updateById(new UserPoint().setId(userPoint.getId()).setIsGet(Code.one));
            }
        }
        Users users = usersService.getById(userId);
        boolean b = usersService.updateById(new Users().setId(userId).setPoint(users.getPoint() + point));
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.GET_POINT_ERROR);
    }

    /**
     * 单个领取
     * @param id
     * @param userId
     * @return
     */
    @RequestMapping("/receivePointById")
    public Object receivePointById(@NotNull(message = "ID不能为空") int id,
                                   @NotNull(message = "用户ID不能为空") Integer userId){
        UserPoint point = pointService.getOne(new QueryWrapper<UserPoint>().lambda()
                .eq(UserPoint::getId, id)
                .eq(UserPoint::getUserId, userId));
        if (point == null){
            throw new MyException(ExceptionEnum.DATA_NULL);
        }
        boolean b1 = pointService.updateById(new UserPoint().setId(point.getId()).setIsGet(Code.one));
        if (b1){
            Users users = usersService.getById(userId);
            boolean b = usersService.updateById(new Users().setId(userId).setPoint(users.getPoint() + point.getNum()));
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.GET_POINT_ERROR);
        }
        throw new MyException(ExceptionEnum.GET_POINT_ERROR);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delUsrPointById")
    public Object delUsrPointById(@NotNull(message = "ID不能为空") Integer id){
        boolean b = pointService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DATA_NULL);
    }
}

