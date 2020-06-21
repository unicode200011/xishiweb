package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户银行账户信息
 * </p>
 *
 * @author stylefeng
 * @since 2020-03-02
 */
@TableName("eb_bank_user")
@Data
public class BankUser extends Model<BankUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账户分类id
     */
    @TableField("bank_account_id")
    private Integer bankAccountId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 卡号信息
     */
    @TableField("bank_card")
    private String bankCard;
    /**
     * 二维码
     */
    private String qrcode;
    @TableField("create_time")
    private Date createTime;
    /**
     * 类型0：用户账号 1：代理商账号
     */
    private Integer type;

    @TableField(exist = false)
    private String userAccountName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
