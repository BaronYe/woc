package com.util;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;


public class CodeClient {

    /*
     * 获取 token
     * 普通的 get 可获 token
     */
    public static String getToken(String appid,String secret) {
        try {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("grant_type", "client_credential");
            map.put("appid",appid); // 小程序APPID
            map.put("secret", secret); // 小程序APPSECRET
            String rt = UrlUtil.sendPost("https://api.weixin.qq.com/cgi-bin/token", map);
            JSONObject json = JSONObject.fromObject(rt);
            if (json.getString("access_token") != null || json.getString("access_token") != "") {
                return json.getString("access_token");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * post请求返回base64的图片数据
     * */
    //
    public static String sendPost3(String url,String scene,String path) {
        PrintWriter out = null;
        String result = "";
        InputStream inputStream=null;
        //请求数据，自行拼接
        JSONObject paramJson = new JSONObject();
        paramJson.put("scene",scene);//这就是你二维码里携带的参数 String型  名称不可变
        paramJson.put("page", path); //这是设置扫描二维码后跳转的页面
        paramJson.put("width", 400);

//        String param="{ \"scene\":\"19014\" ,\"page\":\"pages/mine/pages/regiest/regiest\",\"width\":430}";
        String param = paramJson.toString();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			conn.setCharacterEncoding("gbk");
            conn.setRequestProperty("Content-Type", "application/json;charset-gbk");
            conn.setRequestProperty("responseType", "arraybuffer");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            //获取流数据
            inputStream = conn.getInputStream();
//            System.out.println(param);
//            System.out.println(inputStream);

            // 将获取流转为base64格式
            byte[] data = null;
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();

            result =  "data:image/png;base64," + new String(Base64.getEncoder().encode(data));
//			当import java.util.Base64;无法导入时，只能在网上找找其他的jar包，写法换成下面这种
//			result = new String(Base64.encodeBase64(data));


        }catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }

            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}

