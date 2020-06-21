package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 代理商
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-26
 */
@Data
@TableName("eb_agent")
public class Agent extends Model<Agent> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 代理商编号
     */
    @TableField("agent_num")
    private String agentNum;
    /**
     * 绑定手机号
     */
    @TableField("agent_phone")
    private String agentPhone;
    /**
     * 审核状态 0：待审核 1：通过 2：驳回
     */
    @TableField("audit_state")
    private Integer auditState;
    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;
    /**
     * 拒绝理由
     */
    private String reason;
    /**
     * 状态  0：正常 1：已禁用
     */
    private Integer state;
    /**
     * 上传电影权限 0：有  1：无
     */
    private Integer permission;
    /**
     * 申请人id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 代理人姓名
     */
    @TableField("agent_user_name")
    private String agentUserName;
    /**
     * 家族名称
     */
    @TableField("agent_name")
    private String agentName;
    /**
     * 身份证信息
     */
    private String card;
    /**
     * 家族Logo
     */
    @TableField("agent_logo")
    private String agentLogo;
    /**
     * 家族简介
     */
    @TableField("agent_intro")
    private String agentIntro;
    /**
     * 身份证正面照
     */
    @TableField("card_front")
    private String cardFront;
    /**
     * 身份证反面照
     */
    @TableField("card_back")
    private String cardBack;
    /**
     * 申请时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 主播个数
     */
    @TableField("shower_num")
    private Integer showerNum;
    /**
     * 创建方式 0 ： app申请  1：后台创建
     */
    @TableField("build_type")
    private Integer buildType;

    @TableField(exist = false)
    private BigDecimal totalGiftAmount;

    @TableField("admin_rate")
    private BigDecimal adminRate;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
