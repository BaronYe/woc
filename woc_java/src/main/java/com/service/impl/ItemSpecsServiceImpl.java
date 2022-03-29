package com.service.impl;

import com.entity.ItemSpecs;
import com.mapper.ItemSpecsMapper;
import com.service.IItemSpecsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品规格 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-08
 */
@Service
public class ItemSpecsServiceImpl extends ServiceImpl<ItemSpecsMapper, ItemSpecs> implements IItemSpecsService {

}
