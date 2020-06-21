package com.stylefeng.guns.rest.core.util;


import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 腾讯云 视频 直播 工具类
 *
 * @author lx
 */
@Slf4j
public class UrlMaker {

    /**
     * 失效时间
     */
    private static long outTime = 24 * 60 * 60;
    private static final String MAC_NAME_SHA1 = "HmacSHA1";
    public static final String MAC_NAME_SHA256 = "HmacSHA256";
    private static final String ENCODING = "UTF-8";


    final static char SPLIT_CHAR = '_';

    /**
     * 获取推送流
     *
     * @return
     */
    public static String getPushUrl(Integer liveId, String key, String bizid, String groupId) {
        long currentTimeMillis = System.currentTimeMillis();
        String streamId = createStreamId(bizid, liveId, groupId);
        currentTimeMillis = currentTimeMillis / 1000 + outTime;

        return new StringBuffer("rtmp://")
                .append(bizid)
                .append(".livepush.myqcloud.com/live/")
                .append(streamId)
                .append("?bizid=")
                .append(bizid)
                .append("&")
                .append(getSafeUrl(key, streamId, currentTimeMillis))
                //.append("&record=hls&record_interval=5400")//mp4或者hls格式需要指定这两个参数    hls不需要
                .toString();
    }

    public static void main(String[] args) {
        String pushUrl = getPushUrl(999999, "344627a420dd83b190598abf83e2552b", "41939", "@TGS#aWPM5XXFQ")
                .replace("41939.livepush.myqcloud.com", "livepush.douniuv.com");
        String playUrlFlv = getPlayUrlFlv(999999, "41939", "@TGS#aWPM5XXFQ")
                .replace("41939.liveplay.myqcloud.com", "liveplay.douniuv.com");

        System.err.println("推流地址:" + pushUrl);
        System.err.println("播流地址:" + playUrlFlv);
    }

    /**
     * 获取连麦者推送流
     *
     * @return
     */
    public static String getUserPushUrl(Integer liveId, Long userId, String key, String bizid, String groupId) {
        long currentTimeMillis = System.currentTimeMillis();
        String streamId = createUserStreamId(bizid, liveId, groupId, userId);
        currentTimeMillis = currentTimeMillis / 1000 + outTime;

        return new StringBuffer("rtmp://")
                .append(bizid)
                .append(".livepush.myqcloud.com/live/")
                .append(streamId)
                .append("?bizid=")
                .append(bizid)
                .append("&")
                .append(getSafeUrl(key, streamId, currentTimeMillis))
                //.append("&record=hls&record_interval=5400")//mp4或者hls格式需要指定这两个参数    hls不需要
                .toString();
    }

    /**
     * @description: 混流请求URL
     * @author: lx
     */
    private static String getMixStreamQueryString(String key, String appId) {
        //1分钟有效
        long txTime = System.currentTimeMillis() / 1000 + 60;
        String txSecret = getMD5(key + txTime);
        String query = "?appid=" + appId +
                "&interface=mix_streamv2.start_mix_stream_advanced&t=" + txTime + "&sign=" + txSecret;
        query = "http://fcgi.video.qcloud.com/common_access" + query;
        return query;
    }

