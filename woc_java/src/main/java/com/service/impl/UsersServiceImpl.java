package com.service.impl;

import com.entity.Users;
import com.mapper.UsersMapper;
import com.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户数据表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2020-11-05
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
