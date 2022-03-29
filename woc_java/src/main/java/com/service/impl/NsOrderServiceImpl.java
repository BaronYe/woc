package com.service.impl;

import com.entity.NsOrder;
import com.mapper.NsOrderMapper;
import com.service.INsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单管理 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
@Service
public class NsOrderServiceImpl extends ServiceImpl<NsOrderMapper, NsOrder> implements INsOrderService {

    @Autowired
    private NsOrderMapper nsOrderMapper;

    @Override
    public List<Map<String, Object>> getNsOrderCountList() {
        return  nsOrderMapper.getNsOrderCountList();
    }
}
