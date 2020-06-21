package com.stylefeng.guns.core.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: LEIYU
 * @description: 极光推送工具类
 * @date: 2018/3/27
 */
@Component
public class JiGuangPushUtil {

    @Value(value = "${push.secret}")
    public String JG_SECRET = "74cbfacd298adbc4dab35606";

    @Value(value = "${push.key}")
    public String JG_KEY = "34fbe262f84809bcd1be0e49";

    @Value(value = "${push.model}")
    public boolean MODEL = false;


    /**
     * @author: LEIYU
     * @description: 推送所有用户->无条件
     * @date: 2018/3/27
     */

    public boolean pushMsgAllWithouCondition(String msg) {
        try {
            PushResult pushResult = getJPushClient().sendPush(PushPayload.alertAll(msg));
            System.out.println("推送结果:" + pushResult);
            int responseCode = pushResult.getResponseCode();
            System.out.println(responseCode);
            return true;
        } catch (APIConnectionException e) {
            e.printStackTrace();
            return false;
        } catch (APIRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author: LEIYU
     * @description: 推送所有用户;
     * @date: 2018/3/27
     */
    public boolean pushMsgAllUser(String msg, Map<String, String> extra) {
        JPushClient client = getJPushClient();
        PushPayload payload = buildPushBodyForAll(msg, extra);
        return pushToJG(client, payload);
    }

    private boolean pushToJG(JPushClient client, PushPayload payload) {
        try {
            PushResult pushResult = client.sendPush(payload);
            System.out.println("推送结果:" + pushResult);
            return true;
        } catch (APIConnectionException e) {
            e.printStackTrace();
            return false;
        } catch (APIRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String pushToJG(String time, String scheduleName, JPushClient client, PushPayload payload) {
        try {
            ScheduleResult result = client.createSingleSchedule(scheduleName, time, payload);
            return result.getSchedule_id();
        } catch (APIConnectionException e) {
            e.printStackTrace();
            return null;
        } catch (APIRequestException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @author: LEIYU
     * @description: 推送指定用户;
     * @date: 2018/3/27
     */
    public boolean pushMsgAssignUser(String msg, Map<String, String> extraMsg, List<String> userIdList) {
        JPushClient client = getJPushClient();
        PushPayload payload = buildPushBodyForAssignUser(msg, extraMsg, userIdList);
        return pushToJG(client, payload);
    }

    /**
     * @author: LEIYU
     * @description: 推送指定用户;
     * @date: 2018/3/27
     */
    public boolean pushMsgAssignUser(String msg, Map<String, String> extraMsg, String... userId) {
        JPushClient client = getJPushClient();
        PushPayload payload = buildPushBodyForAssignUser(msg, extraMsg, userId);
        return pushToJG(client, payload);
    }


    /**
     * @author: LEIYU
     * @description: 定时推送到所有用户: time:推送时间  scheduleName:调度器名字  msg:推送消息
     * @date: 2018/3/27
     */
    public String schedulePushMsgAllUser(String time, String scheduleName, String msg) {
        JPushClient client = getJPushClient();
        PushPayload payload = PushPayload.alertAll(msg);
        try {
            ScheduleResult result = client.createSingleSchedule(scheduleName, time, payload);
            String scheduleId = result.getSchedule_id();
            System.out.println("定时推送结果:" + result);
            return scheduleId;
        } catch (APIConnectionException e) {
            e.printStackTrace();
            return null;
        } catch (APIRequestException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @author: LEIYU
     * @description: 定时推送到所有用户: time:推送时间  scheduleName:调度器名字  msg:推送消息  extraMsg:业务参数
     * @date: 2018/3/27
     */
    public String schedulePushMsgAllUser(String time, String scheduleName, String msg, Map<String, String> extraMsg) {
        JPushClient client = getJPushClient();
        PushPayload payload = buildPushBodyForAll(msg, extraMsg);
        return pushToJG(time, scheduleName, client, payload);
    }

    /**
     * @author: LEIYU
     * @description: 定时推送到指定用户: time:推送时间  scheduleName:调度器名字  msg:推送消息  extraMsg:业务参数  userIdList:推送用户
     * @date: 2018/3/27
     */
    public String schedulePushMsgAssignUser(String time, String scheduleName, String msg, Map<String, String> extraMsg, List<String> userIdList) {
        JPushClient client = getJPushClient();
        PushPayload payload = buildPushBodyForAssignUser(msg, extraMsg, userIdList);
        return pushToJG(time, scheduleName, client, payload);
    }

    /**
     * @author: LEIYU
     * @description: 定时推送到指定用户: time:推送时间  scheduleName:调度器名字  msg:推送消息  extraMsg:业务参数  userId:推送用户
     * @date: 2018/3/27
     */
    public String schedulePushMsgAssignUser(String time, String scheduleName, String msg, Map<String, String> extraMsg, String... userId) {
        JPushClient client = getJPushClient();
        PushPayload payload = buildPushBodyForAssignUser(msg, extraMsg, userId);
        return pushToJG(time, scheduleName, client, payload);
    }


    /**
     * @author: LEIYU
     * @description: 删除定时任务的推送:scheduleId :定时推送调度器ID->返回的ID
     * @date: 2018/3/28
     */
    public boolean delSchedulePush(String scheduleId) {
        JPushClient client = getJPushClient();
        try {
            client.deleteSchedule(scheduleId);
            return true;
        } catch (APIConnectionException e) {
            e.printStackTrace();
            return false;
        } catch (APIRequestException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @author: LEIYU
     * @description: 创建推送所有用户->带额外消息
     * @date: 2018/3/27
     */
    private PushPayload buildPushBodyForAll(String msg, Map<String, String> extras) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(msg)
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder().addExtras(extras).build())
                                .addPlatformNotification(
                                        IosNotification.newBuilder().addExtras(extras).build())
                                .build())
                //设置ios平台环境  True 表示推送生产环境，False 表示要推送开发环境   默认是开发
                .setOptions(Options.newBuilder().setApnsProduction(MODEL).build()).build();
    }


    /**
     * @author: LEIYU
     * @description: 创建推送指定用户;-->List形式
     * @date: 2018/3/27
     */
    private PushPayload buildPushBodyForAssignUser(String msg, Map<String, String> extras, List<String> userIdList) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(userIdList))
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(msg)
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder().addExtras(extras).build())
                                .addPlatformNotification(
                                        IosNotification.newBuilder().addExtras(extras).build())
                                .build())
                //设置ios平台环境  True 表示推送生产环境，False 表示要推送开发环境   默认是开发
                .setOptions(Options.newBuilder().setApnsProduction(MODEL).build()).build();
    }

    /**
     * @author: LEIYU
     * @description: 创建推送指定用户;->可变参数形式
     * @date: 2018/3/27
     */
    private PushPayload buildPushBodyForAssignUser(String msg, Map<String, String> extras, String... userId) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(userId))
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(msg)
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder().addExtras(extras).build())
                                .addPlatformNotification(
                                        IosNotification.newBuilder().addExtras(extras).setSound("happy").build())
                                .build())
                //设置ios平台环境  True 表示推送生产环境，False 表示要推送开发环境   默认是开发
                .setOptions(Options.newBuilder().setApnsProduction(MODEL).build())
                .build();
    }


    /**
     * @author: LEIYU
     * @description: 获取极光推送实例
     * @date: 2018/3/27
     */
    private JPushClient getJPushClient() {
        return new JPushClient(JG_SECRET, JG_KEY);
    }

    public static void main(String[] args) {
        new JiGuangPushUtil().pushMsgAllWithouCondition("谢谢谢谢");
    }
}
