package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Region;
import com.entity.RegionCircle;
import com.enums.ExceptionEnum;
import com.service.impl.RegionCircleServiceImpl;
import com.service.impl.RegionServiceImpl;
import com.tool.Token;
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
 * 地区 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@RestController
@RequestMapping("/region")
@CrossOrigin
public class RegionController {
    @Autowired
    private RegionServiceImpl regionService;
    @Autowired
    private RegionCircleServiceImpl circleService;

    /**
     * 添加和修改地区
     * @param request
     * @return
     */
    @RequestMapping("/setRegion")
    @Token.LoginToken
    public Object setRegion(HttpServletRequest request){
        Region data = (Region) MapUnite.getEntity(Util.getJSONParam(request), Region.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = regionService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        boolean b = regionService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 后台select接口
     * @return
     */
    @RequestMapping("/getRegionAndCircle")
    @Token.PassToken
    public Object getRegionToSelect(){
        List<Region> regionList = regionService.list(new QueryWrapper<Region>().lambda()
                .select(Region::getId,Region::getName));
        List list = new ArrayList();
        for (Region region : regionList) {
            Map objMap = MapUnite.getObjMap(region);
            List<RegionCircle> circleList = circleService.list(new QueryWrapper<RegionCircle>().lambda()
                    .eq(RegionCircle::getRegionId, region.getId())
                    .select(RegionCircle::getId, RegionCircle::getName));
            objMap.put("items",circleList);
            list.add(objMap);
        }
        return Result.ok(list);
    }

    /**
     * 查询地区
     * @return
     */
    @RequestMapping("/getRegion")
    @Token.PassToken
    public Object getRegion(){
        List<Region> list = regionService.list();
        return Result.ok(list);
    }

    /**
     * 删除地区
     * @param id
     * @return
     */
    @RequestMapping("/delRegionById")
    @Token.LoginToken
    public Object delRegionById(Integer id){
        boolean b = regionService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 添加和修改地区商圈
     * @param request
     * @return
     */
    @RequestMapping("/setCircle")
    @Token.LoginToken
    public Object setCircle(HttpServletRequest request){
        RegionCircle data = (RegionCircle) MapUnite.getEntity(Util.getJSONParam(request), RegionCircle.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = circleService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        boolean b = circleService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询商圈
     * @param regionId
     * @return
     */
    @RequestMapping("/getCircle")
    public Object getCircle(Integer regionId){
        List<RegionCircle> circleList = circleService.list(new QueryWrapper<RegionCircle>().lambda()
                .eq(RegionCircle::getRegionId, regionId));
        return Result.ok(circleList);
    }

    /**
     * 删除地区的商圈
     * @param id
     * @return
     */
    @RequestMapping("/delCircleById")
    public Object delCircleById(Integer id){
        boolean b = circleService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }
}

