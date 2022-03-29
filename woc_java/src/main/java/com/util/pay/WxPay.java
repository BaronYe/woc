package com.util.pay;

import com.alibaba.fastjson.JSON;
import com.util.Code;
import com.util.Result;
import org.apache.commons.lang.CharEncoding;
import org.jdom2.JDOMException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 描述:
 * 微信小程序支付/退款
 *
 * @author songxinyong
 * @ClassName WxPay
 * @date 2020-01-02 14:26
 */
public class WxPay {

    /**
     * @Description: //TODO 创建订单
     * @param: [wxPayConfiguration]
     * @author: songxinyong
     * @date: 2020/11/5 09:37 
     * @return: java.lang.Object
     */
    public static Object createOrder(WxPayConfiguration wxPayConfiguration){
        SortedMap<Object, Object> packageParams = new TreeMap<>();
        packageParams.put("appid", wxPayConfiguration.getAppid());
        packageParams.put("mch_id", wxPayConfiguration.getMchid());
        packageParams.put("nonce_str", wxPayConfiguration.getNonceStr());
        packageParams.put("body", wxPayConfiguration.getBody());
        packageParams.put("out_trade_no", wxPayConfiguration.getOutTradeNo());
        packageParams.put("total_fee", wxPayConfiguration.getTotalFee());
        packageParams.put("notify_url", wxPayConfiguration.getNotifyUrl());
        packageParams.put("attach", wxPayConfiguration.getAttach());
        packageParams.put("trade_type", wxPayConfiguration.getTradeType());
        packageParams.put("openid", wxPayConfiguration.getOpenid());
        String sign = PayCommonUtil.createSign(CharEncoding.UTF_8, packageParams , wxPayConfiguration.getMchsecret());
        packageParams.put("sign",sign);
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        String resXml = HttpsUtil.postData(WxApi.pay_unifiedorder, requestXML);
        try {
            Map<String,Object> map1 = WxUtil.doXMLParse(resXml);
            String prepay_id = (String) map1.get("prepay_id");
            SortedMap<Object, Object> packageP = new TreeMap<>();
            packageP.put("appId",  wxPayConfiguration.getAppid());
            packageP.put("nonceStr", wxPayConfiguration.getNonceStr());
            packageP.put("package", "prepay_id=" + prepay_id);
            packageP.put("signType", "MD5");
            packageP.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
            String paySign = PayCommonUtil.createSign(CharEncoding.UTF_8, packageP, wxPayConfiguration.getMchsecret());
            packageP.put("paySign", paySign);

            return Result.ok(packageP);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return Result.fail(Code.FAIL,"拉起支付失败");
    }

    /**
     * @Description: //TODO 支付回调
     * @param: [request, response]  
     * @author: songxinyong
     * @date: 2020/11/5 09:38 
     * @return: java.util.Map
     */
    public static Map notify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();
        Map<String, String> m;
        m = WxUtil.doXMLParse(sb.toString());
        SortedMap<Object,Object> packageParams = new TreeMap<>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);
            String v = "";
            if(null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        String resXml;
        if("SUCCESS".equals(packageParams.get("result_code")) &&
                "SUCCESS".equals(packageParams.get("return_code")) ){
            String attach = (String) packageParams.get("attach");
            String transactionId = (String) packageParams.get("transaction_id");
            Map maps = (Map) JSON.parse(attach);
            maps.put("transactionId",transactionId);
            //得到返回的参数
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
           return maps;
        } else {
            return null;
        }
    }
}
