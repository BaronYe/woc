package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.Configs;
import com.entity.UserPoint;
import com.mapper.ConfigsMapper;
import com.mapper.UserPointMapper;
import com.service.IUserPointService;
import com.util.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 积分明细 服务实现类
 * </p>
 *
 * @author simon
 * @since 2021-06-25
 */
@Service
public class UserPointServiceImpl extends ServiceImpl<UserPointMapper, UserPoint> implements IUserPointService {

    @Autowired
    private ConfigsMapper configsMapper;

    /**
     * 创建积分明细
     * @return
     */
    @Override
    public Object setUserPoint(String types, Integer uid,String money){
        Configs configs = configsMapper.selectById(Code.one);
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(uid);
        userPoint.setCreateTime(LocalDateTime.now());
        userPoint.setTypes(types);
        userPoint.setIsGet(Code.zero);
        userPoint.setSign(Code.one);
        if (Code.ONE.equals(types)){
            userPoint.setTitle("新用户注册");
            userPoint.setNum(configs.getRegisterIntegralNum());
        }else if (Code.TWO.equals(types)){
            userPoint.setTitle("每天登陆");
            userPoint.setNum(configs.getLoginIntegralNum());
        }else if (Code.THREE.equals(types)){
            userPoint.setTitle("消费");
            Double p =  Double.parseDouble(money) * Double.parseDouble(configs.getConsumeIntegralNum());
            userPoint.setNum(p.intValue());
        }else if (Code.FOUR.equals(types)){
            userPoint.setTitle("完善信息");
            userPoint.setNum(configs.getPerfectIntegralNum());
        }else if (Code.FIVE.equals(types)){
            userPoint.setTitle("发布动态");
            userPoint.setNum(configs.getDynamicIntegralNum());
        }else if (Code.SIX.equals(types)){
            userPoint.setTitle("邀请好友");
            userPoint.setNum(configs.getInviteIntegralNum());
        }else if (Code.SEVEN.equals(types)){
            userPoint.setTitle("分享小程序");
            userPoint.setNum(configs.getShareIntegralNum());
        }else{
            userPoint.setTitle(configs.getIntegralName());
            userPoint.setNum(Code.zero);
        }
        return this.save(userPoint);
    }
}
