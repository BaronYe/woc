package com;

import com.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Random;


@SpringBootTest
class ApiApplicationTests {

    @Resource
    private RedisUtils redisUtils;


    /**
     * 插入缓存数据
     */
    @Test
    public void resideTest() {
        boolean set = redisUtils.set("simon", "宋新勇");
        System.out.println(set);
        Object simon = redisUtils.get("simon");
        System.out.println(simon);
    }

    @Test
    public void getResideTest() {
        String s = getRandomJianHan(5);
        System.out.println(s);
    }


    //自动生成名字（中文）
    public static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }
}
