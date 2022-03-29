package com.service.impl;

import com.entity.Dynamic;
import com.mapper.DynamicMapper;
import com.service.IDynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户发布的动态 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-11
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements IDynamicService {

    @Autowired
    private DynamicMapper dynamicMapper;

    // 附近
    @Override
    public List<Dynamic> getDynamicLat(String latitude, String longitude, Integer page, Integer pagesize){
        return dynamicMapper.getDynamicLat(latitude,longitude,page,pagesize);
    }
}
