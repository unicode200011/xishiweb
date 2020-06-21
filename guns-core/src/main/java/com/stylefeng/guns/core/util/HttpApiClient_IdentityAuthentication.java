//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.stylefeng.guns.core.util;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;




public class HttpApiClient_IdentityAuthentication extends ApacheHttpClient{
    public final static String HOST = "idenauthen.market.alicloudapi.com";
    static HttpApiClient_IdentityAuthentication instance = new HttpApiClient_IdentityAuthentication();
    public static HttpApiClient_IdentityAuthentication getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public ApiResponse identityAuthenticationHttpSend(String name , String idNo , ApiCallback callback) {
        String path = "/idenAuthentication";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("name" , name , ParamPosition.BODY , true);
        request.addParam("idNo" , idNo , ParamPosition.BODY , true);



        //sendAsyncRequest(request , callback);
        return sendSyncRequest(request);
    }
}