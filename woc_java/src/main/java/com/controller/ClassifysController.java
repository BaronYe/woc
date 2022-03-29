package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Classifys;
import com.enums.ExceptionEnum;
import com.service.impl.ClassifysServiceImpl;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分类 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-07
 */
@RestController
@RequestMapping("/classifys")
@CrossOrigin
public class ClassifysController {
    @Autowired
    private ClassifysServiceImpl classifysService;

    /**
     * 商品分类的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setClassifys")
    @Token.LoginToken
    public Object setClassifys(HttpServletRequest request){
        Classifys data = (Classifys) MapUnite.getEntity(Util.getJSONParam(request), Classifys.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = classifysService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = classifysService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询分类
     * @return
     */
    @RequestMapping("/getClassifys")
    public Object getClassifys(){
        List<Classifys> classifysList = classifysService.list(new QueryWrapper<Classifys>().lambda()
                .eq(Classifys::getCid, Code.zero).select(Classifys::getId,Classifys::getName));
        return Result.ok(classifysList);
    }

    /**
     * 查询分类的下级
     * @param cid
     * @return
     */
    @RequestMapping("/getClassifysSon")
    public Object getClassifysSon(Integer cid){
        List<Classifys> classifysList = classifysService.list(new QueryWrapper<Classifys>().lambda()
                .eq(Classifys::getCid, cid).select(Classifys::getId,Classifys::getName));
        return Result.ok(classifysList);
    }

    /**
     * 分类和子类一起展示
     * @return
     */
    @RequestMapping("/getClassifyList")
    public Object getClassifyList(){
        List<Classifys> classifysList = classifysService.list(new QueryWrapper<Classifys>().lambda()
                .eq(Classifys::getCid, Code.zero));
        List list = new ArrayList();
        for (Classifys classifys : classifysList) {
            Map map = MapUnite.getMap(classifys);
            List<Classifys> cList = classifysService.list(new QueryWrapper<Classifys>().lambda()
                    .eq(Classifys::getCid, classifys.getId()));
            map.put("items",cList);
            list.add(map);
        }
        return Result.ok(list);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @RequestMapping("/delClassifyById")
    public  Object delClassifyById(Integer id){
        boolean b = classifysService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

}