    /**
     * @description: 查询流状态URL
     * @author: lx
     */
    private static String getStreamStatuQueryString(String key, String appId, String streamId) {
        //5分钟有效
        long txTime = System.currentTimeMillis() / 1000 + 300;
        String txSecret = getMD5(key + txTime);
        String query = "?appid=" + appId
                + "&interface=Live_Channel_GetStatus&Param.s.channel_id=" + streamId
                + "&t=" + txTime
                + "&sign=" + txSecret;
        query = "http://fcgi.video.qcloud.com/common_access" + query;
        return query;
    }

    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return byteArrayToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }


    /**
     * 获取播放流
     *
     * @return
     */
    public static String getPlayUrlRtmp(Integer liveId, String bizid, String groupId) {
        return new StringBuffer("rtmp://")
                .append(bizid)
                .append(".liveplay.myqcloud.com/live/")
                .append(createStreamId(bizid, liveId, groupId)).toString();
    }


    /**
     * 获取播放流
     *
     * @return
     */
    public static String getPlayUrlFlv(Integer liveId, String bizid, String groupId) {
        return new StringBuffer("http://")
                .append(bizid)
                .append(".liveplay.myqcloud.com/live/")
                .append(createStreamId(bizid, liveId, groupId))
                .append(".flv").toString();
    }

    /**
     * 获取连麦者播放流
     *
     * @return
     */
    public static String getUserPlayUrlFlv(Integer liveId, Long userId, String bizid, String groupId) {
        return new StringBuffer("http://")
                .append(bizid)
                .append(".liveplay.myqcloud.com/live/")
                .append(createUserStreamId(bizid, liveId, groupId, userId))
                .append(".flv").toString();
    }

    /**
     * 获取播放流
     *
     * @return
     */
    public static String getPlayUrlHls(Integer liveId, String bizid, String groupId) {
        return new StringBuffer("http://")
                .append(bizid)
                .append(".liveplay.myqcloud.com/live/")
                .append(createStreamId(bizid, liveId, groupId))
                .append(".m3u8").toString();
    }

    /**
     * 创建视频流ID
     * <p>
     * 业务ID + "_" + 直播ID + "_" + 聊天室Id后四位
     * </p>
     *
     * @param bizid
     * @param liveId
     * @param groupId
     * @return
     */
    public static String createStreamId(String bizid, Integer liveId, String groupId) {
        return new StringBuffer(bizid)
                .append(SPLIT_CHAR)
                .append(liveId)
                .append(SPLIT_CHAR)
                .append(groupId.substring(groupId.length() - 4, groupId.length())).toString();
    }

    /**
     * 创建用户推流ID
     *
     * @param bizid
     * @param liveId
     * @param groupId
     * @return
     */
    private static String createUserStreamId(String bizid, Integer liveId, String groupId, Long userId) {
        return new StringBuffer(bizid)
                .append(SPLIT_CHAR)
                .append(liveId)
                .append(SPLIT_CHAR)
                .append(groupId.substring(groupId.length() - 4, groupId.length()))
                .append(SPLIT_CHAR)
                .append(userId.toString())
                .append(SPLIT_CHAR)
                .append("userPusher")
                .toString();
    }

    /**
     * 获取录像请求地址
     *
     * @return
     */
    public static String getReplayUrl(Integer liveId, String startTime, String endTime, String appid, String apiKey, String bizid, String groupId) {
        long time = System.currentTimeMillis() / 1000 + outTime;
        String streamId = createStreamId(bizid, liveId, groupId);
        StringBuilder sb = new StringBuilder();
        sb.append("http://fcgi.video.qcloud.com/common_access?appid=");
        sb.append(appid);
        sb.append("&interface=Live_Tape_GetFilelist&Param.s.channel_id=");
        sb.append(streamId);
        sb.append("&t=");
        sb.append(time);
        sb.append("&sign=");
        sb.append(getSafeSign(apiKey, time));
//		sb.append("&Param.n.page_no=1");
//		sb.append("&Param.n.page_size=20");
//		startTime = "2017-07-12 12:48:42";
//		endTime = "2017-07-12 16:48:42";
        try {
            sb.append("&Param.s.start_time=").append(URLEncoder.encode(startTime, ENCODING));
            sb.append("&Param.s.end_time=").append(URLEncoder.encode(endTime, ENCODING));
        } catch (UnsupportedEncodingException e) {

        }
        return sb.toString();
    }

    /**
     * 获取视频转码url
     *
     * @param fileId
     * @return
     */
    public static String getConvertVodFileUrl(String fileId, String apiSecretId, String apiSecretKey) {
        long timeNow = System.currentTimeMillis() / 1000;
        // 随机正整数，与 Timestamp 联合起来, 用于防止重放攻击。
        int nonceInt = Math.abs(new Random().nextInt());
        Map<String, String> map = new HashMap<>();
        map.put("fileId=", fileId);
        map.put("Action=", "ConvertVodFile");
        map.put("SecretId=", apiSecretId);
        map.put("Nonce=", String.valueOf(nonceInt));
//		map.put("Region=","");
        map.put("Timestamp=", String.valueOf(timeNow));
        String paramStr = sort(map, 0);

        StringBuilder paramSb = new StringBuilder();
        paramSb.append("GETvod.api.qcloud.com/v2/index.php?");
        paramSb.append(paramStr);
        String signature = HmacSHA.generateSign(apiSecretKey, paramSb.toString(), MAC_NAME_SHA1);
        if ("".equals(signature)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
//		sb.append("https://vod.api.qcloud.com/v2/index.php?Action=ConvertVodFile&fileId=").append(fileId);
//		sb.append("&Nonce=");
//		sb.append(nonceInt);
//		sb.append("&Region=");
//		sb.append("&SecretId=");
//		sb.append(apiSecretId);
//		sb.append("&Timestamp=");
//		sb.append(timeNow);
        sb.append("https://vod.api.qcloud.com/v2/index.php?").append(paramStr);
        sb.append("&Signature=");
        sb.append(signature);
//		sb.append("&&isScreenshot=1");
//		sb.append("&isWatermark=1");
        return sb.toString();
    }

    /**
     * @description: 短视频公共请求接口
     * @author: lx
     */
    public static String videoPublicRequest(Map params, String id, String key) {
        try {
            String secretId = URLEncoder.encode(id, ENCODING);
            String secretKey = key;
            long timestamp = System.currentTimeMillis() / 1000;
            Integer random = ToolUtil.get32RandomNum();
            SortedMap<String, Object> param = new TreeMap<>();
            param.put("SecretId", secretId);
            param.put("Region", "cd");
            param.put("Timestamp", timestamp);
            param.put("Nonce", random.toString());
            param.put("SignatureMethod", MAC_NAME_SHA256);
            param.putAll(params);
            StringBuilder paramStr = new StringBuilder();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                paramStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String finalParamStr = paramStr.substring(0, paramStr.length() - 1);
            StringBuilder signSb = new StringBuilder("GETvod.api.qcloud.com/v2/index.php?");
            signSb.append(finalParamStr);
            String sign = HmacSHA.generateSign(secretKey, signSb.toString(), MAC_NAME_SHA256);

            StringBuilder rSb = new StringBuilder();
            rSb.append("https://vod.api.qcloud.com/v2/index.php?").append(paramStr);
            rSb.append("Signature=");
            rSb.append(URLEncoder.encode(sign, ENCODING));
            return HTTPUtils.sendGetHttpRequest(rSb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取删除视频url
     *
     * @param fileId
     * @return
     */
    public static String getDeleteFileUrl(String fileId, String apiSecretId, String apiSecretKey) {
        long timeNow = System.currentTimeMillis() / 1000;
        // 随机正整数，与 Timestamp 联合起来, 用于防止重放攻击。
        int nonceInt = Math.abs(new Random().nextInt());
        Map<String, String> map = new HashMap<>();

        map.put("Action=", "DeleteVodFile");
        // 签名用的串不能编码
        map.put("fileId=", fileId);
        map.put("priority=", "0");
        map.put("SecretId=", apiSecretId);
        map.put("Nonce=", String.valueOf(nonceInt));
        map.put("Region=", "");
        map.put("Timestamp=", String.valueOf(timeNow));
        String paramStr = sort(map, 0);

        StringBuilder paramSb = new StringBuilder();
        paramSb.append("GETvod.api.qcloud.com/v2/index.php?");
        paramSb.append(paramStr);
        String signature = HmacSHA.generateSign(apiSecretKey, paramSb.toString(), MAC_NAME_SHA1);
        if ("".equals(signature)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://vod.api.qcloud.com/v2/index.php?").append(paramStr);
        sb.append("&Signature=");
        sb.append(signature);
        return sb.toString();
    }

    /**
     * 获取禁用，中断，允许直播流
     *
     * @param liveId
     * @return
     */
    public static String getSetLiveStatusUrl(Integer liveId, String status, String appid, String apiKey, String bizid, String groupId) {
        long time = System.currentTimeMillis() / 1000 + outTime;
        String streamId = createStreamId(bizid, liveId, groupId);
        StringBuilder sb = new StringBuilder();
        sb.append("http://fcgi.video.qcloud.com/common_access?appid=");
        sb.append(appid);
        sb.append("&interface=Live_Channel_SetStatus&Param.s.channel_id=");
        sb.append(streamId);
        sb.append("&t=");
        sb.append(time);
        sb.append("&sign=");
        sb.append(getSafeSign(apiKey, time));
        sb.append("&Param.n.status=").append(status);
        return sb.toString();
    }


    /**
     * 查询直播中直播的状态
     *
     * @param liveId
     * @return
     */
    public static String getGetLivePlayStat(Integer liveId, String appid, String apiKey, String bizid, String groupId) {
        long time = System.currentTimeMillis() / 1000 + outTime;
        String streamId = createStreamId(bizid, liveId, groupId);
        StringBuilder sb = new StringBuilder();
        sb.append("http://fcgi.video.qcloud.com/common_access?appid=");
        sb.append(appid);
        sb.append("&interface=Get_LiveStat");
        sb.append("&Param.s.channel_id=");
        sb.append(streamId);
        sb.append("&t=");
        sb.append(time);
        sb.append("&sign=");
        sb.append(getSafeSign(apiKey, time));

        return sb.toString();
    }

    /**
     * 按字母顺序排序
     *
     * @return
     */
    private static String sort(Map<String, String> paramMap, final int sort) {
        Map<String, String> map = new TreeMap<String, String>(
                (obj1, obj2) -> {
                    if (sort == 0) {
                        // 升序排列
                        return obj1.compareTo(obj2);
                    } else if (sort == 1) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    } else {
                        // 升序排列
                        return obj1.compareTo(obj2);
                    }
                });
        map.putAll(paramMap);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }


    public static String hamcsha1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 计算安全签名
     *
     * @param key
     * @param txTime
     * @return
     */
    private static String getSafeSign(String key, long txTime) {
        String input = new StringBuilder(key).append(Long.toString(txTime)).toString();
        String safeSign = MD5Password.md5(input);
        return safeSign;
    }

    /*
     * KEY+ stream_id + txTime
     */
    private static String getSafeUrl(String key, String streamId, long txTime) {
        String input = new StringBuilder().append(key).append(streamId).append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret = byteArrayToHexString(messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" : new StringBuilder().append("txSecret=").append(txSecret).append("&").append("txTime=").append(Long.toHexString(txTime).toUpperCase()).toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    /**
     * 查询频道列表
     *
     * @param
     * @return
     */
    public static String getLiveRoom(String appid, String apiKey) {
        long time = System.currentTimeMillis() / 1000 + outTime;
        StringBuilder sb = new StringBuilder();
        sb.append("http://fcgi.video.qcloud.com/common_access?appid=");
        sb.append(appid);
        sb.append("&interface=Live_Channel_GetLiveChannelList");
        sb.append("&t=");
        sb.append(time);
        sb.append("&sign=");
        sb.append(getSafeSign(apiKey, time));
        return sb.toString();
    }

    /**
     * 获取视频信息
     */
    public static String getVideoInfo(String fileId, String apiSecretId, String apiSecretKey) {
        long time = System.currentTimeMillis() / 1000;
        int nonceInt = Math.abs(new Random().nextInt());
        Map<String, String> map = new HashMap<>();
        map.put("fileId=", fileId);
        map.put("Action=", "GetVideoInfo");
        map.put("SecretId=", apiSecretId);
        map.put("Nonce=", String.valueOf(nonceInt));
        map.put("Timestamp=", String.valueOf(time));
        String paramStr = sort(map, 0);

        StringBuilder paramSb = new StringBuilder();
        paramSb.append("GETvod.api.qcloud.com/v2/index.php?");
        paramSb.append(paramStr);
        String signature = HmacSHA.generateSign(apiSecretKey, paramSb.toString(), MAC_NAME_SHA1);
        if ("".equals(signature)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://vod.api.qcloud.com/v2/index.php?").append(paramStr);
        sb.append("&Signature=");
        sb.append(signature);
        return sb.toString();

    }

    /**
     * 删除视频
     */
    public static String deleteVideodFile(String fileId, String apiSecretId, String apiSecretKey) {
        long time = System.currentTimeMillis() / 1000;
        int nonceInt = Math.abs(new Random().nextInt());
        Map<String, String> map = new HashMap<>();
        map.put("fileId=", fileId);
        //删除文件时是否及时刷新cdn缓存文件；默认不刷新，指定为1时刷新
        map.put("isFlushCdn=", "1");
        //优先级0:中 1：高 2：低
        map.put("priority=", "0");
        map.put("Action=", "DeleteVodFile");
        map.put("SecretId=", apiSecretId);
        map.put("Nonce=", String.valueOf(nonceInt));
        map.put("Timestamp=", String.valueOf(time));
        String paramStr = sort(map, 0);

        StringBuilder paramSb = new StringBuilder();
        paramSb.append("GETvod.api.qcloud.com/v2/index.php?");
        paramSb.append(paramStr);
        String signature = HmacSHA.generateSign(apiSecretKey, paramSb.toString(), MAC_NAME_SHA1);
        if ("".equals(signature)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://vod.api.qcloud.com/v2/index.php?").append(paramStr);
        sb.append("&Signature=");
        sb.append(signature);
        return sb.toString();

    }

    /**
     * @description: 根据推流者数量获取模板ID
     * @author: lx
     */
    public static int getMixStreamTemplateId(int pusherSize) {
        switch (pusherSize) {
            case 1:
                return 0;
            case 2:
                return 20;
            case 3:
                return 310;
            case 4:
                return 410;
            default:
                return 0;
        }
    }

    /**
     * @description: 从推送流获取流iD
     * @author: lx
     */
    public static String getStreamIdFromPushUrl(String pushUrl) {
        int index = pushUrl.indexOf("?");
        if (index == -1) {
            return "";
        } else {
            String substr = pushUrl.substring(0, index);
            int index_2 = substr.lastIndexOf("/");
            String streamID = substr.substring(index_2 + 1, index);
            return streamID;
        }
    }



    /**
     * @description: 取消混流;
     * @author: lx
     */
    public static boolean cancelMergeStream(String apiAuthKey, String appId, String mainStreamId, String sessionId) {
        String requestUrl = getMixStreamQueryString(apiAuthKey, appId);
        Map<String, Object> params = new HashMap<>(16);

        Map<String, Object> interfaceParam = new HashMap<>(16);
        interfaceParam.put("app_id", appId);
        interfaceParam.put("interface", "mix_streamv2.cancel_mix_stream");
        interfaceParam.put("mix_stream_session_id", sessionId);
        interfaceParam.put("output_stream_id", mainStreamId);

        Map<String, Object> interfaceData = new HashMap<>();
        interfaceData.put("interfaceName", "Mix_StreamV2");
        interfaceData.put("para", interfaceParam);

        params.put("timestamp", System.currentTimeMillis() / 1000);
        params.put("eventId", System.currentTimeMillis() / 1000);
        params.put("interface", interfaceData);

        String param = JSON.toJSONString(params);
        log.info("【直播连麦】取消混流URL:【{}】,参数:【{}】", requestUrl, param);
        String result = sendPost(requestUrl, param);
        if (StrKit.isNotEmpty(result)) {
            Map resultMap = JSON.parseObject(result, Map.class);
            if (0 == Integer.valueOf(resultMap.get("code").toString())) {
                return true;
            }
        }
        return false;

    }

    /**
     * @description: 查询流状态 0:断流 1:开启 3:关闭
     * @author: lx
     */
    public static int getStreamStatus(String key, String appId, String streamId) {
        String streamStatusUrl = getStreamStatuQueryString(key, appId, streamId);
        String resultStr = HTTPUtils.sendGetHttpRequest(streamStatusUrl);
        System.err.println(resultStr);
        log.info("【直播】流ID:【{}】，查询状态:【{}】", streamId, resultStr);
        if (StrKit.isEmpty(resultStr)) {
            return 0;
        }
        Map<String, Object> resultMap = JSON.parseObject(resultStr, Map.class);
        if (0 != Integer.valueOf(resultMap.get("ret").toString())) {
            return 0;
        }
        List<Map> list = (List) resultMap.get("output");
        Map<String, Object> output = (Map) list.get(0);
        return Integer.valueOf(output.get("status").toString());
    }


    /**
     * @description: send Post Json Data
     * @author: lx
     */
    public static String sendPost(String addr, String jsonData) {
        try {
            //创建连接
            URL url = new URL(addr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonData.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                //连接成功
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = URLDecoder.decode(lines, "utf-8");
                    sb.append(lines);
                }
                log.info("【直播连麦】请求混流响应结果:【{}】", sb.toString());
                reader.close();
                // 断开连接
                connection.disconnect();
                return sb.toString();
            } else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}