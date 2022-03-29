package com.service.impl;

import com.entity.Region;
import com.mapper.RegionMapper;
import com.service.IRegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地区 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

}
