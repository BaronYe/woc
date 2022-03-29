package com.mapper;

import com.entity.NsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单管理 Mapper 接口
 * </p>
 *
 * @author simon
 * @since 2021-06-28
 */
public interface NsOrderMapper extends BaseMapper<NsOrder> {
    List<Map<String, Object>> getNsOrderCountList();
}
