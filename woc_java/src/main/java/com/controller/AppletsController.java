package com.controller;


import com.entity.Applets;
import com.enums.ExceptionEnum;
import com.service.impl.AppletsServiceImpl;
import com.tool.ResponseResult;
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
 * 小程序信息配置 前端控制器
 * </p>
 *
 * @author simon
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/applets")
@CrossOrigin
@ResponseResult
public class AppletsController {
  @Autowired
  private AppletsServiceImpl appletsService;

  /**
   * @Description: //TODO 查询小程序配置
   * @param: []
   * @author: songxinyong
   * @date: 2020/11/5 09:32
   * @return: java.lang.Object
   */
  @RequestMapping("/getApplets")
  @Token.PassToken
  public Object getApplets(){
    Applets applets = appletsService.getById(Code.one);
    return  applets;
  }

  /**
   * @Description: //TODO 修改小程序配置
   * @param: [request]
   * @author: songxinyong
   * @date: 2020/11/5 09:32
   * @return: java.lang.Object
   */
  @RequestMapping("/setApplets")
  @Token.LoginToken
  public Object setApplets(HttpServletRequest request){
    Applets applets =  (Applets) MapUnite.getEntity(Util.getJSONParam(request), Applets.class);
    boolean b = appletsService.updateById(applets);
    if (b){
      return b;
    }
    throw new MyException(ExceptionEnum.UPDATE_INFO);
  }
}

