package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.enums.ExceptionEnum;
import com.service.impl.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 盲盒 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-15
 */
@RestController
@RequestMapping("/box")
@CrossOrigin
public class BoxController {
    @Autowired
    private BoxClassifyServiceImpl classifyService;
    @Autowired
    private BoxServiceImpl boxService;
    @Autowired
    private BoxItemServiceImpl itemService;
    @Autowired
    private ItemServiceImpl iService;
    @Autowired
    private ItemSpecsServiceImpl specsService;

    /**
     * 盲盒分类的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setBoxClassify")
    @Token.LoginToken
    public Object setBoxClassify(HttpServletRequest request){
        BoxClassify data = (BoxClassify) MapUnite.getEntity(Util.getJSONParam(request), BoxClassify.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = classifyService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = classifyService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询分类
     * @return
     */
    @RequestMapping("/getBoxClassify")
    @Token.PassToken
    public Object getBoxClassify(){
        List<BoxClassify> classifysList = classifyService.list();
        return Result.ok(classifysList);
    }


    /**
     * 删除分类
     * @param id
     * @return
     */
    @RequestMapping("/delBoxClassifyById")
    @Token.LoginToken
    public  Object delBoxClassifyById(Integer id){
        boolean b = classifyService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 盲盒的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setBox")
    @Token.LoginToken
    public Object setBox(HttpServletRequest request){
        Box data = (Box) MapUnite.getEntity(Util.getJSONParam(request), Box.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = boxService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = boxService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 获取盲盒详情
     * @param id
     * @return
     */
    @RequestMapping("/getBoxById")
    public Object getBoxById(Integer id){
        Box box = boxService.getById(id);
        return Result.ok(MapUnite.getMap(box));
    }

    /**
     * 查询盲盒列表
     * @param page
     * @param pagesize
     * @param cid
     * @param name
     * @return
     */
    @RequestMapping("/getBox")
    public Object getBox(Integer page,Integer pagesize,Integer cid,String name,String isShelves){
        Page<Box> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Box> query = new QueryWrapper<Box>().lambda();
        query.orderByDesc(Box::getCreateTime);
        if(!Code.load_one.equals(cid)){
            query.eq(Box::getCid,cid);
        }
        if (!Code.KONG.equals(name)){
            query.like(Box::getBoxName,name);
        }
        if (!Code.LOAD_ONE.equals(isShelves)){
            query.eq(Box::getIsShelves,isShelves);
        }
        IPage iPage = boxService.page(uPage, query);
        List<Box> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Box data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 删除盲盒
     * @param id
     * @return
     */
    @RequestMapping("/delBoxById")
    public Object delBoxById(Integer id){
        boolean b = boxService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 盲盒下商品的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setBoxItem")
    @Token.LoginToken
    public Object setBoxItem(HttpServletRequest request){
        BoxItem data = (BoxItem) MapUnite.getEntity(Util.getJSONParam(request), BoxItem.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = itemService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = itemService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 获取盲盒下的商品
     * @param boxId
     * @return
     */
    @RequestMapping("/getBoxItem")
    public Object getBoxItem(Integer boxId){
        List<BoxItem> itemList = itemService.list(new QueryWrapper<BoxItem>().lambda().eq(BoxItem::getBoxId, boxId));
        List list = new ArrayList();
        for (BoxItem boxItem : itemList) {
            Map objMap = MapUnite.getObjMap(boxItem);
            Item item = iService.getOne(new QueryWrapper<Item>().lambda()
                    .eq(Item::getId, boxItem.getItemId())
                    .select(Item::getId, Item::getTitle,Item::getImgs));
            objMap.put("iName",item.getTitle());
            objMap.put("iImgs",item.getImgs().split(",")[0]);
            if(!boxItem.getSpescId().equals(Code.zero)){
                ItemSpecs specs = specsService.getById(boxItem.getSpescId());
                objMap.put("specsName",specs.getName());
            }else{
                objMap.put("specsName","");
            }
            list.add(objMap);
        }
        return Result.ok(list);
    }

    /**
     * 删除盲盒下的商品
     * @param id
     * @return
     */
    @RequestMapping("/delBoxItemById")
    @Token.LoginToken
    public  Object delBoxItemById(Integer id){
        boolean b  = itemService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 盲盒的批量操作
     * @param types
     * @param idx
     * @return
     */
    @RequestMapping("/batchBox")
    public Object batchBox(String types,String idx){
        List<String> list = Arrays.asList(idx.split(","));
        if (Code.ZERO.equals(types)){
            for (String s : list) {
                boxService.updateById(new Box().setId(Integer.parseInt(s)).setIsShelves(Code.ONE));
            }
        }else if (Code.ONE.equals(types)) {
            for (String s : list) {
                boxService.updateById(new Box().setId(Integer.parseInt(s)).setIsShelves(Code.ZERO));
            }
        }else{
            boxService.removeByIds(list);
        }
        return Result.ok();
    }

}

