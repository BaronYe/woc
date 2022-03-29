package com.mapper;

import com.entity.Dynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户发布的动态 Mapper 接口
 * </p>
 *
 * @author simon
 * @since 2021-06-11
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {
    List<Dynamic> getDynamicLat(String latitude,String longitude,Integer page,Integer pagesize);
}
