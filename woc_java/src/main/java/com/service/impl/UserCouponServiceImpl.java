package com.service.impl;

import com.entity.UserCoupon;
import com.mapper.UserCouponMapper;
import com.service.IUserCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 我的优惠券 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-21
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements IUserCouponService {

}
