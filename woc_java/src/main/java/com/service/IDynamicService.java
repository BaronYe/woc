package com.service;

import com.entity.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户发布的动态 服务类
 * </p>
 *
 * @author simon
 * @since 2021-06-11
 */
public interface IDynamicService extends IService<Dynamic> {
    List<Dynamic> getDynamicLat(String latitude, String longitude, Integer page, Integer pagesize);
}
