package com.service.impl;

import com.entity.MenuBar;
import com.mapper.MenuBarMapper;
import com.service.IMenuBarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 一级菜单栏（后台的导航栏） 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-05
 */
@Service
public class MenuBarServiceImpl extends ServiceImpl<MenuBarMapper, MenuBar> implements IMenuBarService {

}
