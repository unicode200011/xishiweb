//
//  Created by  fred on 2016/10/26.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.stylefeng.guns.core.util;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.fasterxml.jackson.databind.JsonNode;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
public class IdentityAuthentication{

    private static final Log logger = LogFactory.getLog(IdentityAuthentication.class);
    static{
        //HTTP Client init
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey("25093882");
        httpParam.setAppSecret("788a041e703a3632ebbf592b11bad8e6");
        HttpApiClient_IdentityAuthentication.getInstance().init(httpParam);


        //HTTPS Client init
        HttpClientBuilderParams httpsParam = new HttpClientBuilderParams();
        //httpsParam.setAppKey("25093857");
        //httpsParam.setAppSecret("1113fb8746c1b88a0eb55827c123a1fe");
        httpsParam.setAppKey("25093882");
        httpsParam.setAppSecret("788a041e703a3632ebbf592b11bad8e6");

        /**
        * HTTPS request use DO_NOT_VERIFY mode only for demo
        * Suggest verify for security
        */
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        httpsParam.setSslSocketFactory(sslContext.getSocketFactory());
        httpsParam.setX509TrustManager(xtm);
        httpsParam.setHostnameVerifier(DO_NOT_VERIFY);

        HttpsApiClient_IdentityAuthentication.getInstance().init(httpsParam);


    }

       public static boolean identityAuthenticationHttpSend(String realName,String idNum){
//        HttpApiClient_IdentityAuthentication.getInstance().身份证实名认证查询接口("" , "" , new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                try {
//                    System.out.println(getResultString(response));
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });
           ApiResponse response= HttpApiClient_IdentityAuthentication.getInstance().identityAuthenticationHttpSend(realName , idNum,null);
           if(response.getCode()==0000) {
               System.out.println(response.getMessage());
               return true;
           }else{
               System.out.println(response.getMessage());
               return false;
           }
   }

    public static boolean identityAuthenticationHttpsSend(String realName,String idNum){
//        HttpsApiClient_IdentityAuthentication.getInstance().identityAuthenticationHttpsSend(realName , idNum , new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                try {
//                    response.
//                    System.out.println(getResultString(response));
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });
        ApiResponse response= HttpsApiClient_IdentityAuthentication.getInstance().identityAuthenticationHttpsSend(realName , idNum,null);
        try {
            logger.info(getResultString(response));
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response.getCode()==200) {
           try {
               JsonNode node = JsonUtils.byteArrayToJson(response.getBody());
               if (node.get("respCode") !=null) {
                   String code = node.get("respCode").textValue();
                   //boolean ii= (code!=null);
                   //boolean gg= code.equals("0000");
                   if (code!=null &&code.equals("0000")){
                       return true;
                   }else{
                      // logger.info(getResultString(response));
                   }
               }

           }catch (Exception e){
               e.printStackTrace();
           }
           return false;
        }else{
           return false;
       }
    }



    private static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if(response.getCode() != 200){
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody() , SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }
}
