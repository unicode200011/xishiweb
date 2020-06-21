package com.stylefeng.guns.admin.modular.moneyManage.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.ChargeRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface IChargeRecordService extends IService<ChargeRecord> {

    Page<ChargeRecord> selectChargeRecordPage(Page<ChargeRecord> chargeRecordPage, EntityWrapper<ChargeRecord> entityWrapper);
}
