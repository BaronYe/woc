package com.service.impl;

import com.entity.Notices;
import com.mapper.NoticesMapper;
import com.service.INoticesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-07-05
 */
@Service
public class NoticesServiceImpl extends ServiceImpl<NoticesMapper, Notices> implements INoticesService {

}
