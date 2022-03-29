package com.controller;


import com.entity.Configs;
import com.enums.ExceptionEnum;
import com.service.impl.ConfigsServiceImpl;
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

/**
 * <p>
 * 基础配置 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/configs")
@CrossOrigin
public class ConfigsController {

    @Autowired
    private ConfigsServiceImpl configsService;

    /**
     * 查询基础配置
     * @return
     */
    @RequestMapping("/getConfigs")
    @Token.PassToken
    public Object getConfigs(){
        Configs configs = configsService.getById(Code.one);
        return Result.ok(configs);
    }

    /**
     * 修改基础配置
     * @param request
     * @return
     */
    @RequestMapping("/setConfigs")
    @Token.LoginToken
    public Object setApplets(HttpServletRequest request){
        Configs applets =  (Configs) MapUnite.getEntity(Util.getJSONParam(request), Configs.class);
        boolean b = configsService.updateById(applets);
        if (b){
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }
}

