package com.service.impl;

import com.entity.Topics;
import com.mapper.TopicsMapper;
import com.service.ITopicsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 话题列表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-07-09
 */
@Service
public class TopicsServiceImpl extends ServiceImpl<TopicsMapper, Topics> implements ITopicsService {

}
