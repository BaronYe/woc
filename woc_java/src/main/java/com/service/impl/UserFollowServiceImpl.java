package com.service.impl;

import com.entity.UserFollow;
import com.mapper.UserFollowMapper;
import com.service.IUserFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 我的关注用户 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-23
 */
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements IUserFollowService {

}
