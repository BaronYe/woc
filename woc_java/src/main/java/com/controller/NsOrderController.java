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
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 订单管理 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
@RestController
@RequestMapping("/nsOrder")
@CrossOrigin
public class NsOrderController {
    @Autowired
    private NsOrderServiceImpl orderService;
    @Autowired
    private NsOrderAddressServiceImpl nsAddressService;
    @Autowired
    private BoxServiceImpl boxService;
    @Autowired
    private BoxItemServiceImpl boxItemService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private ConfigsServiceImpl configsService;
    @Autowired
    private AppletsServiceImpl appletsService;
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private UserPointServiceImpl pointService;
    @Autowired
    private UserBalanceServiceImpl balanceService;
    @Autowired
    private NsOrderGroupListServiceImpl groupListService;
    @Autowired
    private NoticesGoodsServiceImpl noticesGoodsService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ItemSpecsServiceImpl specsService;

    @Value("${yuming}")
    private String yuming;

    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    @RequestMapping("/createOrder")
    public Object createOrder(HttpServletRequest request) {
        //转化前端的数据
        NsOrder ord = (NsOrder) MapUnite.getEntity(Util.getJSONParam(request), NsOrder.class);
        // 查询盲盒数量
        Box box = boxService.getById(ord.getBoxId());
        //判断是否为空
        if (box == null) {
            throw new MyException(ExceptionEnum.DATA_NULL);
        }
        // 判断盲盒库存
        if (box.getCounts().equals(Code.zero)){
            throw new MyException(ExceptionEnum.ITEM_NOT);
        }
        ord.setBoxName(box.getBoxName());
        ord.setBoxPicture(box.getCover());
        ord.setBoxNum(Code.one);
        ord.setOrderNo(Util.getOrderSn());
        // 判断是不是拼单，拼单获取拼单价格
        if (ord.getIsTuangou() == 1) {
            ord.setOrderMoney(box.getSpellPrice());
        } else {
            ord.setOrderMoney(box.getPrice());
        }
        ord.setCreateTime(LocalDateTime.now());
        // 查询用户
        Users user = usersService.getById(ord.getUserId());
        // 判断是不是积分兑换
        if (ord.getPaymentType() == 2) {
            // 获取积分兑换金额的比例
            Configs configs = configsService.getById(Code.one);
            // 判断单个所需要的积分
            int point = (int) (Float.parseFloat(String.valueOf(configs.getIntegralDeduction())) * Float.parseFloat(box.getPrice()));
            // 判断用户积分够不够兑换盲盒
            if (point > user.getPoint()) {
                throw new MyException(ExceptionEnum.POINT_NOT);
            }
            ord.setPoint(point);
            ord.setPayTime(LocalDateTime.now());
            ord.setPayStatus(Code.one);
            ord.setOrderStatus(Code.one);
            ord.setIsTuangou(Code.zero);
        } else if (ord.getPaymentType() == 0) {// 是不是余额
            // 判断是不是拼单
            if (Float.parseFloat(user.getBalance()) < Float.parseFloat(ord.getOrderMoney())) {
                throw new MyException(ExceptionEnum.USER_BANLANCE_ERROR);
            } else {
                if (ord.getIsTuangou() == 1){
                    ord.setOrderStatus(Code.six);
                } else {
                    ord.setOrderStatus(Code.one);
                }
            }
            ord.setPayTime(LocalDateTime.now());
            ord.setPayStatus(Code.one);
        }
        //存储到数据库
        boolean b = orderService.save(ord);
        if (b) {
            // 0 余额 1 微信 2 积分
            if (ord.getPaymentType() == 1) {
                Map map = new HashMap();
                map.put("orderId", ord.getOrderId());
                map.put("isWeiXin", Code.one);
                return Result.ok(map);
            } else if (ord.getPaymentType() == 0) {
                // 判断是不是拼团
                if (Code.one.equals(ord.getIsTuangou())) {
                    // 创建成团列表
                    NsOrderGroupList groupList = new NsOrderGroupList();
                    groupList.setCreateTime(LocalDateTime.now());
                    groupList.setBoxId(ord.getBoxId());
                    groupList.setState(Code.zero);
                    groupList.setFromId(Code.zero);
                    groupList.setMainId(ord.getUserId());
                    groupList.setNum(Code.two);
                    groupList.setRemainNum(Code.one);
                    boolean save = groupListService.save(groupList);
                    if (save) {
                        orderService.updateById(new NsOrder().setOrderId(ord.getOrderId()).setGroupId(groupList.getId()));
                    }
                }
                // 盲盒机余额购买商品记录明细
                balanceService.save(new UserBalance()
                        .setTitle("购买商品")
                        .setPrice(ord.getOrderMoney())
                        .setState(Code.ZERO)
                        .setCreateTime(LocalDateTime.now())
                        .setIsCashOut(Code.ZERO)
                        .setUserId(ord.getUserId()));

                String balance = user.getBalance();
                String price = ord.getOrderMoney();
                String bb = String.format("%.2f", (Float.parseFloat(balance) - Float.parseFloat(price)));
                // 扣除余额
                usersService.updateById(new Users().setId(user.getId()).setBalance(bb));
                // 获得积分记录
                pointService.setUserPoint(Code.THREE, user.getId(), ord.getOrderMoney());
            } else if (ord.getPaymentType() == 2) {
                // 扣除用户积分
                usersService.updateById(new Users().setId(user.getId()).setPoint(user.getPoint() - ord.getPoint()));
            }

            // 给出售方发送商品信息
            noticesGoodsService.save(new NoticesGoods()
                    .setOrderId(ord.getOrderId())
                    .setCreateTime(LocalDateTime.now())
                    .setUserId(ord.getUserId())
                    .setTypes(Code.zero)
                    .setTitle("购物")
                    .setContent("下单成功")
            );
            // 添加销量
            boxService.updateById(new Box()
                    .setId(ord.getBoxId())
                    .setCounts(box.getCounts() - Code.one)
                    .setSales(box.getSales() + Code.one));

            Map map = new HashMap();
            map.put("orderId", ord.getOrderId());
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
            Integer uid = (Integer) map.get("uid");
            String transactionId = String.valueOf(map.get("transactionId"));

            NsOrder order = orderService.getById(id);

            orderService.updateById(new NsOrder().setOrderId(id)
                    .setTransactionId(transactionId)
                    .setPayTime(LocalDateTime.now())
                    .setOrderStatus(Code.six)
                    .setPayStatus(Code.one));

            //   判断是不是拼单
            if (Code.one.equals(order.getIsTuangou())) {
                // 创建成团列表
                NsOrderGroupList groupList = new NsOrderGroupList();
                groupList.setCreateTime(LocalDateTime.now());
                groupList.setBoxId(order.getBoxId());
                groupList.setState(Code.zero);
                groupList.setFromId(Code.zero);
                groupList.setMainId(order.getUserId());
                groupList.setNum(Code.two);
                groupList.setRemainNum(Code.one);
                boolean save = groupListService.save(groupList);
                if (save) {
                    orderService.updateById(new NsOrder().setOrderId(order.getOrderId()).setGroupId(groupList.getId()));
                }
            }

            // 给出售方发送商品信息
            noticesGoodsService.save(new NoticesGoods()
                    .setOrderId(order.getOrderId())
                    .setCreateTime(LocalDateTime.now())
                    .setUserId(order.getUserId())
                    .setTypes(Code.zero)
                    .setTitle("购物")
                    .setContent("下单成功")
            );

            Box box = boxService.getById(order.getBoxId());
            if (box != null){
                // 添加销量
                boxService.updateById(new Box()
                        .setId(box.getId())
                        .setCounts(box.getCounts() - Code.one)
                        .setSales(box.getSales() + Code.one));
            }
            pointService.setUserPoint(Code.THREE, uid, order.getOrderMoney());
        }
    }

