package com.service.impl;

import com.entity.Merchant;
import com.mapper.MerchantMapper;
import com.service.IMerchantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-16
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements IMerchantService {

}
