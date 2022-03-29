package com.service.impl;

import com.entity.Address;
import com.mapper.AddressMapper;
import com.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址数据表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-21
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
