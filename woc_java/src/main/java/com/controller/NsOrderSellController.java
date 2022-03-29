package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Box;
import com.entity.NsOrder;
import com.entity.NsOrderSell;
import com.enums.ExceptionEnum;
import com.service.impl.BoxServiceImpl;
import com.service.impl.NsOrderSellServiceImpl;
import com.service.impl.NsOrderServiceImpl;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import com.util.Util;
import com.vo.MyException;
import org.apache.http.protocol.HTTP;
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
 * 购买订单转出售 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/nsOrderSell")
@CrossOrigin
public class NsOrderSellController {
    @Autowired
    private NsOrderSellServiceImpl sellService;
    @Autowired
    private NsOrderServiceImpl orderService;
    @Autowired
    private BoxServiceImpl boxService;

    /**
     * 创建盲盒商城商品
     * @param request
     * @return
     */
    @RequestMapping("/creareSellShop")
    public Object creareSellShop(HttpServletRequest request){
        NsOrderSell sell = (NsOrderSell)MapUnite.getEntity(Util.getJSONParam(request), NsOrderSell.class);
        NsOrder order = orderService.getById(sell.getOrderId());
        Box box = boxService.getById(order.getBoxId());
        sell.setBoxClassifyId(box.getCid());
        sell.setBoxId(order.getBoxId());
        sell.setCreateTime(LocalDateTime.now());
        sell.setBoxCover(order.getBoxPicture());
        sell.setBoxItemId(order.getBoxItemId());
        boolean b = sellService.save(sell);
        if (b) {
            orderService.updateById(new NsOrder()
                    .setOrderStatus(Code.four)
                    .setIsSellState(Code.two)
                    .setOrderId(sell.getOrderId()));
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.ADD_INFO);
    }

    /**
     * 获取盲盒商城商品详情
     * @param id
     * @return
     */
    @RequestMapping("/getSellShopById")
    public Object getSellShopById(Integer id){
        NsOrderSell sell = sellService.getById(id);
        return Result.ok(MapUnite.getMap(sell));
    }

    /**
     * 查询盲盒商城商品列表
     * @param page
     * @param pagesize
     * @param cid
     * @return
     */
    @RequestMapping("/getSellShop")
    public Object getSellShop(Integer page,Integer pagesize,Integer cid,Integer state,String serachName){
        Page<NsOrderSell> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<NsOrderSell> query = new QueryWrapper<NsOrderSell>().lambda();
        query.orderByDesc(NsOrderSell::getCreateTime);
        // 盲盒分类
        if(!Code.load_one.equals(cid)){
            query.eq(NsOrderSell::getBoxClassifyId,cid);
        }
        // 出售状态
        if (!Code.load_one.equals(state)){
            query.eq(NsOrderSell::getState,state);
        }
        // 搜索值
        if (!Code.KONG.equals(serachName)){
            query.like(NsOrderSell::getTitle,serachName);
        }
        IPage iPage = sellService.page(uPage, query);
        List<NsOrderSell> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (NsOrderSell data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

}

