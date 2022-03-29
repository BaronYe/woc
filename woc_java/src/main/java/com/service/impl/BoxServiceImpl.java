package com.service.impl;

import com.entity.Box;
import com.mapper.BoxMapper;
import com.service.IBoxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 盲盒 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-15
 */
@Service
public class BoxServiceImpl extends ServiceImpl<BoxMapper, Box> implements IBoxService {

}
