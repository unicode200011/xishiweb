package com.stylefeng.guns.rest.core.util.sms;

import com.aliyuncs.utils.XmlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.stylefeng.guns.core.util.JsonUtils;
import com.stylefeng.guns.core.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;


/**
 * 短信
 */
@Slf4j
public class SendSmsCode {
//    private static final String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
//    private static final String account = "C89883325";
//    private static final String password = "b3edcea6713ec17ea4bd8a735ae69441";
    private static final String Url = "http://119.3.17.76:7862/sms?action=send";
    private static final String account = "922001";
    private static final String password = "cd3kkp";
    public static Integer sendCode(String mobile) {

//        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

//        client.getParams().setContentCharset("GBK");
//        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType", "application/json;charset=UTF-8");

        String mobileCode = ToolUtil.getRandomNum(6);
        String content = "【西施】验证码：" + mobileCode + "，请您在10分钟内填写。如非本人操作，请忽略本短信。";

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", account),
                new NameValuePair("password", password),
                new NameValuePair("mobile", mobile),
                new NameValuePair("content", content),
                new NameValuePair("extno", "1069010"),
        };
        Map map = new HashMap();
                map.put("account", account);
                map.put("password", password);
                map.put("mobile", mobile);
                map.put("content", content);
                map.put("extno", "1069010");
        map.put("rt", "json");
        StringBuilder buf = new StringBuilder();
        buildPayParams(buf,map,false);
        method.setRequestBody(data);
        String str=buf.toString();
        System.out.println(str);
        try {
            CloseableHttpResponse response = null;
            CloseableHttpClient client = null;
//            int code = client.executeMethod(method);
            HttpPost httpPost = new HttpPost(Url);
            StringEntity entityParams = new StringEntity(str, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "application/json;utf-8");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            System.out.println(response);
//            Map<String, String> resultMap = null;
//            resultMap = toMap1(EntityUtils.toByteArray(response.getEntity()), "utf-8");
            JsonNode sr = JsonUtils.byteArrayToJson(EntityUtils.toByteArray(response.getEntity()));
//            resultMap=JsonUtils.jsonToMap(sr);
//            System.out.println(resultMap);
//            String SubmitResult = method.getResponseBodyAsString();
//            Document doc = DocumentHelper.parseText(SubmitResult);
//            Element root = doc.getRootElement();

//            String code = root.elementText("code");
//            String msg = root.elementText("msg");
//            String smsid = root.elementText("smsid");
//            if ("2".equals(code)) {
//                return Integer.valueOf(mobileCode);
//            } else {
//                log.info("【短信发送】短信发送失败,手机号=【{}】,错误码=【{}】,错误信息=【{}】,消息ID=【{}】", mobile, code, msg, smsid);
//                return null;
//            }
//            String status=root.elementText("status");
//            String result=root.elementText("result");
//            String mid = root.elementText("mid");
//            String list = root.elementText("list");
//            if (status.equals("0")&&result.equals("0")){
//            if ("0".equals(resultMap.get("status"))) {
                return Integer.valueOf(mobileCode);
//            }else {
//                log.info("【短信发送】短信发送失败,手机号=【{}】", mobile);
//                return null;
//            }
//            }else {
//                log.info("【短信发送】短信发送失败,手机号=【{}】,错误码=【{}】,错误信息=【{}】,消息ID=【{}】,错误列表=【{}】", mobile, status, result, mid,list);
//                return null;
//            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Map<String, String> toMap1(byte[] xmlBytes,String charset) throws Exception{
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        Map<String, String> params = toMap(doc.getRootElement());
        return params;
    }
    public static Map<String, String> toMap(Element element){
        Map<String, String> rest = new HashMap<String, String>();
        List<Element> els = element.elements();
        for(Element el : els){
            rest.put(el.getName().toLowerCase(), el.getTextTrim());
        }
        return rest;
    }

    public static void buildPayParams(StringBuilder sb, Map<String, String> payParams, boolean encoding){
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");

            sb.append(payParams.get(key));

            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }


    public static void main(String[] args) {
        System.out.println(sendCode("18123362419"));
    }
}
