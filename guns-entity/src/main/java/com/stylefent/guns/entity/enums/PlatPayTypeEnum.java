package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 平台 收支类型 类型:\n
 * 0.视频解锁券补贴\n
 * 1.视频解锁补贴\n
 * 2.视频评论券不贴\n
 * 3.视频评论不贴\n
 * 4.礼物聊天券不贴\n
 * 5.礼物聊天不贴\n
 * 6.视频解锁抽成收入\n
 * 7.视频评论抽成收入\n
 * 8.礼物聊天抽成收入\n
 * 9.应赏抽成收入\n
 * 10.提现抽成收入
 *
 * @author: LEIYU
 */
@Getter
public enum PlatPayTypeEnum {

    Video_Unlock_Ticket(0, "视频解锁券补贴"),
    Video_Unlock(1, "视频解锁补贴"),
    Video_Comment_Ticket(2, "视频评论券补贴"),
    Video_Comment(3, "视频评论补贴"),
    Gift_Chat_Ticket(4, "礼物聊天券补贴"),
    Gift_Chat(5, "礼物聊天补贴"),
    Video_Unlock_Income(6, "视频解锁抽成"),
    Video_Comment_Income(7, "视频评论抽成"),
    Gift_Chat_Income(8, "礼物聊天抽成"),
    Accept_Bounty(9, "应赏抽成"),
    Withdraw(10, "提现抽成"),;

    private int code;
    private String msg;

    PlatPayTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static PlatPayTypeEnum getByCode(int code) {
        for (PlatPayTypeEnum payTypeEnum : values()) {
            if (payTypeEnum.getCode() == code) {
                return payTypeEnum;
            }
        }
        return null;
    }
}
