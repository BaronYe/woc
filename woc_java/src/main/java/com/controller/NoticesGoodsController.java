package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.NoticesGoods;
import com.entity.NsOrder;
import com.entity.NsOrderAddress;
import com.entity.Users;
import com.service.impl.NoticesGoodsServiceImpl;
import com.service.impl.NsOrderSellOrderServiceImpl;
import com.service.impl.NsOrderServiceImpl;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品通知 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-07-09
 */
@RestController
@RequestMapping("/noticesGoods")
@CrossOrigin
public class NoticesGoodsController {
    @Autowired
    private NoticesGoodsServiceImpl noticesGoodsService;
    @Autowired
    private NsOrderServiceImpl nsOrderService;

    /**
     * 查询商品消息
     * @param userId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getNoticesGoods")
    public Object getNoticesGoods(Integer userId,Integer page,Integer pagesize){
        Page<NoticesGoods> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<NoticesGoods> query = new QueryWrapper<NoticesGoods>().lambda();
        query.orderByDesc(NoticesGoods::getCreateTime);
        // 根据用户ID
        if (!Code.zero.equals(userId)) {
            query.eq(NoticesGoods::getUserId, userId);
        }
        IPage iPage = noticesGoodsService.page(uPage, query);
        List<NoticesGoods> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (NoticesGoods data : dataList) {
            Map map = MapUnite.getMap(data);
            NsOrder order = nsOrderService.getOne(new QueryWrapper<NsOrder>().lambda()
                    .eq(NsOrder::getOrderId, data.getOrderId())
                    .select(NsOrder::getBoxPicture, NsOrder::getBoxName));
            map.put("order",MapUnite.getObjMap(order));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

}

