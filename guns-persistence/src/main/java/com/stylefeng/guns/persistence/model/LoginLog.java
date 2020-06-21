package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 登录记录
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
@Getter
@Setter
@ToString
@TableName("eb_login_log")
public class LoginLog extends Model<LoginLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 日志名称
     */
	private String logname;
    /**
     * 管理员id
     */
	private Integer adminid;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 是否执行成功
     */
	private String succeed;
    /**
     * 具体消息
     */
	private String message;
    /**
     * 登录ip
     */
	private String ip;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
