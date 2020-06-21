package com.stylefeng.guns.admin.modular.agentManage.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.admin.modular.agentManage.service.IBankUserService;
import com.stylefeng.guns.persistence.dao.BankUserMapper;
import com.stylefeng.guns.persistence.model.BankUser;
import org.springframework.stereotype.Service;

@Service
public class IBankUserServiceImpl extends ServiceImpl<BankUserMapper, BankUser> implements IBankUserService {
}
