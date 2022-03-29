package com.service.impl;

import com.entity.UserBalance;
import com.mapper.UserBalanceMapper;
import com.service.IUserBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 我的余额明细 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-22
 */
@Service
public class UserBalanceServiceImpl extends ServiceImpl<UserBalanceMapper, UserBalance> implements IUserBalanceService {

}