    /**
     * 去支付
     *
     * @param id
     * @return
     */
    @RequestMapping("/toOrderPay")
    public Object toOrderPay(Integer id) {
        NsOrder order = orderService.getById(id);
        if (order != null) {
            Applets applets = appletsService.getById(1);
            if (applets.getAppId().equals(Code.KONG) || applets.getPayId().equals(Code.KONG) ||
                    applets.getPaySecret().equals("")) {
                throw new MyException(ExceptionEnum.APPID_NULL);
            }
            Users u = usersService.getById(order.getUserId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("uid", order.getUserId());
            WxPayConfiguration wxPayConfiguration = new WxPayConfiguration();
            wxPayConfiguration.setMchid(applets.getPayId());
            wxPayConfiguration.setAppid(applets.getAppId());
            wxPayConfiguration.setMchsecret(applets.getPaySecret());
            wxPayConfiguration.setNonceStr(System.currentTimeMillis() + "");
            wxPayConfiguration.setBody("创建盲盒订单");
            wxPayConfiguration.setOutTradeNo(order.getOrderNo());
//      wxPayConfiguration.setTotalFee(1);
            wxPayConfiguration.setTotalFee(Util.yuanToFen(order.getOrderMoney()));
            wxPayConfiguration.setNotifyUrl(yuming + "/api/nsOrder/notify");
            wxPayConfiguration.setOpenid(u.getOpenId());
            wxPayConfiguration.setAttach(JSON.toJSONString(map));
            wxPayConfiguration.setTradeType("JSAPI");
            return WxPay.createOrder(wxPayConfiguration);
        }
        throw new MyException(ExceptionEnum.ORDER_NULL);

    }

    /**
     * 查询盲盒下拼单订单列表
     *
     * @param boxId
     * @return
     */
    @RequestMapping("/getPinTuanList")
    public Object getPinTuanList(Integer boxId) {
        List<NsOrderGroupList> groupList = groupListService.list(new QueryWrapper<NsOrderGroupList>().lambda()
                .eq(NsOrderGroupList::getBoxId, boxId)
                .eq(NsOrderGroupList::getState, Code.zero));
        List list = new ArrayList();
        for (NsOrderGroupList data : groupList) {
            Map map = MapUnite.getObjMap(data);
            List<NsOrder> orderList = orderService.list(new QueryWrapper<NsOrder>().lambda()
                    .eq(NsOrder::getGroupId, data.getId())
                    .eq(NsOrder::getPayStatus, Code.one)
                    .select(NsOrder::getUserId, NsOrder::getOrderId));
            List userList = new ArrayList();
            for (NsOrder nsOrder : orderList) {
                Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                        .eq(Users::getId, nsOrder.getUserId())
                        .select(Users::getAvatar, Users::getId, Users::getNickname));
                userList.add(MapUnite.getObjMap(users));
            }
            map.put("userList", userList);
            list.add(map);
        }
        return Result.ok(list);
//        List<NsOrder> orderList = orderService.list(new QueryWrapper<NsOrder>().lambda()
//                .eq(NsOrder::getBoxId, boxId)
//                .eq(NsOrder::getIsTuangou, Code.one)
//                .eq(NsOrder::getOrderStatus, Code.six)
//                .eq(NsOrder::getTuangouUserId, Code.zero)
//                .eq(NsOrder::getPayStatus, Code.one)
//                .select(NsOrder::getOrderId, NsOrder::getUserId));
//        List list = new ArrayList();
//        for (NsOrder nsOrder : orderList) {
//            Map map = MapUnite.getObjMap(nsOrder);
//            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
//                    .eq(Users::getId, nsOrder.getUserId()).select(Users::getAvatar, Users::getId, Users::getNickname));
//            map.put("users", MapUnite.getObjMap(users));
//            list.add(map);
//        }
    }

