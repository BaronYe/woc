package com.service.impl;

import com.entity.Shop;
import com.mapper.ShopMapper;
import com.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 店铺 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-10
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

}
