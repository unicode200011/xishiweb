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
 * 充值列表
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Data
@TableName("eb_charge_list")
public class ChargeList extends Model<ChargeList> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 金豆数量
     */
    @TableField("xishi_num")
    private Integer xishiNum;
    /**
     * 所需人民币
     */
    private BigDecimal rmb;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 赠送比例
     */
    private BigDecimal rate;
    /**
     * 备注
     */
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