    /**
     * 获取盲盒订单信息
     *
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @RequestMapping("/getNsOrder")
    public Object getNsOrder(Integer page, Integer pagesize, Integer userId, String orderNo, Integer paymentType,
                             Integer orderStatus, Integer isSellState, Integer isTuangou) {
        Page<NsOrder> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<NsOrder> query = new QueryWrapper<NsOrder>().lambda();
        query.orderByDesc(NsOrder::getCreateTime);
        // 根据用户ID
        if (!Code.zero.equals(userId)) {
            query.eq(NsOrder::getUserId, userId);
        }
        // 根据订单编号
        if (!Code.KONG.equals(orderNo)) {
            query.like(NsOrder::getOrderNo, orderNo);
        }
        // 支付类型
        if (!Code.load_one.equals(paymentType)) {
            query.eq(NsOrder::getPaymentType, paymentType);
        }
        // 订单状态
        if (!Code.load_one.equals(orderStatus)) {
            query.eq(NsOrder::getOrderStatus, orderStatus);
        }
        // 出售状态
        if (!Code.load_one.equals(isSellState)) {
            query.eq(NsOrder::getIsSellState, isSellState);
        }
        // 是否拼单
        if (!Code.load_one.equals(isTuangou)) {
            query.eq(NsOrder::getIsTuangou, isTuangou);
        }
        IPage iPage = orderService.page(uPage, query);
        List<NsOrder> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (NsOrder data : dataList) {
            Map map = MapUnite.getMap(data);
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getId, data.getUserId()).select(Users::getAvatar, Users::getNickname));
            map.put("user", MapUnite.getObjMap(users));
            NsOrderAddress address = nsAddressService.getOne(new QueryWrapper<NsOrderAddress>().lambda().eq(NsOrderAddress::getOrderId, data.getOrderId()));
            map.put("address", address);
            Map iMap = new HashMap();
            if (!Code.zero.equals(data.getBoxItemId())){
                BoxItem boxItem = boxItemService.getById(data.getBoxItemId());
                Item item = itemService.getById(boxItem.getItemId());
                if (!Code.zero.equals(boxItem.getSpescId())){
                    ItemSpecs specs = specsService.getById(boxItem.getSpescId());
                    iMap.put("spesc",specs.getName());
                    iMap.put("spescImg",specs.getImg());
                }else{
                    iMap.put("spesc","/");
                    if (item.getImgs() != null){
                        iMap.put("spescImg",item.getImgs().split(",")[0]);
                    }else{
                        iMap.put("spescImg","");
                    }
                }
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
     * 查询详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/getOrderById")
    public Object getOrderById(Integer orderId) {
        NsOrder data = orderService.getById(orderId);
        Map map = MapUnite.getMap(data);
        Users users = usersService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getId, data.getUserId()).select(Users::getAvatar, Users::getNickname));
        map.put("user", MapUnite.getObjMap(users));
        NsOrderAddress address = nsAddressService.getOne(new QueryWrapper<NsOrderAddress>().lambda().eq(NsOrderAddress::getOrderId, data.getOrderId()));
        map.put("address", address);
        Map iMap = new HashMap();
        if (!Code.zero.equals(data.getBoxItemId())){
//            BoxItem boxItem = boxItemService.getById(data.getBoxItemId());
//            if (!Code.zero.equals(boxItem.getSpescId())){
//                ItemSpecs specs = specsService.getById(boxItem.getSpescId());
//                iMap.put("spesc",specs.getName());
//            }
//            Item item = itemService.getById(boxItem.getItemId());
//            iMap.put("iName",item.getTitle());

            BoxItem boxItem = boxItemService.getById(data.getBoxItemId());
            Item item = itemService.getById(boxItem.getItemId());
            if (!Code.zero.equals(boxItem.getSpescId())){
                ItemSpecs specs = specsService.getById(boxItem.getSpescId());
                iMap.put("spesc",specs.getName());
                iMap.put("spescImg",specs.getImg());
            }else{
                iMap.put("spesc","/");
                if (item.getImgs() != null){
                    iMap.put("spescImg",item.getImgs().split(",")[0]);
                }else{
                    iMap.put("spescImg","");
                }
            }
            iMap.put("iName",item.getTitle());

            map.put("item",iMap);
        }else{
            map.put("item","");
        }
        return Result.ok(map);
    }

    /**
     * 邮寄
     *
     * @param orderId
     * @param addressId
     * @return
     */
    @RequestMapping("/setMail")
    public Object setMail(Integer orderId, Integer addressId) {
        boolean b = orderService.updateById(new NsOrder().setIsSellState(Code.one).setOrderId(orderId));
        Address address = addressService.getById(addressId);
        if (b) {
            NsOrderAddress address1 = nsAddressService.getOne(new QueryWrapper<NsOrderAddress>().lambda().eq(NsOrderAddress::getOrderId, orderId));
            if (address1 == null) {
                nsAddressService.save(new NsOrderAddress()
                        .setAddressId(addressId)
                        .setOrderId(orderId)
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
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 暂存
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/setStaging")
    public Object setStaging(@NotNull(message = "订单ID不能为空") Integer orderId) {
        boolean b = orderService.updateById(new NsOrder().setIsSellState(Code.three).setOrderId(orderId));
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 发货
     *
     * @param com
     * @param name
     * @param orderId
     * @return
     */
    @RequestMapping("/setShipping")
    public Object setShipping(String com, String name, Integer orderId) {
        boolean b = orderService.updateById(new NsOrder()
                .setOrderId(orderId)
                .setShippingStatus(Code.one)
                .setShippingExpress(name)
                .setOrderStatus(Code.two)
                .setShippingCom(com));
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 收货
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/setTheGoods")
    public Object setTheGoods(Integer orderId) {
        boolean b = orderService.updateById(new NsOrder()
                .setOrderId(orderId)
                .setOrderStatus(Code.three));
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 删除订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/delNsOderById")
    public Object delNsOderById(Integer orderId) {
        boolean b = orderService.removeById(orderId);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DATA_NULL);
    }

    /**
     * 去拼单
     *
     * @param groupId
     * @param userId
     * @param paymentType
     * @return
     */
    @RequestMapping("/doSpellOrder")
    public Object doSpellOrder(Integer groupId, Integer userId, Integer paymentType, Integer boxId) {
        // 获取拼团信息
        NsOrderGroupList group = groupListService.getById(groupId);

        NsOrder nsOrder = new NsOrder();
        Box box = boxService.getById(boxId);
        if (box == null) {
            throw new MyException(ExceptionEnum.DATA_NULL);
        }
        if (box.getCounts().equals(Code.zero)){
            throw new MyException(ExceptionEnum.ITEM_NOT);
        }
        nsOrder.setBoxName(box.getBoxName());
        nsOrder.setBoxPicture(box.getCover());
        nsOrder.setBoxNum(Code.one);
        nsOrder.setOrderNo(Util.getOrderSn());
        nsOrder.setOrderMoney(box.getSpellPrice());
        nsOrder.setCreateTime(LocalDateTime.now());
        nsOrder.setIsTuangou(Code.one);
        nsOrder.setPaymentType(paymentType);
        nsOrder.setUserId(userId);
        nsOrder.setGroupId(groupId);
        Users user = usersService.getById(nsOrder.getUserId());
        // 余额支付
        if (nsOrder.getPaymentType() == 0) {
            if (Float.parseFloat(user.getBalance()) < Float.parseFloat(box.getSpellPrice())) {
                throw new MyException(ExceptionEnum.USER_BANLANCE_ERROR);
            }
            nsOrder.setOrderStatus(Code.one);
            nsOrder.setPayTime(LocalDateTime.now());
        }
        boolean b = orderService.save(nsOrder);
        if (b) {
            // 微信支付
            if (nsOrder.getPaymentType() == 1) {
                Map map = new HashMap();
                map.put("orderId", nsOrder.getOrderId());
                map.put("isWeiXin", Code.one);
                return Result.ok(map);
            }
            // 判断该团人数有没有完成
            if (group.getRemainNum() > Code.one) {
                // 修改订单状态和添加拼团ID
                orderService.updateById(new NsOrder().setOrderId(nsOrder.getOrderId())
                        .setOrderStatus(Code.six)
                        .setPayStatus(Code.one));
                //修改成团信息的修改
                groupListService.updateById(new NsOrderGroupList().setId(groupId)
                        .setRemainNum(group.getRemainNum() - Code.one));
            } else {
                // 修改订单状态和添加拼团ID
                orderService.updateById(new NsOrder().setOrderId(nsOrder.getOrderId())
                        .setOrderStatus(Code.one)
                        .setPayStatus(Code.one));
                //修改成团信息的修改
                groupListService.updateById(new NsOrderGroupList().setId(groupId)
                        .setRemainNum(group.getRemainNum() - Code.one)
                        .setState(Code.one));
                // 该团完成之后修改其他成员的订单信息
                List<NsOrder> nsOrderList = orderService.list(new QueryWrapper<NsOrder>().lambda()
                        .eq(NsOrder::getPayStatus, Code.one)
                        .eq(NsOrder::getOrderStatus, Code.six)
                        .eq(NsOrder::getGroupId, group.getId())
                        .select(NsOrder::getOrderId));
                for (NsOrder o : nsOrderList) {
                    orderService.updateById(new NsOrder()
                            .setOrderStatus(Code.one)
                            .setOrderId(o.getOrderId()));
                }
            }
            // 余额支付
            String balance = user.getBalance();
            String price = nsOrder.getOrderMoney();
            String bb = String.format("%.2f", (Float.parseFloat(balance) - Float.parseFloat(price)));
            // 扣除余额
            usersService.updateById(new Users().setId(user.getId()).setBalance(bb));
            // 生成积分明细
            pointService.setUserPoint(Code.THREE, user.getId(), nsOrder.getOrderMoney());
            // 盲盒机余额购买商品记录明细
            balanceService.save(new UserBalance()
                    .setTitle("购买商品")
                    .setPrice(nsOrder.getOrderMoney())
                    .setState(Code.ZERO)
                    .setCreateTime(LocalDateTime.now())
                    .setIsCashOut(Code.ZERO)
                    .setUserId(nsOrder.getUserId()));

            // 给出售方发送商品信息
            noticesGoodsService.save(new NoticesGoods()
                    .setOrderId(nsOrder.getOrderId())
                    .setCreateTime(LocalDateTime.now())
                    .setUserId(nsOrder.getUserId())
                    .setTypes(Code.zero)
                    .setTitle("购物")
                    .setContent("下单成功")
            );
            // 添加盲盒销量
            boxService.updateById(new Box()
                    .setId(box.getId())
                    .setCounts(box.getCounts() - Code.one)
                    .setSales(box.getSales() + Code.one));
            Map map = new HashMap();
            map.put("orderId", nsOrder.getOrderId());
            map.put("isWeiXin", Code.zero);
            return Result.ok(map);
        }
        throw new MyException(ExceptionEnum.ORDER_ERROR);

    }

    /**
     * 拼单去支付
     *
     * @param id
     * @return
     */
    @RequestMapping("/toSpellOrderPay")
    public Object toSpellOrderPay(Integer id) {
        NsOrder order = orderService.getById(id);
        if (order != null) {
            Applets applets = appletsService.getById(1);
            if (applets.getAppId().equals(Code.KONG) || applets.getPayId().equals(Code.KONG) ||
                    applets.getPaySecret().equals("")) {
                throw new MyException(ExceptionEnum.APPID_NULL);
            }
            Users u = usersService.getById(order.getUserId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("uid", order.getUserId());
            WxPayConfiguration wxPayConfiguration = new WxPayConfiguration();
            wxPayConfiguration.setMchid(applets.getPayId());
            wxPayConfiguration.setAppid(applets.getAppId());
            wxPayConfiguration.setMchsecret(applets.getPaySecret());
            wxPayConfiguration.setNonceStr(System.currentTimeMillis() + "");
            wxPayConfiguration.setBody("创建盲盒订单");
            wxPayConfiguration.setOutTradeNo(order.getOrderNo());
//      wxPayConfiguration.setTotalFee(1);
            wxPayConfiguration.setTotalFee(Util.yuanToFen(order.getOrderMoney()));
            wxPayConfiguration.setNotifyUrl(yuming + "/api/nsOrder/spellNotify");
            wxPayConfiguration.setOpenid(u.getOpenId());
            wxPayConfiguration.setAttach(JSON.toJSONString(map));
            wxPayConfiguration.setTradeType("JSAPI");
            return WxPay.createOrder(wxPayConfiguration);
        }
        throw new MyException(ExceptionEnum.ORDER_NULL);

    }

    // 拼单支付的回调
    @RequestMapping("/spellNotify")
    public void spellNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = WxPay.notify(request, response);
        if (map != null) {
            Integer id = (Integer) map.get("id");
            String transactionId = String.valueOf(map.get("transactionId"));
            NsOrder order = orderService.getById(id);

            // 给出售方发送商品信息
            noticesGoodsService.save(new NoticesGoods()
                    .setOrderId(order.getOrderId())
                    .setCreateTime(LocalDateTime.now())
                    .setUserId(order.getUserId())
                    .setTypes(Code.zero)
                    .setTitle("购物")
                    .setContent("下单成功")
            );

            Box box = boxService.getById(order.getBoxId());
            if (box != null){
                // 添加销量
                boxService.updateById(new Box()
                        .setId(box.getId())
                        .setCounts(box.getCounts() - Code.one)
                        .setSales(box.getSales() + Code.one));
            }

            // 生成积分明细
            pointService.setUserPoint(Code.THREE, order.getUserId(), order.getOrderMoney());

//            orderService.updateById(new NsOrder().setOrderId(id)
//                    .setTransactionId(transactionId)
//                    .setPayTime(LocalDateTime.now())
//                    .setOrderStatus(Code.one)
//                    .setPayStatus(Code.one));


//            orderService.updateById(new NsOrder()
//                    .setOrderId(order.getTuangouOrderId())
//                    .setOrderStatus(Code.one)
//                    .setTuangouUserId(order.getUserId()));

            NsOrderGroupList group = groupListService.getById(order.getGroupId());

            // 判断该团人数有没有完成
            if (group.getRemainNum() > Code.one) {
                // 修改订单状态和添加拼团ID
                orderService.updateById(new NsOrder()
                        .setOrderId(id)
                        .setOrderStatus(Code.six)
                        .setPayStatus(Code.one)
                        .setGroupId(group.getId()));
                //修改成团信息的修改
                groupListService.updateById(new NsOrderGroupList()
                        .setId(group.getId())
                        .setRemainNum(group.getRemainNum() - Code.one));
            } else {
                // 修改订单状态和添加拼团ID
                orderService.updateById(new NsOrder()
                        .setOrderId(id)
                        .setOrderStatus(Code.one)
                        .setPayStatus(Code.one));
                //修改成团信息的修改
                groupListService.updateById(new NsOrderGroupList()
                        .setId(order.getGroupId())
                        .setRemainNum(group.getRemainNum() - Code.one)
                        .setState(Code.one));
                // 该团完成之后修改其他成员的订单信息
                List<NsOrder> nsOrderList = orderService.list(new QueryWrapper<NsOrder>().lambda()
                        .eq(NsOrder::getPayStatus, Code.one)
                        .eq(NsOrder::getOrderStatus, Code.six)
                        .eq(NsOrder::getGroupId, group.getId())
                        .select(NsOrder::getOrderId));
                for (NsOrder nsOrder : nsOrderList) {
                    orderService.updateById(new NsOrder().setOrderStatus(Code.one).setOrderId(nsOrder.getOrderId()));
                }
            }

        }
    }

    /**
     * 开盲盒
     * @param orderId
     * @return
     */
    @RequestMapping("/noBoxItem")
    public Object noBoxItem(Integer orderId){
        NsOrder nsOrder = orderService.getById(orderId);
        if (!nsOrder.getBoxItemId().equals(Code.zero)){
            throw new MyException(ExceptionEnum.BOX_ITEM_NOT);
        }
        List<BoxItem> boxItemList = boxItemService.list(new QueryWrapper<BoxItem>().lambda().eq(BoxItem::getBoxId, nsOrder.getBoxId()));
        // 存储概率
        List<Double> orignalRates = new ArrayList<>(boxItemList.size());
        for (BoxItem data : boxItemList) {
            double probability = new Double(data.getProbability()) / 100;
            if (probability < 0) {
                probability = 0;
            }
            orignalRates.add(probability);
        }
        // 中奖商品索引
        int orignalIndex = lottery(orignalRates);
        // 拿到对应的商品
        BoxItem boxItem = boxItemList.get(orignalIndex);

        Item item = itemService.getOne(new QueryWrapper<Item>().lambda()
                .eq(Item::getId,boxItem.getItemId())
                .select(Item::getTitle,Item::getPrice,Item::getImgs,Item::getSpecialPrice,Item::getId));

        // 修改盲盒开箱状态
        boolean b = orderService.updateById(new NsOrder()
                .setOrderId(orderId)
                .setBoxItemId(boxItem.getId()));
        //.setBoxPicture(item.getImgs().split(",")[0])

        if (b) {
            return Result.ok(MapUnite.getMap(item));
        }
        throw new MyException(ExceptionEnum.BOX_NO_ITEM);
    }

    /**
     * 抽奖方法
     * <br/>
     * create by: leigq
     * <br/>
     * create time: 2019/7/5 23:08
     * @param orignalRates 商品中奖概率列表，保证顺序和实际物品对应
     * @return 中奖商品索引
     */
    public static int lottery(List<Double> orignalRates) {

        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }
}
