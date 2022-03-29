package com.service.impl;

import com.entity.BoxItem;
import com.mapper.BoxItemMapper;
import com.service.IBoxItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 盲盒下的商品 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-16
 */
@Service
public class BoxItemServiceImpl extends ServiceImpl<BoxItemMapper, BoxItem> implements IBoxItemService {

}
