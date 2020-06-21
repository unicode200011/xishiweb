package com.stylefeng.guns.admin.modular.moneyManage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.UserWalletRecord;
import com.stylefeng.guns.persistence.dao.UserWalletRecordMapper;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户钱包记录 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Service
public class UserWalletRecordServiceImpl extends ServiceImpl<UserWalletRecordMapper, UserWalletRecord> implements IUserWalletRecordService {

    @Override
    public Page<UserWalletRecord> selectUserWalletRecordPage(Page<UserWalletRecord> userWalletRecordPage, EntityWrapper<UserWalletRecord> entityWrapper) {
        return userWalletRecordPage.setRecords(baseMapper.selectUserWalletRecordPage(userWalletRecordPage,entityWrapper));
    }

    @Override
    public Page<UserWalletRecord> selectInfos(Page<UserWalletRecord> page, Long userId, String startTime, String endTime) {
     return page.setRecords(baseMapper.selectInfos(page, userId, startTime, endTime));
    }
}
