package com.stylefeng.guns.admin.modular.moneyManage.service.impl;

import com.stylefeng.guns.persistence.model.BankUser;
import com.stylefeng.guns.persistence.dao.BankUserMapper;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户银行账户信息 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2020-03-02
 */
@Service
public class BankUserServiceImpl extends ServiceImpl<BankUserMapper, BankUser> implements IBankUserService {

}
