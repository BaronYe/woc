package com.service.impl;

import com.entity.Coupon;
import com.mapper.CouponMapper;
import com.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-18
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

}
