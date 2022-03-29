package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.IntegralClassify;
import com.entity.IntegralGoods;
import com.entity.Item;
import com.enums.ExceptionEnum;
import com.service.impl.IntegralClassifyServiceImpl;
import com.service.impl.IntegralGoodsServiceImpl;
import com.service.impl.ItemServiceImpl;
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
 * 积分商品 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-25
 */
@RestController
@RequestMapping("/integralGoods")
@CrossOrigin
public class IntegralGoodsController {
    @Autowired
    private IntegralGoodsServiceImpl goodsService;
    @Autowired
    private IntegralClassifyServiceImpl classifyService;
    @Autowired
    private ItemServiceImpl itemService;

    /**
     * 分类的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setIntegralClassify")
    @Token.LoginToken
    public Object setIntegralClassify(HttpServletRequest request){
        IntegralClassify data = (IntegralClassify) MapUnite.getEntity(Util.getJSONParam(request), IntegralClassify.class);
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
    @RequestMapping("/getIntegralClassify")
    @Token.PassToken
    public Object getIntegralClassify(){
        List<IntegralClassify> classifysList = classifyService.list();
        return Result.ok(classifysList);
    }


    /**
     * 删除分类
     * @param id
     * @return
     */
    @RequestMapping("/delIntegralClassifyById")
    @Token.LoginToken
    public  Object delIntegralClassifyById(Integer id){
        boolean b = classifyService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 积分商品的添加和修改
     * @param request
     * @return
     */
    @PostMapping("/setIntegralGoods")
    @Token.LoginToken
    public Object setIntegralGoods(HttpServletRequest request){
        IntegralGoods data = (IntegralGoods) MapUnite.getEntity(Util.getJSONParam(request), IntegralGoods.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = goodsService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = goodsService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 获取积分商品详情
     * @param id
     * @return
     */
    @RequestMapping("/getIntegralGoodsById")
    public Object getIntegralGoodsById(Integer id){
        IntegralGoods data = goodsService.getById(id);
        Map map = MapUnite.getMap(data);
        Item item = itemService.getOne(new QueryWrapper<Item>().lambda()
                .eq(Item::getId,data.getItemId())
                .select(Item::getImgs,Item::getTitle,Item::getPrice));
        map.put("iName",item.getTitle());
        map.put("price",item.getPrice());
        map.put("imgs",item.getImgs().split(","));
        map.put("text",item.getText());
        return Result.ok(map);
    }

    /**
     * 查询积分商品列表
     * @param page
     * @param pagesize
     * @param cid
     * @param name
     * @return
     */
    @RequestMapping("/getIntegralGoods")
    public Object getIntegralGoods(Integer page,Integer pagesize,Integer cid,String name,String isShelves){
        Page<IntegralGoods> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<IntegralGoods> query = new QueryWrapper<IntegralGoods>().lambda();
        query.orderByDesc(IntegralGoods::getCreateTime);
        if(!Code.load_one.equals(cid)){
            query.eq(IntegralGoods::getCid,cid);
        }
        if (!Code.KONG.equals(name)){
            query.like(IntegralGoods::getTitle,name);
        }
        if (!Code.LOAD_ONE.equals(isShelves)){
            query.eq(IntegralGoods::getIsShelves,isShelves);
        }
        IPage iPage = goodsService.page(uPage, query);
        List<IntegralGoods> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (IntegralGoods data : dataList) {
            Map map = MapUnite.getMap(data);
            Item item = itemService.getOne(new QueryWrapper<Item>().lambda()
                    .eq(Item::getId,data.getItemId())
                    .select(Item::getImgs,Item::getTitle,Item::getPrice));
            map.put("item",MapUnite.getMap(item));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 微信端获取积分商品列表
     * @param page
     * @param pagesize
     * @param cid
     * @return
     */
    @RequestMapping("/getIntegralGoodsAndWechat")
    public Object getIntegralGoodsAndWechat(Integer page,Integer pagesize,Integer cid,String types,String sort){
        Page<IntegralGoods> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<IntegralGoods> query = new QueryWrapper<IntegralGoods>().lambda();
        if(!Code.load_one.equals(cid)){
            query.eq(IntegralGoods::getCid,cid);
        }
        if(!Code.KONG.equals(types)){
            if (Code.ZERO.equals(types)){
                if ("asc".equals(sort)){
                    query.orderByAsc(IntegralGoods::getCreateTime);
                }else{
                    query.orderByDesc(IntegralGoods::getCreateTime);
                }
            }else if (Code.ONE.equals(types)){
                if ("asc".equals(sort)){
                    query.orderByAsc(IntegralGoods::getTotalCount);
                }else{
                    query.orderByDesc(IntegralGoods::getTotalCount);
                }
            }
        }
        IPage iPage = goodsService.page(uPage, query);
        List<IntegralGoods> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (IntegralGoods data : dataList) {
            Map map = MapUnite.getMap(data);
            Item item = itemService.getOne(new QueryWrapper<Item>().lambda()
                    .eq(Item::getId,data.getItemId())
                    .select(Item::getImgs,Item::getTitle,Item::getPrice));
            map.put("item",MapUnite.getMap(item));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 删除积分商品
     * @param id
     * @return
     */
    @RequestMapping("/delIntegralGoodsById")
    public Object delIntegralGoodsById(Integer id){
        boolean b = goodsService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 积分商品批量操作
     * @param types
     * @param idx
     * @return
     */
    @RequestMapping("/batchIntegral")
    public Object batchIntegral(String types,String idx){
        List<String> list = Arrays.asList(idx.split(","));
        if (Code.ZERO.equals(types)){
            for (String s : list) {
                goodsService.updateById(new IntegralGoods().setId(Integer.parseInt(s)).setIsShelves(Code.ONE));
            }
        }else if (Code.ONE.equals(types)) {
            for (String s : list) {
                goodsService.updateById(new IntegralGoods().setId(Integer.parseInt(s)).setIsShelves(Code.ZERO));
            }
        }else{
            goodsService.removeByIds(list);
        }
        return Result.ok();
    }
}

