package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Address;
import com.enums.ExceptionEnum;
import com.service.impl.AddressServiceImpl;
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
import java.util.List;

/**
 * <p>
 * 地址数据表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2020-11-18
 */
@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;

    /***
     * @Description: //TODO 添加和修改地址
     * @param: [request]
     * @author: songxinyong
     * @date: 2020/11/18 11:01
     * @return: java.lang.Object
     */
    @RequestMapping("/setAddress")
    @Token.PassToken
    public Object setAddress(HttpServletRequest request){
        Address data =  (Address) MapUnite.getEntity(Util.getJSONParam(request), Address.class);
        if (data.getIsDefault().equals(Code.one)){
            Address address = addressService.getOne(new QueryWrapper<Address>()
                    .lambda()
                    .eq(Address::getUserId, data.getUserId())
                    .eq(Address::getIsDefault,Code.one)
            );
            if (address != null){
                address.setIsDefault(Code.zero);
                addressService.updateById(address);
            }
        }
        if (data.getId().equals(Code.zero)) {
            data.setCreateTime(LocalDateTime.now());
            boolean b = addressService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = addressService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }


    /***
     * @Description: //TODO 根据用户查询地址
     * @param: [uid]
     * @author: songxinyong
     * @date: 2020/11/18 11:01
     * @return: java.lang.Object
     */
    @RequestMapping("/getAddressByUid")
    @Token.PassToken
    public Object getAddressByUid(int userId){
        List<Address> addressList = addressService.list(new QueryWrapper<Address>()
                .lambda()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault));
        return Result.ok(addressList);
    }

    /***
     * @Description: //TODO 删除地址
     * @param: [id]
     * @author: songxinyong
     * @date: 2020/11/18 11:01
     * @return: java.lang.Object
     */
    @RequestMapping("/delAddressById")
    @Token.PassToken
    public Object delAddressById(int id){
        boolean b = addressService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /***
     * @Description: //TODO 获取地址详情
     * @param: [id]
     * @author: songxinyong
     * @date: 2020/11/23 15:20
     * @return: java.lang.Object
     */
    @RequestMapping("/getAddressByid")
    @Token.PassToken
    public Object getAddressByid(int id){
        Address address = addressService.getById(id);
        return Result.ok(address);
    }

}

