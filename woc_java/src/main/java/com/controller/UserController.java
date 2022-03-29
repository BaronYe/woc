package com.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.enums.ExceptionEnum;
import com.service.impl.*;
import com.tool.Token;
import com.util.*;
import com.util.pay.HttpsUtil;
import com.util.pay.WxApi;
import com.vo.MyException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * <p>
 * 用户数据表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UsersServiceImpl userService;
    @Autowired
    private AppletsServiceImpl appletsService;
    @Autowired
    private UserPointServiceImpl pointService;
    @Autowired
    private UserCouponServiceImpl couponService;

    /**
     * @Description: //TODO 授权登录
     * @param: [request]
     * @author: songxinyong
     * @date: 2020/11/5 10:15
     * @return: java.lang.Object
     */
    @RequestMapping("/getOpenId")
    @Token.PassToken
    public Object getOpenId(String code) throws Exception {
        JSONObject json = findOpenId(code);
        if (Code.NULL == json.get("openid")) {
            return Result.fail(ExceptionEnum.APPID_ERROR);
        }
        Map map = new HashMap();
        String openid = json.get("openid").toString();
        map.put("sessionKey", json.get("session_key") == null ? "" : json.get("session_key").toString());
        map.put("openId", openid);
        map.put("unionId", json.get("union_id") == null ? "" : json.get("union_id").toString());
        map.put("expiresIn", json.get("expires_in") == null ? "" : json.get("expires_in").toString());
        Users user = userService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getOpenId, openid));
        if (Code.NULL != user) {
            map.put("user", MapUnite.getMap(user));
            // 判断用户每天登陆
            UserPoint point = pointService.getOne(new QueryWrapper<UserPoint>().lambda()
                    .eq(UserPoint::getUserId, user.getId())
                    .eq(UserPoint::getTypes,Code.TWO)
                    .apply("TO_DAYS(user_point.create_time) = TO_DAYS(NOW())"));
            if (point == null){
                pointService.setUserPoint(Code.TWO,user.getId(),Code.ZERO);
            }
        }
        return Result.ok(map);
    }

    /**
     * 注册用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @Token.PassToken
    public Object login(HttpServletRequest request) {
        Users u = (Users) MapUnite.getEntity(Util.getJSONParam(request), Users.class);
        if (u.getOpenId() == null || Code.KONG.equals(u.getOpenId())) {
            throw new MyException(ExceptionEnum.OPENID_ERROR);
        }
        Users user = userService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getOpenId, u.getOpenId()));
        if (Code.NULL == user) {
            u.setNickname(this.getRandomJianHan(3));
            u.setCreateTime(LocalDateTime.now());
            u.setUpdateTime(LocalDateTime.now());
            boolean b = userService.save(u);
            if (b) {
                // 判断用户注册
                UserPoint point = pointService.getOne(new QueryWrapper<UserPoint>().lambda()
                        .eq(UserPoint::getUserId, user.getId())
                        .eq(UserPoint::getTypes,Code.ONE));
                if (point == null){
                    pointService.setUserPoint(Code.ONE,user.getId(),Code.ZERO);
                }
                // 邀请好友获得积分
                if (!Code.zero.equals(u.getInviterId())){
                    pointService.setUserPoint(Code.SIX,u.getInviterId(),Code.ZERO);
                }
                return Result.ok(MapUnite.getObjMap(u));
            }
            throw new MyException(ExceptionEnum.USER_REGISTER_ERROR);
        }
//        u.setId(user.getId());
//        u.setNickname(u.getNickname());
//        u.setUpdateTime(LocalDateTime.now());
//        boolean b = userService.updateById(u);
//        if (b) {
            return Result.ok(MapUnite.getObjMap(user));
//        }
//        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }


    //自动生成名字（中文）
    public static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }

    /***
     * @Description: //TODO 授权获取手机号
     * @param: [encryptedData, code, iv]
     * @author: songxinyong
     * @date: 2020/11/5 10:14
     * @return: java.lang.Object
     */
    @RequestMapping("/getPhone")
    @Token.PassToken
    public Object getPhoneNumber(HttpServletRequest request) throws Exception {
        Users u = (Users) MapUnite.getEntity(Util.getJSONParam(request), Users.class);
        JSONObject json = findOpenId(u.getCode());
        String sessionKey;
        if (json.getString("openid") != null) {
            sessionKey = json.getString("session_key");
            String openid = json.getString("openid");
            String sr = WXBizDataCrypt.wxDecrypt(u.getEncryptedData(), sessionKey, u.getIv());
            if (sr == null) {
                throw new MyException(ExceptionEnum.SHOUQUAN_ERROR);
            }
            JSONObject jsonObject = JSON.parseObject(sr);
            String phoneNumber = jsonObject.getString("phoneNumber");
            Users user = userService.getOne(new QueryWrapper<Users>().lambda().eq(Users::getOpenId, openid));
            if (Code.NULL == user) {
                u.setCreateTime(LocalDateTime.now());
                u.setNickname(Util.base64Plus(u.getNickname()));
                u.setOpenId(openid);
                u.setMobile(phoneNumber);
                boolean b = userService.save(u);
                if (b) {
                    return Result.ok(MapUnite.getMap(u));
                }
                throw new MyException(ExceptionEnum.ADD_INFO);
            }
            return Result.ok(MapUnite.getMap(user));
        }
        throw new MyException(ExceptionEnum.APPID_ERROR);
    }

    /**
     * @Description: //TODO 通过uid获取用户信息
     * @param: [uid]
     * @author: songxinyong
     * @date: 2020/11/5 10:20
     * @return: java.lang.Object
     */
    @RequestMapping("/getUserById")
    @Token.PassToken
    public Object getUserByUid(int id) {
        Users user = userService.getById(id);
        if (user != Code.NULL) {
            Map map = MapUnite.getObjMap(user);
            int i = couponService.count(new QueryWrapper<UserCoupon>().lambda()
                    .eq(UserCoupon::getUserId, user.getId()).eq(UserCoupon::getState,Code.ZERO));
            map.put("couponCount",i);
            return Result.ok(map);
        } else
            throw new MyException(ExceptionEnum.DATA_NULL);
    }

    /**
     * @Description: //TODO 修改用户信息
     * @param: [request]
     * @author: songxinyong
     * @date: 2020/11/5 10:21
     * @return: java.lang.Object
     */
    @RequestMapping("/setUser")
    @Token.PassToken
    public Object setUser(HttpServletRequest request) {
        Users user = (Users) MapUnite.getEntity(Util.getJSONParam(request), Users.class);
        boolean b = userService.updateById(user);
        if (b) {
            // 判断用户注册
            UserPoint point = pointService.getOne(new QueryWrapper<UserPoint>().lambda()
                    .eq(UserPoint::getUserId, user.getId())
                    .eq(UserPoint::getTypes,Code.FOUR));
            if (point == null){
                pointService.setUserPoint(Code.FOUR,user.getId(),Code.ZERO);
            }
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * @Description: //TODO 获取用户列表
     * @param: [page]
     * @author: songxinyong
     * @date: 2020/11/5 10:22
     * @return: java.lang.Object
     */
    @GetMapping("/getUserList")
    @Token.LoginToken
    public Object getUserList(Integer page, Integer pagesize) {
        Page<Users> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Users> queryWrapper = new QueryWrapper<Users>().lambda();
        queryWrapper.orderByDesc(Users::getCreateTime);
        IPage iPage = userService.page(uPage, queryWrapper);
        List<Users> usersList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Users user : usersList) {
            list.add(MapUnite.getObjMap(user));
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /***
     * @Description: //TODO 解析openid
     * @param: [code]
     * @author: songxinyong
     * @date: 2020/11/5 10:13
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject findOpenId(String code) throws Exception {
        Applets applets = appletsService.getById(1);
        if (applets.getAppId().equals("") || applets.getAppSecret().equals("")) {
            throw new MyException(ExceptionEnum.APPID_NULL);
        }
        String params = String.format("appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", applets.getAppId(), applets.getAppSecret(), code);
        String sr = HttpsUtil.get(WxApi.jscode2session, params);
        return JSONObject.parseObject(sr);
    }

}

