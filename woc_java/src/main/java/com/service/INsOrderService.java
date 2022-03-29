package com.service;

import com.entity.NsOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单管理 服务类
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
public interface INsOrderService extends IService<NsOrder> {
    List<Map<String, Object>> getNsOrderCountList();
}
