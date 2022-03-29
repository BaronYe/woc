package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Classifys;
import com.entity.Item;
import com.entity.ItemSpecs;
import com.enums.ExceptionEnum;
import com.service.impl.ClassifysServiceImpl;
import com.service.impl.ItemServiceImpl;
import com.service.impl.ItemSpecsServiceImpl;
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
 * 商品 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-08
 */
@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ItemSpecsServiceImpl specsService;
    @Autowired
    private ClassifysServiceImpl classifysService;

    /**
     * 添加或修改商品
     * @param request
     * @return
     */
    @PostMapping("/setItem")
    @Token.LoginToken
    public Object setItem(HttpServletRequest request){
        Item data = (Item) MapUnite.getEntity(Util.getJSONParam(request), Item.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = itemService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        boolean b = itemService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询商品
     * @param page
     * @param pagesize
     * @param cid
     * @param sonCid
     * @param name
     * @return
     */
    @RequestMapping("/getItem")
    @Token.PassToken
    public Object getItem(Integer page,Integer pagesize,Integer cid,Integer sonCid,String name,String isShelves){
        Page<Item> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Item> query = new QueryWrapper<Item>().lambda();
        if (!Code.load_one.equals(cid)){
            query.eq(Item::getCid,cid);
        }
        if (!Code.load_one.equals(sonCid)){
            query.eq(Item::getSonCid,sonCid);
        }
        if (!Code.KONG.equals(name)){
            query.like(Item::getTitle,name);
        }
        if (!Code.LOAD_ONE.equals(isShelves)){
            query.eq(Item::getIsShelves,isShelves);
        }
        query.select(Item::getId,Item::getTitle,Item::getImgs,Item::getPrice,Item::getCid,Item::getSonCid,
                Item::getIsShelves,Item::getIsDesc);
        IPage iPage = itemService.page(uPage, query);
        List<Item> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Item data : dataList) {
            Map map = MapUnite.getMap(data);
            Classifys c = classifysService.getById(data.getCid());
            map.put("cName",c.getName());
            Classifys s = classifysService.getById(data.getSonCid());
            map.put("sName",s.getName());
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询商品详情
     * @param id
     * @return
     */
    @RequestMapping("/getItemById")
    @Token.PassToken
    public Object getItemById(Integer id){
        Item item = itemService.getById(id);
        Map map = MapUnite.getMap(item);
        return Result.ok(map);
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    @RequestMapping("/delItemById")
    @Token.LoginToken
    public Object delItemById(Integer id){
        boolean b = itemService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 添加或修改规格
     * @param request
     * @return
     */
    @RequestMapping("/setSpecs")
    @Token.LoginToken
    public Object setSpecs(HttpServletRequest request){
        ItemSpecs data = (ItemSpecs) MapUnite.getEntity(Util.getJSONParam(request), ItemSpecs.class);
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = specsService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = specsService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 获取商品的规格
     * @param itemId
     * @return
     */
    @RequestMapping("/getSpecs")
    @Token.PassToken
    public Object getSpecs(Integer itemId){
        List<ItemSpecs> specsList = specsService.list(new QueryWrapper<ItemSpecs>().lambda().eq(ItemSpecs::getItemId, itemId));
        return Result.ok(specsList);
    }

    /**
     * 删除规格
     * @param id
     * @return
     */
    @RequestMapping("/delSpecsById")
    @Token.LoginToken
    public Object delSpecsById(Integer id){
        boolean b = specsService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 商品批量操作
     * @param types
     * @param idx
     * @return
     */
    @RequestMapping("/batchItem")
    public Object batchItem(String types,String idx){
        List<String> list = Arrays.asList(idx.split(","));
        if (Code.ZERO.equals(types)){
            for (String s : list) {
                itemService.updateById(new Item().setId(Integer.parseInt(s)).setIsShelves(Code.ONE));
            }
        }else if (Code.ONE.equals(types)) {
            for (String s : list) {
                itemService.updateById(new Item().setId(Integer.parseInt(s)).setIsShelves(Code.ZERO));
            }
        }else{
            itemService.removeByIds(list);
        }
        return Result.ok();
    }
}

