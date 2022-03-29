package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Dynamic;
import com.entity.DynamicZan;
import com.entity.Feedbacks;
import com.entity.Users;
import com.enums.ExceptionEnum;
import com.service.impl.FeedbacksServiceImpl;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 反馈信息 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@RestController
@RequestMapping("/feedbacks")
@CrossOrigin
public class FeedbacksController {
    @Autowired
    private FeedbacksServiceImpl feedbacksService;
    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 添加和修改
     * @param request
     * @return
     */
    @RequestMapping("/setFeedbacks")
    public Object setFeedbacks(HttpServletRequest request){
        Feedbacks data = (Feedbacks) MapUnite.getEntity(Util.getJSONParam(request), Feedbacks.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = feedbacksService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = feedbacksService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delFeedbacksById")
    public Object delFeedbacksById(Integer id){
        boolean b = feedbacksService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 查询反馈信息
     * @param page
     * @param pagesize
     * @param state
     * @return
     */
    @RequestMapping("/getFeedbacks")
    public Object getFeedbacks(Integer page,Integer pagesize,String state){
        Page<Feedbacks> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Feedbacks> query = new QueryWrapper<Feedbacks>().lambda();
        query.orderByDesc(Feedbacks::getCreateTime);
        if (!Code.KONG.equals(state)){
            query.eq(Feedbacks::getState,state);
        }
        IPage iPage = feedbacksService.page(uPage, query);
        List<Feedbacks> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Feedbacks data : dataList) {
            Map map = MapUnite.getMap(data);
            Users users = usersService.getById(data.getUserId());
            map.put("users",MapUnite.getObjMap(users));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

}

