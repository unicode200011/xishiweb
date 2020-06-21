package com.stylefeng.guns.admin.modular.moneyManage.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.UserWalletRecord;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户钱包记录 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface IUserWalletRecordService extends IService<UserWalletRecord> {

    Page<UserWalletRecord> selectUserWalletRecordPage(Page<UserWalletRecord> userWalletRecordPage, EntityWrapper<UserWalletRecord> entityWrapper);

    Page<UserWalletRecord> selectInfos(Page<UserWalletRecord> page, Long userId, String startTime, String endTime);
}
