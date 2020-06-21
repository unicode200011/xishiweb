package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.ChargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface ChargeRecordMapper extends BaseMapper<ChargeRecord> {

    List<ChargeRecord> selectChargeRecordPage(Page<ChargeRecord> chargeRecordPage,@Param("ew") EntityWrapper<ChargeRecord> entityWrapper);
}
