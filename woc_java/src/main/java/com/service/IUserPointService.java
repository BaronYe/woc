package com.service;

import com.entity.Configs;
import com.entity.UserPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mapper.ConfigsMapper;
import com.util.Code;
import com.util.Result;

/**
 * <p>
 * 积分明细 服务类
 * </p>
 *
 * @author simon
 * @since 2021-06-25
 */
public interface IUserPointService extends IService<UserPoint> {
    Object setUserPoint(String types, Integer uid,String money);
}
