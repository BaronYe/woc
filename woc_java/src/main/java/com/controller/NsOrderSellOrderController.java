package com.controller;


import com.alibaba.fastjson.JSON;
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
import com.util.pay.WxPay;
import com.util.pay.WxPayConfiguration;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 盲盒商城订单 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-29
 */
@RestController
@RequestMapping("/nsOrderSellOrder")
@CrossOrigin
public class NsOrderSellOrderController {
    @Autowired
    private NsOrderSellServiceImpl sellService;
    @Autowired
    private NsOrderSellOrderServiceImpl sellOrderService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private BoxServiceImpl boxService;
    @Autowired
    private NsOrderServiceImpl orderService;
    @Autowired
    private NsOrderSellOrderAddressServiceImpl sellOrderAddressService;
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private AppletsServiceImpl appletsService;
    @Autowired
    private UserPointServiceImpl pointService;
    @Autowired
    private UserBalanceServiceImpl balanceService;
    @Autowired
    private NoticesGoodsServiceImpl noticesGoodsService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ItemSpecsServiceImpl specsService;
    @Autowired
    private BoxItemServiceImpl boxItemService;

    @Value("${yuming}")
    private String yuming;

    /**
     * 创建商城订单
     *
     * @param request
     * @return
     */
    @RequestMapping("/createSellOrder")
    public Object createSellOrder(HttpServletRequest request) {
        NsOrderSellOrder order = (NsOrderSellOrder) MapUnite.getEntity(Util.getJSONParam(request), NsOrderSellOrder.class);
        NsOrderSell nsOrderSell = sellService.getById(order.getSellId());
        if (nsOrderSell == null) {
            throw new MyException(ExceptionEnum.DATA_NULL);
        }
        if (nsOrderSell.getState().equals(Code.one)) {
            throw new MyException(ExceptionEnum.SELL_NULL);
        }
        Address address = addressService.getById(order.getAddressId());
        if (address == null) {
            throw new MyException(ExceptionEnum.ADDRESS_NOT);
        }
        NsOrder nsOrder = orderService.getById(nsOrderSell.getOrderId());
        if(nsOrder == null){
            throw new MyException(ExceptionEnum.NSORDER_NULL);
        }
        order.setBoxName(nsOrder.getBoxName());
        order.setBoxPicture(nsOrder.getBoxPicture());
        order.setBoxItemId(nsOrderSell.getBoxItemId());
        order.setBoxNum(Code.one);
        order.setOrderNo(Util.getOrderSn());
        order.setCreateTime(LocalDateTime.now());
        order.setOrderMoney(nsOrderSell.getPrice());
        order.setNsOrderId(nsOrderSell.getOrderId());
        Box box = boxService.getById(nsOrderSell.getBoxId());
        order.setBoxId(box.getId());
        order.setBoxName(box.getBoxName());
        order.setBoxPicture(box.getCover());
        Users user = usersService.getById(order.getUserId());
        if (order.getPaymentType() == 0) { // 是不是余额
            if (Float.parseFloat(user.getBalance()) < Float.parseFloat(order.getOrderMoney())) {
                throw new MyException(ExceptionEnum.USER_BANLANCE_ERROR);
            }
            order.setPayTime(LocalDateTime.now());
            order.setPayStatus(Code.one);
            order.setOrderStatus(Code.one);
        }
        boolean b = sellOrderService.save(order);
        if (b) {
            NsOrderSellOrderAddress intAddress = sellOrderAddressService.getOne(new QueryWrapper<NsOrderSellOrderAddress>()
                    .lambda().eq(NsOrderSellOrderAddress::getSellOrderId, order.getId()));
            if (intAddress == null) {
                sellOrderAddressService.save(new NsOrderSellOrderAddress()
                        .setAddressId(address.getId())
                        .setSellOrderId(order.getId())
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
            if (order.getPaymentType() == 1) {
                Map map = new HashMap();
                map.put("orderId", order.getId());
                map.put("isWeiXin", Code.one);
                return Result.ok(map);
            } else if (order.getPaymentType() == 0) {
                String balance = user.getBalance();
                String price = order.getOrderMoney();
                String bb = String.format("%.2f", (Float.parseFloat(balance) - Float.parseFloat(price)));
                // 减少用户余额
                usersService.updateById(new Users().setId(user.getId()).setBalance(bb));
                // 修改商城商品状态
                sellService.updateById(new NsOrderSell().setId(order.getSellId()).setState(Code.one));
                // 修改出售商品的订单列表
                orderService.updateById(new NsOrder().setOrderId(order.getNsOrderId()).setOrderStatus(Code.five));
                // 生成积分明细
                pointService.setUserPoint(Code.THREE,order.getUserId(),order.getOrderMoney());
                // 盲盒商城余额购买商品记录明细
                balanceService.save(new UserBalance()
                        .setTitle("购买商品")
                        .setPrice(order.getOrderMoney())
                        .setState(Code.ZERO)
                        .setCreateTime(LocalDateTime.now())
                        .setIsCashOut(Code.ZERO)
                        .setUserId(order.getUserId()));
            }
            Map map = new HashMap();
            map.put("orderId", order.getId());
            map.put("isWeiXin", Code.zero);
            return Result.ok(map);
        }
        throw new MyException(ExceptionEnum.ORDER_ERROR);
    }

    /**
     * 支付成功的回调
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = WxPay.notify(request, response);
        if (map != null) {
            Integer id = (Integer) map.get("id");
            String transactionId = String.valueOf(map.get("transactionId"));
            NsOrderSellOrder sellOrder = sellOrderService.getById(id);
            // 修改商城订单状态
            sellOrderService.updateById(new NsOrderSellOrder()
                    .setId(id)
                    .setTransactionId(transactionId)
                    .setPayTime(LocalDateTime.now())
                    .setOrderStatus(Code.one)
                    .setPayStatus(Code.one));
            // 修改商城商品状态
            sellService.updateById(new NsOrderSell().setId(id).setState(Code.one));
            //修改出售商品的订单列表
            orderService.updateById(new NsOrder().setOrderId(sellOrder.getNsOrderId()).setOrderStatus(Code.five));
            //生成积分明细
            pointService.setUserPoint(Code.THREE,sellOrder.getUserId(),sellOrder.getOrderMoney());
        }
    }

    /**
     * 去支付
     * @param id
     * @return
     */
    @RequestMapping("/toSellOrderPay")
    public Object toOrderPay(Integer id) {
        NsOrderSellOrder order = sellOrderService.getById(id);
        if (order != null) {
            Applets applets = appletsService.getById(1);
            if (applets.getAppId().equals(Code.KONG) || applets.getPayId().equals(Code.KONG) ||
                    applets.getPaySecret().equals("")) {
                throw new MyException(ExceptionEnum.APPID_NULL);
            }
            NsOrderSell nsOrderSell = sellService.getById(order.getSellId());
            if (nsOrderSell.getState().equals(Code.one)) {
                throw new MyException(ExceptionEnum.SELL_NULL);
            }
            Users u = usersService.getById(order.getUserId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            WxPayConfiguration wxPayConfiguration = new WxPayConfiguration();
            wxPayConfiguration.setMchid(applets.getPayId());
            wxPayConfiguration.setAppid(applets.getAppId());
            wxPayConfiguration.setMchsecret(applets.getPaySecret());
            wxPayConfiguration.setNonceStr(System.currentTimeMillis() + "");
            wxPayConfiguration.setBody("创建盲盒商城订单");
            wxPayConfiguration.setOutTradeNo(order.getOrderNo());
//      wxPayConfiguration.setTotalFee(1);
            wxPayConfiguration.setTotalFee(Util.yuanToFen(order.getOrderMoney()));
            wxPayConfiguration.setNotifyUrl(yuming + "/api/nsOrderSellOrder/notify");
            wxPayConfiguration.setOpenid(u.getOpenId());
            wxPayConfiguration.setAttach(JSON.toJSONString(map));
            wxPayConfiguration.setTradeType("JSAPI");
            return WxPay.createOrder(wxPayConfiguration);
        }
        throw new MyException(ExceptionEnum.ORDER_NULL);

    }

    /**
     * 查询商城订单
     *
     * @param page
     * @param pagesize
     * @param userId
     * @param orderNo
     * @param orderStatus
     * @return
     */
    @RequestMapping("/getSellOrder")
    public Object getSellOrder(Integer page, Integer pagesize, Integer userId, String orderNo,
                               Integer orderStatus, Integer paymentType) {
        Page<NsOrderSellOrder> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<NsOrderSellOrder> query = new QueryWrapper<NsOrderSellOrder>().lambda();
        query.orderByDesc(NsOrderSellOrder::getCreateTime);
        // 根据用户ID
        if (!Code.zero.equals(userId)) {
            query.eq(NsOrderSellOrder::getUserId, userId);
        }
        // 根据订单编号
        if (!Code.KONG.equals(orderNo)) {
            query.like(NsOrderSellOrder::getOrderNo, orderNo);
        }
        // 订单状态
        if (!Code.load_one.equals(orderStatus)) {
            query.eq(NsOrderSellOrder::getOrderStatus, orderStatus);
        }
        // 支付类型
        if (!Code.load_one.equals(paymentType)){
            query.eq(NsOrderSellOrder::getPaymentType,paymentType);
        }
        IPage iPage = sellOrderService.page(uPage, query);
        List<NsOrderSellOrder> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (NsOrderSellOrder data : dataList) {
            Map map = MapUnite.getMap(data);
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getId, data.getUserId()).select(Users::getAvatar, Users::getNickname));
            map.put("user", MapUnite.getObjMap(users));
            NsOrderSellOrderAddress address = sellOrderAddressService.getOne(new QueryWrapper<NsOrderSellOrderAddress>().lambda()
                    .eq(NsOrderSellOrderAddress::getSellOrderId, data.getId()));
            map.put("address",MapUnite.getObjMap(address));

            Map iMap = new HashMap();
            if (!Code.zero.equals(data.getBoxItemId())){
                BoxItem boxItem = boxItemService.getById(data.getBoxItemId());
                if (!Code.zero.equals(boxItem.getSpescId())){
                    ItemSpecs specs = specsService.getById(boxItem.getSpescId());
                    iMap.put("spesc",specs.getName());
                }else{
                    iMap.put("spesc","/");
                }
                Item item = itemService.getById(boxItem.getItemId());
                iMap.put("iName",item.getTitle());
                map.put("item",iMap);
            }else{
                map.put("item","");
            }
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询盲盒订单详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/getSellOrderById")
    public Object getSellOrderById(Integer id) {
        NsOrderSellOrder data = sellOrderService.getById(id);
        Map map = MapUnite.getMap(data);
        Users users = usersService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getId, data.getUserId())
                .select(Users::getAvatar, Users::getNickname));
        map.put("user", MapUnite.getObjMap(users));
        NsOrderSellOrderAddress address = sellOrderAddressService.getOne(new QueryWrapper<NsOrderSellOrderAddress>().lambda()
                .eq(NsOrderSellOrderAddress::getSellOrderId, data.getId()));
        map.put("address",MapUnite.getObjMap(address));
        Map iMap = new HashMap();
        if (!Code.zero.equals(data.getBoxItemId())){
            BoxItem boxItem = boxItemService.getById(data.getBoxItemId());
            if (!Code.zero.equals(boxItem.getSpescId())){
                ItemSpecs specs = specsService.getById(boxItem.getSpescId());
                iMap.put("spesc",specs.getName());
            }else{
                iMap.put("spesc","/");
            }
            Item item = itemService.getById(boxItem.getItemId());
            iMap.put("iName",item.getTitle());
            map.put("item",iMap);
        }else{
            map.put("item","");
        }
        return Result.ok(map);
    }

    /**
     * 修好盲盒商城订单信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/setSellOrder")
    public Object setSellOrder(HttpServletRequest request) {
        NsOrderSellOrder order = (NsOrderSellOrder) MapUnite.getEntity(Util.getJSONParam(request), NsOrderSellOrder.class);
        boolean b = sellOrderService.updateById(order);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 收货
     *
     * @param id
     * @return
     */
    @RequestMapping("/setSellTheGoods")
    public Object setSellTheGoods(Integer id) {
        boolean b = sellOrderService.updateById(new NsOrderSellOrder()
                .setId(id)
                .setOrderStatus(Code.three));
        if (b) {
            // 收货之后将钱打给出售方
            NsOrderSellOrder sellOrder = sellOrderService.getById(id);

            NsOrder order = orderService.getById(sellOrder.getNsOrderId());
            Users users = usersService.getById(order.getUserId());
            Double balance = Double.valueOf(users.getBalance());
            Double money = Double.valueOf(sellOrder.getOrderMoney());
            DecimalFormat df = new DecimalFormat("#.00");
            boolean id1 = usersService.updateById(new Users().setId(users.getId()).setBalance(df.format(balance + money)));
            if (id1) {
                // 记录余额明细
                balanceService.save(new UserBalance()
                        .setTitle("出售商品")
                        .setPrice(sellOrder.getOrderMoney())
                        .setState(Code.ONE)
                        .setCreateTime(LocalDateTime.now())
                        .setIsCashOut(Code.ZERO)
                        .setUserId(users.getId()));
                // 给出售方发送商品信息
                noticesGoodsService.save(new NoticesGoods()
                        .setOrderId(order.getOrderId())
                        .setCreateTime(LocalDateTime.now())
                        .setUserId(order.getUserId())
                        .setTypes(Code.one)
                        .setTitle("盲盒转售")
                        .setContent("出售成功")
                );
            }
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/delSellOrderById")
    public Object delSellOrderById(Integer id) {
        boolean b = sellOrderService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }
}

