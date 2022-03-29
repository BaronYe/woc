package com.service.impl;

import com.entity.Configs;
import com.mapper.ConfigsMapper;
import com.service.IConfigsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础配置 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@Service
public class ConfigsServiceImpl extends ServiceImpl<ConfigsMapper, Configs> implements IConfigsService {

}
