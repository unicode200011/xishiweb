package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
@Getter
@Setter
@ToString
@TableName("eb_role")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 序号
     */
	private Integer num;
    /**
     * 父角色id
     */
	private Integer pid;
    /**
     * 角色名称
     */
	private String name;
    /**
     * 部门名称
     */
	private Integer deptid;
    /**
     * 提示
     */
	private String tips;
    /**
     * 保留字段(暂时没用）
     */
	private Integer version;

	/**
	 * 角色描述
	 */
	private String describetion;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
