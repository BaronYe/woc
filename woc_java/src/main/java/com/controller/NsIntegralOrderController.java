package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.enums.ExceptionEnum;
import com.service.impl.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商城订单 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/nsIntegralOrder")
@CrossOrigin
public class NsIntegralOrderController {
    @Autowired
    private NsIntegralOrderServiceImpl nsIntegralOrderService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private IntegralGoodsServiceImpl goodsService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private NsIntegralOrderAddressServiceImpl intAddressService;
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private UserPointServiceImpl pointService;

    /**
     * 创建积分兑换订单
     * @param request
     * @return
     */
    @RequestMapping("/createIntegralOrder")
    public Object createIntegralOrder(HttpServletRequest request){
        NsIntegralOrder order =  (NsIntegralOrder) MapUnite.getEntity(Util.getJSONParam(request), NsIntegralOrder.class);
        order.setOrderNo(Util.getOrderSn());
        order.setNum(Code.one);
        order.setCreateTime(LocalDateTime.now());
        // 查看地址
        Address address = addressService.getById(order.getAddressId());
        if (address == null){
            throw new MyException(ExceptionEnum.ADDRESS_NOT);
        }
        // 查看积分商品
        IntegralGoods goods = goodsService.getById(order.getIntegralGoodsId());
        // 查看用户信息
        Users user = usersService.getById(order.getUserId());
        // 判断用户积分是否够兑换商品
        if ((goods.getTotalCount() *  order.getNum()) > user.getPoint()){
            throw new MyException(ExceptionEnum.POINT_NOT);
        }
        // 查看是不是限购
        Integer purchasing =  goods.getIsPurchasing();
        // 大于零则是限购
        if (purchasing > Code.zero){
            int i = nsIntegralOrderService.count(new QueryWrapper<NsIntegralOrder>().lambda()
                    .eq(NsIntegralOrder::getUserId, order.getUserId())
                    .eq(NsIntegralOrder::getIntegralGoodsId, order.getIntegralGoodsId()));
            // 判断用户购买后的数量是否超过限购数量或者 已经购买的+现在购买的数量 是否大于限购数量
            if (i >= purchasing || (i + order.getNum()) > purchasing){
                throw new MyException(ExceptionEnum.POINT_MAX);
            }
        }
        // 查看商品信息
        Item item = itemService.getById(goods.getItemId());
        order.setIntegralGoodsName(goods.getTitle());
        order.setIntegralGoodsPicture(item.getImgs().split(",")[0]);
        order.setPoint(goods.getTotalCount());
        order.setOrderStatus(Code.one);
        boolean b = nsIntegralOrderService.save(order);
        if (b) {
            // 积分扣除
            usersService.updateById(new Users()
                    .setId(user.getId())
                    .setPoint(user.getPoint() - (goods.getTotalCount() *  order.getNum())));
            NsIntegralOrderAddress intAddress = intAddressService.getOne(new QueryWrapper<NsIntegralOrderAddress>().lambda()
                    .eq(NsIntegralOrderAddress::getIntegralOrderId, order.getId()));
            if (intAddress == null){
                intAddressService.save(new NsIntegralOrderAddress()
                        .setAddressId(address.getId())
                        .setIntegralOrderId(order.getId())
                        .setUserId(address.getUserId())
                        .setReceiverCity(address.getCity())
                        .setReceiverMobile(address.getMobile())
                        .setReceiverName(address.getName())
                        .setReceiverDetailed(address.getDetailed())
                        .setReceiverProvince(address.getProvince())
                        .setReceiverRegion(address.getRegion())
                        .setReceiverZip(address.getPostalCode())
                        .setCreateTime(LocalDateTime.now()));
            }
            // 增加销量
            goodsService.updateById(new IntegralGoods()
                    .setId(goods.getId())
                    .setSales(goods.getSales() + order.getNum()));

            // 记录积分明细
            pointService.save(new UserPoint()
                    .setSign(Code.zero)
                    .setNum((goods.getTotalCount() *  order.getNum()))
                    .setTypes(Code.LOAD_ONE)
                    .setCreateTime(LocalDateTime.now())
                    .setUserId(order.getUserId())
                    .setTitle("积分兑换")
                    .setIsGet(Code.zero));
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.ORDER_ERROR);
    }

    /**
     * 查询积分订单
     * @param page
     * @param pagesize
     * @param userId
     * @param orderNo
     * @param orderStatus
     * @return
     */
    @RequestMapping("/getIntegralOrder")
    public Object getIntegralOrder(Integer page,Integer pagesize,Integer userId,String orderNo,Integer orderStatus){
        Page<NsIntegralOrder> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<NsIntegralOrder> query = new QueryWrapper<NsIntegralOrder>().lambda();
        query.orderByDesc(NsIntegralOrder::getCreateTime);
        // 根据用户ID
        if (!Code.zero.equals(userId)){
            query.eq(NsIntegralOrder::getUserId,userId);
        }
        // 根据订单编号
        if (!Code.KONG.equals(orderNo)){
            query.like(NsIntegralOrder::getOrderNo,orderNo);
        }
        // 订单状态
        if (!Code.load_one.equals(orderStatus)){
            query.eq(NsIntegralOrder::getOrderStatus,orderStatus);
        }
        IPage iPage = nsIntegralOrderService.page(uPage, query);
        List<NsIntegralOrder> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (NsIntegralOrder data : dataList) {
            Map map = MapUnite.getMap(data);
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getId, data.getUserId()).select(Users::getAvatar, Users::getNickname));
            map.put("user",MapUnite.getObjMap(users));
            NsIntegralOrderAddress address = intAddressService.getOne(new QueryWrapper<NsIntegralOrderAddress>().lambda()
                    .eq(NsIntegralOrderAddress::getIntegralOrderId, data.getId()));
            map.put("address",MapUnite.getObjMap(address));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询积分订单详情
     * @param id
     * @return
     */
    @RequestMapping("/getIntegralOrderById")
    public Object getIntegralOrderById(Integer id){
        NsIntegralOrder data = nsIntegralOrderService.getById(id);
        Map map = MapUnite.getMap(data);
        Users users = usersService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getId, data.getUserId()).select(Users::getAvatar, Users::getNickname));
        map.put("user",MapUnite.getObjMap(users));
        NsIntegralOrderAddress address = intAddressService.getOne(new QueryWrapper<NsIntegralOrderAddress>().lambda()
                .eq(NsIntegralOrderAddress::getIntegralOrderId, data.getId()));
        map.put("address",MapUnite.getObjMap(address));
        return Result.ok(map);
    }

    /**
     * 修好积分订单信息
     * @param request
     * @return
     */
    @RequestMapping("/setIntegralOrder")
    public Object setIntegralOrder(HttpServletRequest request){
        NsIntegralOrder order =  (NsIntegralOrder) MapUnite.getEntity(Util.getJSONParam(request), NsIntegralOrder.class);
        boolean b = nsIntegralOrderService.updateById(order);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @RequestMapping("/delIntegralOrderById")
    public Object delIntegralOrderById(Integer id){
        boolean b = nsIntegralOrderService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }
}


