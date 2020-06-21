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
 * 用户信息
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-10
 */
@Data
@TableName("eb_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户手机号/登录名
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * salt
     */
    private String salt;
    /**
     * 昵称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 区县
     */
    private String county;

    /**
     * 城市
     */
    private String city;
    /**
     * 省份
     */
    private String province;
    /**
     * 性别: 1:男2:女  3:保密
     */
    private Integer gender;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 个人简介
     */
    private String intro;
    /**
     * 支付密码
     */
    @TableField("pay_pwd")
    private String payPwd;
    /**
     * 用户总状态 0:正常 1:冻结  2:删除
     */
    private Integer state;
    /**
     * QQ
     */
    private String qq;
    /**
     * VX
     */
    private String wx;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 用户来源: 0 注册  1 系统添加
     */
    private Integer source;
    /**
     * 设备号
     */
    private String machine;
    /**
     * 最后登录时间
     */
    @TableField("login_time")
    private Date loginTime;
    /**
     * 注册时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 版本号
     */
    private Long version;
    /**
     * 西施id
     */
    @TableField("xishi_num")
    private String xishiNum;
    /**
     * 用户在线状态0:离线,1:在线 2:忙碌
     */
    @TableField("online_state")
    private Integer onlineState;
    /**
     * 权重顺序
     */
    @TableField("weight_sort")
    private Integer weightSort;
    /**
     * 是否推荐1是0否
     */
    private Integer recommended;
    /**
     * 所属家族
     */
    @TableField("belong_agent")
    private Long belongAgent;
    @TableField(exist = false)
    private String agentName;
    /**
     * 主播认证申请状态:1:已申请2:未申请3:拒绝4:通过
     */
    @TableField("apply_status")
    private Integer applyStatus;

    /**
     * 是否申请创建家族0：否 1：通过 2：驳回 3：申请中
     */
    @TableField("apply_agent")
    private Integer applyAgent;


    /**
     * 注册方式 0：手机 1：微信 2：qq
     */
    @TableField("registered_state")
    private Integer registeredState;
    /**
     * 主播认证通过时间
     */
    @TableField("apply_status_time")
    private Date applyStatusTime;
    /**
     * 是否主播 0 ： 否 1：是
     */
    private Integer shower;
    /**
     * 用户等级
     */
    @TableField("user_level")
    private Integer userLevel;
    /**
     * 直播等级
     */
    @TableField("live_level")
    private Integer liveLevel;
    @TableField("fans_num")
    private Integer fansNum;

    private String job;

    private String ip;

    @TableField("agent_rate")
    private BigDecimal agentRate;

    @TableField(exist = false)
    private BigDecimal adminRate;

    @TableField("newest_apply_agent_id")
    private Long newestApplyAgentId;

    @TableField(exist = false)
    private BigDecimal giveAmount = BigDecimal.ZERO;

    @TableField(exist = false)
    private BigDecimal gbMoeny = BigDecimal.ZERO;

    @TableField(exist = false)
    private BigDecimal nearCharge = BigDecimal.ZERO;

    @TableField(exist = false)
    private BigDecimal totalCharge = BigDecimal.ZERO;

    @TableField(exist = false)
    private BigDecimal giftAmount = BigDecimal.ZERO;

    @TableField(exist = false)
    private Long totalTime;

    @TableField(exist = false)
    private Integer joinState = 3;

    @TableField(exist = false)
    private Long inviteId;

    @TableField("is_create_agent")
    private Integer isCreateAgent;

    @TableField(exist = false)
    private Long agentId;

    @TableField(exist = false)
    private Integer withdrawNum;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
