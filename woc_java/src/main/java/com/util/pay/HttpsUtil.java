package com.util.pay;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class HttpsUtil {
	private static final Logger log = LogManager.getLogger(HttpsUtil.class.getName());
    private final static int CONNECT_TIMEOUT = 5000;
    private final static String DEFAULT_ENCODING = "UTF-8";  

    public static String postData(String urlStr, String data){  
        return postData(urlStr, data, null);  
    } 
    
    /**
     * 
     * @Title: postData
     * @Description: 向指定URL发送POST方法的请求
     * @param urlStr ->指定URL
     * @param data -> 数据
     * @param contentType
     * @return
     * @return: String
     */
    public static String postData(String urlStr, String data, String contentType){  
        BufferedReader reader = null;  
        try {  
            URL url = new URL(urlStr);  
            URLConnection conn = url.openConnection();  
            conn.setDoOutput(true);  
            conn.setConnectTimeout(CONNECT_TIMEOUT);  
            conn.setReadTimeout(CONNECT_TIMEOUT);  
            if(contentType != null)  
                conn.setRequestProperty("content-type", contentType);  
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);  
            if(data == null)  
                data = "";  
            writer.write(data);   
            writer.flush();  
            writer.close();    

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));  
            StringBuilder sb = new StringBuilder();  
            String line;
            while (null != (line = reader.readLine())) {
                sb.append(line);  
                sb.append("\r\n");  
            }  
            return sb.toString();  
        } catch (IOException e) {  
            log.error("Error connecting to " + urlStr + ": " + e.getMessage());  
        } finally {  
            try {  
                if (reader != null)  
                    reader.close();  
            } catch (IOException e) {  
            	log.error("Error connecting to " + e.getMessage());  
            }  
        }  
        return null;  
    } 
    
    
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return String 所代表远程资源的响应结果
     */
    public static String get(String url,String param) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            /*for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                throw new Exception(e2.getMessage());
            }
        }
        return result.toString();
    }
}
