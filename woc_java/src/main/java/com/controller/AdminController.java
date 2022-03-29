package com.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Admin;
import com.enums.ExceptionEnum;
import com.service.impl.AdminServiceImpl;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员账号 前端控制器
 * </p>
 *
 * @author simon
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin
@ResponseResult
public class AdminController {
  @Autowired
  private AdminServiceImpl adminService;

  /**
   * @Description: //TODO 修改和添加管理员信息
   * @param: [request]
   * @author: songxinyong
   * @date: 2020/11/5 10:27
   * @return: java.lang.Object
   */
  @RequestMapping("/setAdmin")
  @Token.LoginToken

  public Object setAdmin(HttpServletRequest request){
    Admin admin =  (Admin) MapUnite.getEntity(Util.getJSONParam(request), Admin.class);
    if(Code.zero.equals(admin.getId())){
      if (Code.NULL == admin.getPassword()){
        admin.setPassword(SecureUtil.md5(admin.getPassword()));
      }
      admin.setCreateTime(LocalDateTime.now());
      boolean b = adminService.save(admin);
      if (b) {
        return b;
      }
      throw new MyException(ExceptionEnum.ADD_INFO);
    }
    Admin admin1 = adminService.getById(admin.getId());
    if (admin1.getPassword().equals(admin.getPassword())) {
      admin.setPassword(admin1.getPassword());
    }else{
      if (Code.NULL != admin.getPassword()){
        admin.setPassword(SecureUtil.md5(admin.getPassword()));
      }
    }
    boolean b = adminService.updateById(admin);
    if (b) {
      return b;
    }
    throw new MyException(ExceptionEnum.UPDATE_INFO);
  }

  /*** 
   * @Description: //TODO 后台系统的登录
   * @param: [request]
   * @author: songxinyong
   * @date: 2020/11/5 10:30 
   * @return: java.lang.Object
   */
  @RequestMapping("/login")
  @Token.PassToken
  public Object login(HttpServletRequest request){
    Admin a =  (Admin) MapUnite.getEntity(Util.getJSONParam(request), Admin.class);
    Admin admin = adminService.getOne(new QueryWrapper<Admin>()
            .lambda()
            .eq(Admin::getAccount, a.getAccount())
    );
    if (Code.NULL == admin){
      throw new MyException(ExceptionEnum.ACCOUNT_ERROR);
    }
    if(a.getPassword().equals(admin.getPassword())){
      String token = Token.getToken(admin);
      Map map = new HashMap();
      map.put("token",token);
      map.put("admin",MapUnite.getMap(admin));
      return map;
    }
    throw new MyException(ExceptionEnum.PASSWORD_ERROR);
  }

  /**
   * @Description: //TODO 查看管理员
   * @param: [page]
   * @author: songxinyong
   * @date: 2020/11/5 10:30
   * @return: java.lang.Object
   */
  @RequestMapping("/getAdmin")
  @Token.LoginToken
  public Object getAdmin(int page){
    Page<Admin> Page = new Page<>(page, Code.shi);
    IPage<Admin> iPage = adminService.page(Page);
    List<Admin> list = iPage.getRecords();
    List<Object> objects = new ArrayList<>();
    for (Admin admin : list) {
      objects.add(MapUnite.getMap(admin));
    }
    Map map = new HashMap();
    map.put("data", objects);
    map.put("count",(int) iPage.getTotal());
    return map;
  }

  /**
   * @Description: //TODO 删除
   * @param: [id]
   * @author: songxinyong
   * @date: 2020/11/5 10:30
   * @return: java.lang.Object
   */
  @RequestMapping("/delById")
  @Token.PassToken
  public Object delById(int id){
    boolean b = adminService.removeById(id);
    if (b) {
      return b;
    }
    throw new MyException(ExceptionEnum.DELETE_INFO);
  }
}

