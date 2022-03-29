package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Merchant;
import com.entity.Notices;
import com.enums.ExceptionEnum;
import com.service.impl.NoticesServiceImpl;
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
 * 系统通知 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/notices")
@CrossOrigin
public class NoticesController {
    @Autowired
    private NoticesServiceImpl noticesService;

    /**
     * 添加和修改通知
     * @param request
     * @return
     */
    @RequestMapping("/setNotices")
    public Object setNotices(HttpServletRequest request){
        Notices notices = (Notices)MapUnite.getEntity(Util.getJSONParam(request), Notices.class);
        if (Code.zero.equals(notices.getId())){
            notices.setCreateTime(LocalDateTime.now());
            boolean b = noticesService.save(notices);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = noticesService.updateById(notices);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询通知
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getNotices")
    public Object getNotices(Integer page,Integer pagesize){
        Page<Notices> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Notices> query = new QueryWrapper<Notices>().lambda();
        query.orderByDesc(Notices::getCreateTime);
        IPage iPage = noticesService.page(uPage, query);
        List<Notices> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Notices data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 删除通知
     * @param id
     * @return
     */
    @RequestMapping("/delNoticesById")
    public Object delNoticesById(Integer id){
        boolean b = noticesService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }
}

