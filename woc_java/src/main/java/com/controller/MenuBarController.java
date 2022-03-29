package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.MenuBar;
import com.entity.MenuBarSon;
import com.service.impl.MenuBarServiceImpl;
import com.service.impl.MenuBarSonServiceImpl;
import com.util.MapUnite;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 一级菜单栏（后台的导航栏） 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-05
 */
@RestController
@RequestMapping("/menuBar")
@CrossOrigin
public class MenuBarController {
    @Autowired
    private MenuBarServiceImpl menuBarService;
    @Autowired
    private MenuBarSonServiceImpl sonService;

    /**
     * 获取所有的菜单栏
     *
     * @return
     */
    @RequestMapping("/getMenuBar")
    public Object getMenuBar() {
        List<MenuBar> barList = menuBarService.list();
        List list = new ArrayList();
        for (MenuBar menuBar : barList) {
            Map mMap = MapUnite.getMap(menuBar);
            List<MenuBarSon> sonList = sonService.list(new QueryWrapper<MenuBarSon>().lambda()
                    .eq(MenuBarSon::getMenuBarId, menuBar.getId()));
            mMap.put("subs",sonList);
            list.add(mMap);
        }
        return Result.ok(list);
    }
}

