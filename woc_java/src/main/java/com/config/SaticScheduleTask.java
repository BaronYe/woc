package com.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Activity;
import com.entity.Coupon;
import com.service.impl.ActivityServiceImpl;
import com.service.impl.CouponServiceImpl;
import com.util.Code;
import com.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private CouponServiceImpl couponService;

    /**
     * 定时修改活动状态
     * @throws ParseException
     */
    @Scheduled(cron = "30 0 0 * * ?") // 定时每天0'00'30 开始任务 30 0 0 * * ?
    private void setActivityToState() throws ParseException {
        List<Activity> activityList = activityService.list(new QueryWrapper<Activity>().lambda()
                .ne(Activity::getState, Code.TWO)
                .select(Activity::getId,Activity::getActivityStartTime,Activity::getActivityEndTime,Activity::getState));
        for (Activity activity : activityList) {
            if (Code.ZERO.equals(activity.getState())){
                // 现在时间
                long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(activity.getActivityStartTime());
                // 开始时间
                long startTime = date.getTime();
                if (startTime <= now){
                    activityService.updateById(new Activity().setId(activity.getId()).setState(Code.ONE));
                }
            }else{
                // 现在时间
                long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(activity.getActivityEndTime());
                // 截止时间
                long endTime = date.getTime();
                if (endTime <= now){
                    activityService.updateById(new Activity().setId(activity.getId()).setState(Code.TWO));
                }
            }
        }
    }

    /**
     * 检查优惠券的状态
     * @throws ParseException
     */
    @Scheduled(cron = "0 */1 * * * ?") // 每隔1分钟执行一次
    public void setCouponState() throws ParseException {
        List<Coupon> couponList = couponService.list(new QueryWrapper<Coupon>().lambda()
                .ne(Coupon::getCouponStatus, Code.ZERO)
                .select(Coupon::getId,Coupon::getStartTime,Coupon::getEndTime));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Coupon coupon : couponList) {
            if(!Util.toCompareTime(df.format(coupon.getStartTime()))){
                if (!Util.toCompareTime(df.format(coupon.getEndTime()))){
                    coupon.setCouponStatus(Code.ZERO);
                }else{
                    coupon.setCouponStatus(Code.ONE);
                }
            }else{
                coupon.setCouponStatus(Code.TWO);
            }
            couponService.updateById(coupon);
        }
    }

}