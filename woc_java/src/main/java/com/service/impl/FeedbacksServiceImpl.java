package com.service.impl;

import com.entity.Feedbacks;
import com.mapper.FeedbacksMapper;
import com.service.IFeedbacksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 反馈信息 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@Service
public class FeedbacksServiceImpl extends ServiceImpl<FeedbacksMapper, Feedbacks> implements IFeedbacksService {

}
