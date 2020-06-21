package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.persistence.model.UserWallet;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface UserWalletMapper extends BaseMapper<UserWallet> {

    UserWallet selectByUserId(Long id);

    BigDecimal selectNearCharge(Long id);
}
