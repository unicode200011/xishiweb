package com.stylefeng.guns.admin.modular.moneyManage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.ChargeRecord;
import com.stylefeng.guns.persistence.dao.ChargeRecordMapper;
import com.stylefeng.guns.admin.modular.moneyManage.service.IChargeRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Service
public class ChargeRecordServiceImpl extends ServiceImpl<ChargeRecordMapper, ChargeRecord> implements IChargeRecordService {

    @Override
    public Page<ChargeRecord> selectChargeRecordPage(Page<ChargeRecord> chargeRecordPage, EntityWrapper<ChargeRecord> entityWrapper) {
        return chargeRecordPage.setRecords(baseMapper.selectChargeRecordPage(chargeRecordPage,entityWrapper));
    }
}
