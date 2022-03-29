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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-16
 */
@RestController
@RequestMapping("/merchant")
@CrossOrigin
public class MerchantController {

    @Autowired
    private MerchantServiceImpl merchantService;
    @Autowired
    private MerchantShopServiceImpl shopService;
    @Autowired
    private ShopServiceImpl sService;
    @Autowired
    private RegionServiceImpl regionService;
    @Autowired
    private RegionCircleServiceImpl circleService;

    /**
     * 添加和修改商户信息
     * @param request
     * @return
     */
    @RequestMapping("/setMerchant")
    @Token.LoginToken
    public Object setMerchant(HttpServletRequest request){
        Merchant data = (Merchant) MapUnite.getEntity(Util.getJSONParam(request), Merchant.class);
        if (data.getId().equals(Code.zero)) {
            Merchant merchant = merchantService.getOne(new QueryWrapper<Merchant>().lambda().eq(Merchant::getUsername, data.getUsername()));
            if (merchant != null){
                throw new MyException(ExceptionEnum.ACCOUNT_EXIST);
            }
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            data.setPassword(Util.base64Plus(data.getPassword()));
            boolean b = merchantService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        Merchant merchant = merchantService.getById(data.getId());
        if (merchant.getPassword().equals(data.getPassword())){
            data.setPassword(merchant.getPassword());
        }else{
            data.setPassword(Util.base64Plus(data.getPassword()));
        }
        boolean b = merchantService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 封禁与解封商户
     * @param id
     * @param isDisable
     * @return
     */
    @RequestMapping("/setDisable")
    public Object setDisable(Integer id,String isDisable){
        boolean b = merchantService.updateById(new Merchant().setId(id).setIsDisable(isDisable));
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 商户登陆
     * @param request
     * @return
     */
    @RequestMapping("/setMerchantLogin")
    public Object setMerchantLogin(HttpServletRequest request){
        Merchant data = (Merchant) MapUnite.getEntity(Util.getJSONParam(request), Merchant.class);
        Merchant merchant = merchantService.getOne(new QueryWrapper<Merchant>().lambda().eq(Merchant::getUsername, data.getUsername()));
        if (merchant == null){
            throw new MyException(ExceptionEnum.ACCOUNT_ERROR);
        }
        String pas = merchant.getPassword();
        if (!pas.equals(Util.base64Plus(data.getPassword()))){
            throw new MyException(ExceptionEnum.PASSWORD_ERROR);
        }
        return Result.ok(merchant);
    }

    /**
     * 删除商户
     * @param id
     * @return
     */
    @RequestMapping("/delMerchantById")
    @Token.LoginToken
    public Object delMerchantById(Integer id){
        boolean b = merchantService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 获取商户列表
     * @param page
     * @param pagesize
     * @param name
     * @return
     */
    @RequestMapping("/getMerchant")
    public Object getMerchant(Integer page,Integer pagesize,String name){
        Page<Merchant> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Merchant> query = new QueryWrapper<Merchant>().lambda();
        query.orderByDesc(Merchant::getCreateTime);
        if (!Code.KONG.equals(name)){
            query.like(Merchant::getName,name);
        }
        IPage iPage = merchantService.page(uPage, query);
        List<Merchant> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Merchant data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 获取商户详情
     * @param id
     * @return
     */
    @RequestMapping("/getMerchantById")
    public Object getMerchantById(Integer id){
        Merchant merchant = merchantService.getById(id);
        if (!Code.KONG.equals(merchant.getPassword())){
            merchant.setPassword(Util.base64Untie(merchant.getPassword()));
        }
        return Result.ok(merchant);
    }

    /**
     * 添加和修改商户下的店铺
     * @param request
     * @return
     */
    @RequestMapping("/setMerchantShop")
    @Token.LoginToken
    public Object setMerchantShop(HttpServletRequest request){
        MerchantShop data = (MerchantShop) MapUnite.getEntity(Util.getJSONParam(request), MerchantShop.class);
        if (data.getId().equals(Code.zero)) {
            Shop shop = sService.getById(data.getShopId());
            if (Code.ONE.equals(shop.getMerchantState())) {
                throw new MyException(ExceptionEnum.SHOP_NOT);
            }
            data.setCreateTime(LocalDateTime.now());
            data.setUpdateTime(LocalDateTime.now());
            boolean b = shopService.save(data);
            if (b) {
                sService.updateById(new Shop().setId(data.getShopId()).setMerchantId(data.getMerchantId()).setMerchantState(Code.ONE));
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        data.setUpdateTime(LocalDateTime.now());
        boolean b = shopService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询商户下的店铺
     * @param merchantId
     * @return
     */
    @RequestMapping("/getMerchantShop")
    public Object getMerchantShop(Integer merchantId){
        List<MerchantShop> shopList = shopService.list(new QueryWrapper<MerchantShop>().lambda()
                .eq(MerchantShop::getMerchantId, merchantId));
        List list = new ArrayList();
        Merchant merchant = merchantService.getById(merchantId);
        for (MerchantShop merchantShop : shopList) {
            Map objMap = MapUnite.getObjMap(merchantShop);
            Shop shop = sService.getById(merchantShop.getShopId());
            objMap.put("shopName",shop.getName());
            objMap.put("merchantName",merchant.getName());
            list.add(objMap);
        }
        return Result.ok(list);
    }

    /**
     * 删除商户下的店铺
     * @param id
     * @return
     */
    @RequestMapping("/delMerchantShopById")
    public Object delMerchantShopById(Integer id,Integer shopId){
        boolean b = shopService.removeById(id);
        if (b) {
            sService.updateById(new Shop().setId(shopId).setMerchantId(Code.zero).setMerchantState(Code.ZERO));
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 获取店铺详情
     * @param id
     * @return
     */
    @RequestMapping("/getMerchantShopById")
    public Object getMerchantShopById(Integer id){
        MerchantShop merchantShop = shopService.getById(id);
        Map map = MapUnite.getObjMap(merchantShop);
        Shop shop = sService.getById(merchantShop.getShopId());
        map.put("shopName",shop.getName());
        map.put("shopId",shop.getId());
        map.put("shopAddress",shop.getAddress());
        map.put("shopImgs",shop.getBanner().split(","));
        Merchant merchant = merchantService.getById(merchantShop.getMerchantId());
        map.put("merchantName",merchant.getName());
        map.put("merchantId",merchant.getId());
        Region region = regionService.getById(shop.getRegionId());
        RegionCircle circle = circleService.getById(shop.getCircleId());
        map.put("region",region.getName());
        map.put("circle",circle.getName());
        return Result.ok(map);
    }



}

