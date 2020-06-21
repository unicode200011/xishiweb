package com.stylefeng.guns.admin.common.entity;

import lombok.Data;

/**
 * 消息推送文案
 */
@Data
public class MessageContent {
    /**
     * 大写字母 为可替换文本内容
     */

    //小家成员的增加（角色调整）
    public static final String HOME_USER_ADD = "ROLE加入了浪漫满屋";

    //小家成员的退出（角色调整）
    public static final String HOME_USER_QUIT = "ROLE退出了浪漫满屋";

    //宝宝的添加
    public static final String BABY_ADD = "HOMENAME中添加了新的宝贝“BABYNAME”";

    //宝宝的删除
    public static final String BABY_REMOVE = "HOMENAME中的宝贝“BABYNAME”被删除";

    //完成小家切换
    public static final String HOME_CHANGE = "您已切换至小家：HOMENAME";

    //小家会员激活
    public static final String HOME_VIP_START = "HOMENAME已激活成为会员，会员到期时间为2020年9月9日";

    //小家会员到期
    public static final String HOME_VIP_OVERTIME = "小家会员已到期，将不再享受会员权益";

    //管理员权限的移交
    public static final String HOME_MANAGER_CHANGE = "小家管理员权限已被移交至ROLE";

    //订单物流消息-已发货
    public static final String ORDER_SEND_GOODS = "您购买的商品：GOODSNAME已发货";

    //订单物流消息-已发货
    public static final String ORDER_RECEIVE = "您购买的商品：GOODSNAME已成功签收";

    //订单-售后申请通过
    public static final String ORDER_REFUND_PASS = "您申请退款/退货的商品：GOODSNAME已通过";

    //订单-售后申请拒绝
    public static final String ORDER_REFUND_REFUSE = "您申请退款/退货的商品：GOODSNAME已被拒绝";

    //订单-售后-卖家确认收货
    public static final String ORDER_REFUND_RECEIVE = "您申请退货的商品：GOODSNAME卖家已确定收货";

    //订单-订单被关闭
    public static final String ORDER_CLOSE = "您购买的商品：GOODSNAME订单已被关闭";

    //评论被点赞
    public static final String COMMENT_RISE = "ROLE赞了您的一条评论";

    //收到的评论/回复
    public static final String COMMENT_RECEIVE = "您收到了一条来自ROLE的评论";

    //宝宝地理围栏预警
    public static final String BABY_WARN = "警告！宝贝BABYNAME已超出预定范围进行活动！";

    //爸爸陪伴提醒
    public static final String DAD_WITH = "计划10分钟后将开始对宝宝进行陪伴，快去准备一下吧";

    //我组建的战队新参员
    public static final String WAR_TEAM_ADD = "您所在GALAXYNAME的战队有新的队员加入了";

    //我组建的战队成员退出
    public static final String WAR_TEAM_QUIT = "有队员已退出了您所在GALAXYNAME的战队";

    //优惠券
    public static final String COUPON_ADD = "有一个新的优惠券已发送至您，快去查看使用吧！";
}
