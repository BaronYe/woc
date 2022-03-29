package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Box;
import com.entity.NsOrder;
import com.entity.Shop;
import com.entity.Users;
import com.service.impl.BoxServiceImpl;
import com.service.impl.NsOrderServiceImpl;
import com.service.impl.ShopServiceImpl;
import com.service.impl.UsersServiceImpl;
import com.util.Code;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台系统的概况 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@RestController
@RequestMapping("/home")
@CrossOrigin
public class HomeController {
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private NsOrderServiceImpl nsOrderService;
    @Autowired
    private ShopServiceImpl shopService;
    @Autowired
    private BoxServiceImpl boxService;

    /**
     * 概况
     * @return
     */
    @RequestMapping("/doHome")
    public Object doHome(){
        Map map = new HashMap();
        int todayUserCount = usersService.count(new QueryWrapper<Users>()
                .apply("TO_DAYS(users.create_time) = TO_DAYS(NOW())"));
        map.put("todayUserCount",todayUserCount);
        int userCount = usersService.count();
        map.put("userCount",userCount);
        int todayOrderCount = nsOrderService.count(new QueryWrapper<NsOrder>().lambda()
                .ne(NsOrder::getOrderStatus, Code.zero)
                .apply("TO_DAYS(ns_order.create_time) = TO_DAYS(NOW())"));
        map.put("todayOrderCount",todayOrderCount);
        int orderCount = nsOrderService.count(new QueryWrapper<NsOrder>().lambda()
                .ne(NsOrder::getOrderStatus, Code.zero));
        map.put("orderCount",orderCount);
        int todayShopCount = shopService.count(new QueryWrapper<Shop>().lambda()
                .apply("TO_DAYS(shop.create_time) = TO_DAYS(NOW())"));
        map.put("todayShopCount",todayShopCount);
        int shopCount = shopService.count(new QueryWrapper<Shop>().lambda());
        map.put("shopCount",shopCount);
        int todayBoxCount = boxService.count(new QueryWrapper<Box>().lambda()
                .apply("TO_DAYS(box.create_time) = TO_DAYS(NOW())"));
        map.put("todayBoxCount",todayBoxCount);
        int boxCount = boxService.count(new QueryWrapper<Box>().lambda());
        map.put("boxCount",boxCount);

        int paymentCount = nsOrderService.count(new QueryWrapper<NsOrder>().lambda().eq(NsOrder::getOrderStatus, Code.zero));
        map.put("paymentCount",paymentCount);
        int sendCount = nsOrderService.count(new QueryWrapper<NsOrder>().lambda().eq(NsOrder::getOrderStatus, Code.one));
        map.put("sendCount",sendCount);
        int shouCount = nsOrderService.count(new QueryWrapper<NsOrder>().lambda().eq(NsOrder::getOrderStatus, Code.two));
        map.put("shouCount",shouCount);
        int groupCount = nsOrderService.count(new QueryWrapper<NsOrder>().lambda().eq(NsOrder::getOrderStatus, Code.six));
        map.put("groupCount",groupCount);

        Double a = new Double(Code.zero);
        List<NsOrder> orderList = nsOrderService.list(new QueryWrapper<NsOrder>().lambda()
                .ne(NsOrder::getOrderStatus, Code.zero)
                .select(NsOrder::getOrderId, NsOrder::getOrderMoney));
        for (NsOrder nsOrder : orderList) {
            a = a + new Double(nsOrder.getOrderMoney());
        }
        map.put("money",String.format("%.2f", a));
        return Result.ok(map);
    }

    /**
     * 获取7天订单数
     * @return
     */
    @RequestMapping("/getNsOrderCount")
    public Object getNsOrderCountList(){
        List<Map<String, Object>> list = nsOrderService.getNsOrderCountList();
        return Result.ok(list);
    }

}
