package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.UserWalletRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户钱包记录 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface UserWalletRecordMapper extends BaseMapper<UserWalletRecord> {

    List<UserWalletRecord> selectUserWalletRecordPage(Page<UserWalletRecord> userWalletRecordPage,@Param("ew") EntityWrapper<UserWalletRecord> entityWrapper);
    List<UserWalletRecord> selectUserWalletRecordList(@Param("ew") EntityWrapper<UserWalletRecord> entityWrapper);
    List<UserWalletRecord> selectInfos(Page<UserWalletRecord> userWalletRecordPage, @Param("userId") Long userId, @Param("startTime") String startTime, @Param("endTime") String endTime);
    BigDecimal getDayMoney(@Param("time") String time);
    Map getData();
}
