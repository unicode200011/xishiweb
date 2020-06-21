package com.stylefeng.guns.admin.modular.moneyManage.service.impl;

import com.stylefeng.guns.persistence.model.BankAccount;
import com.stylefeng.guns.persistence.dao.BankAccountMapper;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankAccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-09
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

}
