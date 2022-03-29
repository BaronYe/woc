package com.service.impl;

import com.entity.Banner;
import com.mapper.BannerMapper;
import com.service.IBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 轮播图数据表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

}
