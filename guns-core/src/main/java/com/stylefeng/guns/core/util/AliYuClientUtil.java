package com.stylefeng.guns.core.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliYuClientUtil {
    private AliYuClientUtil(){}
//     IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4fgT0rlhCnAy", "UCtK1z9LTvGjVx5ZBM9jFuW3OUKE8o");
//    private  static IAcsClient client = new DefaultAcsClient(profile);
    public static IAcsClient getClient(){
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4fgT0rlhCnAy", "UCtK1z9LTvGjVx5ZBM9jFuW3OUKE8o");
       IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
