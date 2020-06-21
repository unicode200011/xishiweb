package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.PayRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 支付记录(支付宝,微信) Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-07-02
 */
@Component
public interface PayRecordMapper extends BaseMapper<PayRecord> {

    List<PayRecord> selectPayRecordPage(Page<PayRecord> payRecordPage, @Param("ew") EntityWrapper<PayRecord> entityWrapper);
}
