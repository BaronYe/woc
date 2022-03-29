package com.util.pay;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @ClassName: WXUtil
 * @Description: 解析xml返回map格式
 * @author: simon
 * @date: 2019年5月10日 下午4:43:00
 */
public class WxUtil {
    /**
     * 随机字符串
     * @return
     */
    public static String generate() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strxml.getBytes(StandardCharsets.UTF_8));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v;
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = WxUtil.getChildrenText(children);
            }
            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    @SuppressWarnings("rawtypes")
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<").append(name).append(">");
                if(!list.isEmpty()) {
                    sb.append(WxUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</").append(name).append(">");
            }
        }
        return sb.toString();
    }
    /**
     * 将请求参数转换为xml格式的string字符串，微信服务器接收的是xml格式的字符串
     *
     * 作者: zhoubang 日期：2015年6月10日 上午9:25:51
     *
     * @param parameters
     * @return
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<Entry<Object, Object>> es = parameters.entrySet();
        Iterator<Entry<Object, Object>> it = es.iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();

            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<").append(k).append(">").append("<![CDATA[").append(v).append("]]></")
                        .append(k).append(">");
            } else {
                sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * sign签名，必须使用MD5签名，且编码为UTF-8
     * 作者: zhoubang 日期：2015年6月10日 上午9:31:24
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign_ChooseWXPay(String characterEncoding,
                                                SortedMap<Object, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set<Entry<Object, Object>> es = parameters.entrySet();
        Iterator<Entry<Object, Object>> it = es.iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        /** 支付密钥必须参与加密，放在字符串最后面 */
        sb.append("key=").append(key);
        return MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    }


    /**
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
        // 创建SSLContext
        StringBuffer buffer=null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            //往服务器端写内容
            if(null !=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
    public static String urlEncodeUTF8(String source){
        String result=source;
        try {
            result=java.net.URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @return boolean
     */
    @SuppressWarnings("rawtypes")
    public static boolean isTenpaySign(String characterEncoding,
                                       SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if(!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(API_KEY);
        //算出摘要
        String mysign = MD5.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();

        System.out.println(tenpaySign + "    " + mysign);
        return tenpaySign.equals(mysign);
    }

}
