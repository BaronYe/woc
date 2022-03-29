package com.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @ClassName: Util
 * @Description: 工具类
 * @author: simon
 */
public class Util {

    private static final String charlist = "0123456789";
    /**
     * @Description: //TODO 通过request来获取到json数据
     * @param: [request]
     * @author: songxinyong
     * @date: 2020/11/4 17:08
     * @return: com.alibaba.fastjson.JSONObject
     */
    public static JSONObject getJSONParam(HttpServletRequest request){
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(
                        request.getInputStream(),StandardCharsets.UTF_8
                )
            );
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }


    /**
     * @Description: //TODO 判断字段是否大于10
     * @param: [n]
     * @author: songxinyong
     * @date: 2020/11/4 17:03
     * @return: java.lang.String
     */
    public static String handle(int n){
        if (n < 10) {
            return "0" + n;
        }
        return n+"";
    }

    /**
     * @Description: //TODO 使用Base64加密
     * @param: [str]
     * @author: songxinyong
     * @date: 2020/11/4 17:06 
     * @return: java.lang.String
     */
    public static String base64Plus(String str) {
        return Base64.encodeBase64String(str.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * @Description: //TODO 使用Base64解密
     * @param: [str]
     * @author: songxinyong
     * @date: 2020/11/4 17:07 
     * @return: java.lang.String
     */
    public static String base64Untie(String str) {
        return new String(Base64.decodeBase64(str.getBytes()), StandardCharsets.UTF_8);
    }

    /**
     * @Description: //TODO 生成订单编号
     * @param: []
     * @author: songxinyong
     * @date: 2020/11/4 17:07 
     * @return: java.lang.String
     */
    public static String getOrderSn(){
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "" + (handle(calendar.get(Calendar.MONTH)+1)) + ""
                + (handle(calendar.get(Calendar.DATE))) + "" + (handle(calendar.get(Calendar.HOUR_OF_DAY))) + "" +
                calendar.get(Calendar.MINUTE) + ""  + createRandomString(5) ;
        return time.substring(2);
    }

    /*** 
     * @Description: //TODO 元转分
     * @param: [yuan]
     * @author: songxinyong
     * @date: 2020/11/4 17:07 
     * @return: java.lang.Integer
     */
    public static Integer yuanToFen(String yuan) {
        return (new BigDecimal(yuan)).setScale(2, 4)
                .multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 传入时间跟当前时间比较
     * @param time
     * @return
     * @throws ParseException
     */
    public static boolean toCompareTime(String time) throws ParseException {
        // 现在时间
        long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(time);
        // 传入时间
        long beforeTime = date.getTime();
        return beforeTime > now;
    }

    /**
     * @Description: //TODO 返回date格式的日期
     * @param: []
     * @author: songxinyong
     * @date: 2020/11/4 17:07
     * @return: java.util.Date
     */
    public static Date getDate() {
        return new Timestamp(new Date().getTime());
    }


    /**
     * 生产12位随机大写字母
     * @return
     */
    public static String getRandom(){
        String code = "";
        for(int i=0;i<16;i++){
            code += String.valueOf((char) (new Random().nextInt(26) + 65));
        }
        return code;
    }

    /**
     * @Description: //TODO 获取随机数
     * @param: [len]
     * @author: songxinyong
     * @date: 2020/11/4 17:07
     * @return: java.lang.String
     */
    public static String createRandomString(int len) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            str.append(charlist.charAt(getRandom(charlist.length())));
        }
        return str.toString();
    }

    private static int getRandom(int mod) {
        if (mod < 1) {
            return 0;
        }
        return getInt() % mod;
    }


    private static int getInt() {
        return Math.abs(Long.valueOf(getRandomNumString()).intValue());
    }

    private static String getRandomNumString() {
        double d = Math.random();
        String dStr = String.valueOf(d).replaceAll("[^\\d]", "");
        if (dStr.length() > 1) {
            dStr = dStr.substring(0, dStr.length() - 1);
        }
        return dStr;
    }
}
