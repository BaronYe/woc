package com.service.impl;

import com.entity.Admin;
import com.mapper.AdminMapper;
import com.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员账号 服务实现类
 * </p>
 *
 * @author simon
 * @since 2020-11-04
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
