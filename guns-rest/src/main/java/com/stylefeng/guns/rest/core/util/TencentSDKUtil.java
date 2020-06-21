package com.stylefeng.guns.rest.core.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯sdk 工具类
 *
 * @author: lx
 */
@Slf4j
public class TencentSDKUtil {

    private static String SECRET_ID;
    private static String SECRET_KEY;

    static {
        SECRET_ID = ConfigReader.getString("dn.account.secretId");
        SECRET_KEY = ConfigReader.getString("dn.account.secretKey");
    }

    /**
     * 禁用直播流
     *
     * @param streamId
     */
    public static void forbidLive(String streamId) {
        try {
            LiveClient client = getLiveClient();

            String params = "{\"AppName\":\"live\",\"DomainName\":\"livepush.douniuv.com\",\"StreamName\":\"" + streamId + "\"}";
            ForbidLiveStreamRequest req = ForbidLiveStreamRequest.fromJsonString(params, ForbidLiveStreamRequest.class);
            ForbidLiveStreamResponse resp = client.ForbidLiveStream(req);

            log.info("【腾讯直播工具-禁用直播流】流ID=【{}】禁用结果=【{}】", streamId, DescribeLiveForbidStreamListRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流状态 active:活跃 inactive:非活跃 forbid:禁播
     *
     * @param streamId
     * @return true 活跃 false 非活跃
     */
    public static boolean getStreamState(String streamId) {
        try {
            LiveClient client = getLiveClient();

            String params = "{\"AppName\":\"live\",\"DomainName\":\"livepush.douniuv.com\",\"StreamName\":\"" + streamId + "\"}";
            DescribeLiveStreamStateRequest req = DescribeLiveStreamStateRequest.fromJsonString(params, DescribeLiveStreamStateRequest.class);
            DescribeLiveStreamStateResponse resp = client.DescribeLiveStreamState(req);

            log.info("【腾讯直播工具-查询流状态】流ID=【{}】查询结果=【{}】", streamId, DescribeLiveStreamStateRequest.toJsonString(resp));
            return resp.getStreamState().equals("active");
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 断开直播流
     *
     * @param streamId
     */
    public static void offLive(String streamId) {
        try {

            LiveClient client = getLiveClient();

            String params = "{\"StreamName\":\"" + streamId + "\",\"DomainName\":\"livepush.douniuv.com\",\"AppName\":\"live\"}";
            DropLiveStreamRequest req = DropLiveStreamRequest.fromJsonString(params, DropLiveStreamRequest.class);
            DropLiveStreamResponse resp = client.DropLiveStream(req);

            log.info("【腾讯直播工具-断开直播流】流ID=【{}】操作结果=【{}】", streamId, DropLiveStreamRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * 恢复直播流
     *
     * @param streamId
     */
    public static void onLive(String streamId) {
        try {

            LiveClient client = getLiveClient();

            String params = "{\"StreamName\":\"" + streamId + "\",\"DomainName\":\"livepush.douniuv.com\",\"AppName\":\"live\"}";
            ResumeLiveStreamRequest req = ResumeLiveStreamRequest.fromJsonString(params, ResumeLiveStreamRequest.class);
            ResumeLiveStreamResponse resp = client.ResumeLiveStream(req);

            log.info("【腾讯直播工具-恢复直播流】流ID=【{}】操作结果=【{}】", streamId, ResumeLiveStreamRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 获取直播客户端
     *
     * @return
     */
    public static LiveClient getLiveClient() {
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("live.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new LiveClient(cred, "ap-chengdu", clientProfile);
    }
}
