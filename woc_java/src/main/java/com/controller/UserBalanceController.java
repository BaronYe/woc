package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Configs;
import com.entity.UserBalance;
import com.entity.Users;
import com.enums.ExceptionEnum;
import com.service.impl.ConfigsServiceImpl;
import com.service.impl.UserBalanceServiceImpl;
import com.service.impl.UsersServiceImpl;
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
 * 我的余额明细 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/balance")
@CrossOrigin
public class UserBalanceController {
    @Autowired
    private UserBalanceServiceImpl balanceService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private ConfigsServiceImpl configsService;

    /**
     * 申请提现
     * @param userId
     * @return
     */
    @RequestMapping("/setCashOut")
    public Object setCashOut(Integer userId){
        Users users = usersService.getById(userId);
        Configs configs = configsService.getById(Code.one);
        float b = Float.parseFloat(users.getBalance());
        float c = configs.getCashOutMoney();
        if (b >= c){
            String fei = String.format("%.2f", b * configs.getCashOutPoundage() / 100);
            String p = String.format("%.2f",b - Float.parseFloat(fei));
            UserBalance userBalance = new UserBalance()
                                    .setUserId(userId)
                                    .setCashOutCode(Util.getRandom())
                                    .setCashOutPoundage(fei)
                                    .setIsCashOut(Code.ONE)
                                    .setCreateTime(LocalDateTime.now())
                                    .setPrice(p)
                                    .setTitle("您申请了提现")
                                    .setState(Code.ZERO)
                                    .setCashOutState(Code.ZERO);
            boolean b1 = balanceService.save(userBalance);
            if (b1) {
                usersService.updateById(new Users().setId(userId).setBalance(Code.ZERO));
                return Result.ok(userBalance.getCashOutCode());
            }
            throw new MyException(ExceptionEnum.CASHOUT_ERROR);
        }
        throw new MyException(ExceptionEnum.BALANCE_NOT_ENOUGH);
    }

    /**
     * 转账
     * @param request
     * @return
     */
    @RequestMapping("/setUserBalance")
    public Object setUserBalance(HttpServletRequest request){
        UserBalance userBalance = (UserBalance) MapUnite.getEntity(Util.getJSONParam(request),UserBalance.class);
        userBalance.setCashOutState(Code.ONE);
        boolean b = balanceService.updateById(userBalance);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     *  获取明细的详情
     * @param id
     * @return
     */
    @RequestMapping("/getBalanceById")
    public Object getBalanceById(Integer id){
        UserBalance data = balanceService.getById(id);
        Map map = MapUnite.getMap(data);
        Users users = usersService.getById(data.getUserId());
        map.put("users",MapUnite.getObjMap(users));
        return Result.ok(map);
    }

    /**
     * 查看提现记录
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getBalanceAndAdmin")
    public Object getBalanceAndAdmin(Integer page,Integer pagesize,String codeName,String state){
        Page<UserBalance> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<UserBalance> queryWrapper = new QueryWrapper<UserBalance>().lambda();
        queryWrapper.eq(UserBalance::getIsCashOut,Code.ONE);
        if (!Code.KONG.equals(codeName)){
            queryWrapper.eq(UserBalance::getCashOutCode,codeName);
        }
        if (!Code.KONG.equals(state)){
            queryWrapper.eq(UserBalance::getCashOutState,state);
        }
        IPage iPage = balanceService.page(uPage, queryWrapper);
        List<UserBalance> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (UserBalance data : dataList) {
            Map map = MapUnite.getMap(data);
            Users users = usersService.getById(data.getUserId());
            map.put("users",MapUnite.getObjMap(users));
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 用户查询余额明细
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @RequestMapping("/getUserBalance")
    public Object getUserBalance(Integer page,Integer pagesize,Integer userId){
        Page<UserBalance> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<UserBalance> queryWrapper = new QueryWrapper<UserBalance>().lambda();
        queryWrapper.eq(UserBalance::getUserId,userId);
        IPage iPage = balanceService.page(uPage, queryWrapper);
        List<UserBalance> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (UserBalance data : dataList) {
            Map map = MapUnite.getMap(data);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 删除明细
     * @param id
     * @return
     */
    @RequestMapping("/delBalanceById")
    public Object delBalanceById(Integer id){
        boolean b = balanceService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }



}

