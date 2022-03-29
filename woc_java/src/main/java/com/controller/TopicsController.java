package com.controller;


import com.entity.IntegralClassify;
import com.entity.Topics;
import com.enums.ExceptionEnum;
import com.service.impl.TopicsServiceImpl;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 话题列表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-07-09
 */
@RestController
@RequestMapping("/topics")
@CrossOrigin
public class TopicsController {

    @Autowired
    private TopicsServiceImpl topicsService;

    /**
     * 话题的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setTopics")
    @Token.LoginToken
    public Object setTopics(HttpServletRequest request){
        Topics data = (Topics) MapUnite.getEntity(Util.getJSONParam(request), Topics.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = topicsService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = topicsService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询话题
     * @return
     */
    @RequestMapping("/getTopics")
    @Token.PassToken
    public Object getTopics(){
        List<Topics> dataList = topicsService.list();
        return Result.ok(dataList);
    }


    /**
     * 删除话题
     * @param id
     * @return
     */
    @RequestMapping("/delTopics")
    @Token.LoginToken
    public  Object delTopics(Integer id){
        boolean b = topicsService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }
}

