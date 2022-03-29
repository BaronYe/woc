package com.service.impl;

import com.entity.NsOrderItem;
import com.mapper.NsOrderItemMapper;
import com.service.INsOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单盲盒表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
@Service
public class NsOrderItemServiceImpl extends ServiceImpl<NsOrderItemMapper, NsOrderItem> implements INsOrderItemService {

}
