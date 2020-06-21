package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.Withdrawal;

import java.util.List;

/**
 * <p>
 * 提现 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface WithdrawalMapper extends BaseMapper<Withdrawal> {
    List<Withdrawal> selectWithdrawal(Withdrawal withdrawal, Page page);
}
