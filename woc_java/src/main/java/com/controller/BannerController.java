package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Banner;
import com.enums.ExceptionEnum;
import com.service.impl.BannerServiceImpl;
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
 * 轮播图数据表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/banner")
@CrossOrigin
public class BannerController {
    @Autowired
    private BannerServiceImpl bannerService;

    /**
     * 添加修改轮播图
     * @param request
     * @return
     */
    @RequestMapping("/setBanner")
    @Token.LoginToken
    public Object setBanner(HttpServletRequest request){
        Banner banner =  (Banner) MapUnite.getEntity(Util.getJSONParam(request), Banner.class);
        if (banner.getId().equals(Code.zero)) {
            banner.setCreateTime(LocalDateTime.now());
            boolean b = bannerService.save(banner);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = bannerService.updateById(banner);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 获取轮播列表
     * @return
     */
    @RequestMapping("/getBanner")
    @Token.PassToken
    public Object getBanner(){
        List<Banner> bannerList = bannerService.list(new QueryWrapper<Banner>()
                .lambda()
                .orderByDesc(Banner::getCreateTime));
        return Result.ok(bannerList);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delBannerById")
    @Token.LoginToken
    public Object delBannerById(int id){
        boolean b = bannerService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }
}


