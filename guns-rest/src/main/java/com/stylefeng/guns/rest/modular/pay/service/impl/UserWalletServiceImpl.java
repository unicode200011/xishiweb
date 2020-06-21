package com.stylefeng.guns.rest.modular.pay.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.MakeOrderNum;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.ChargeListMapper;
import com.stylefeng.guns.persistence.dao.UserWalletMapper;
import com.stylefeng.guns.persistence.dao.UserWalletRecordMapper;
import com.stylefeng.guns.persistence.dao.WithdrawalMapper;
import com.stylefeng.guns.persistence.model.*;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.common.service.ICommonService;
import com.stylefeng.guns.rest.modular.pay.service.IUserWalletService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.enums.ConsumeType;
import com.stylefent.guns.entity.enums.WalletExpendTypeEnum;
import com.stylefent.guns.entity.query.CommonQuery;
import com.stylefent.guns.entity.query.WithdrawalQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: lx
 */
@Service
@Slf4j
public class UserWalletServiceImpl implements IUserWalletService {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ICommonService commonService;


    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private WithdrawalMapper withdrawalMapper;


    @Autowired
    private UserWalletRecordMapper userWalletRecordMapper;
    @Autowired
    private ChargeListMapper chargeListMapper;




    @Override
    public BaseJson withdraw(WithdrawalQuery query) {
        Long userId = query.getUserId();
        Integer way = query.getWay();
        String account = query.getAccount();
        String userName = query.getUserName();
        BigDecimal money = query.getMoney();

        User dbUser = userService.selectById(userId);
        if (!userService.checkUser(dbUser)) return BaseJson.userNotExist();

//        Object o = this.redisService.get(SystemConstants.WITHDRWAL_PRIFIX_.concat(userId.toString()));
//        if (null != o) return BaseJson.ofFail("每天只能提现一次");
//
//        this.redisService.set(SystemConstants.WITHDRWAL_PRIFIX_.concat(userId.toString()),"0", DateUtils.getleftSec(new Date()));
//
//        if (money == null || money.compareTo(BigDecimal.TEN) < 0) {
//            return BaseJson.ofFail("提现金额不得少于10元");
//        }
//
//        if (money.intValue() % 10 != 0) {
//            return BaseJson.ofFail("提现金额必须是10的倍数");
//        }

        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        BigDecimal userMoney = userWallet.getMoney();
        if (userMoney.compareTo(money) < 0) {
            return BaseJson.ofFail("可提现金额不足");
        }


        //提现手续费
        CommonData txRateData = commonService.findCommonDataById(2L);
        String value = txRateData.getValue();
        BigDecimal txRate = new BigDecimal(value).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN);
        BigDecimal rateMoney = money.multiply(txRate);
        //手续费最低1元,如果低于1元,补齐1元
        if (rateMoney.compareTo(BigDecimal.ONE) < 0) {
            rateMoney = BigDecimal.ONE;
        }
        BigDecimal realMoney = money.subtract(rateMoney);
        userWallet.setMoney(userMoney.subtract(money));
        userWallet.setUpdateTime(new Date());
        Integer count = userWalletMapper.updateById(userWallet);
        if (count < 1) {
            log.info("【提现】钱包更新失败，用户提现失败,用户ID=【{}】", userId);
            return BaseJson.ofFail("提现失败,请稍后再试");
        }

        Withdrawal insert = new Withdrawal();
        insert.setUserId(userId);
        insert.setUserName(dbUser.getName());
        insert.setAccount(dbUser.getPhone());
        insert.setMoney(money);
        insert.setRate(txRate);
        insert.setRateMoney(rateMoney);
        insert.setRealMoney(realMoney);
        insert.setOrderNum(MakeOrderNum.makeOrderNum("TX"));
        insert.setTradeNum("");
        insert.setWay(way);
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());

        withdrawalMapper.insert(insert);
        log.info("【提现】用户审核提现成功,用户ID=【{}】,金额=【{}】,手续费率=【{}】,手续费=【{}】,实际到账金额=【{}】", userId, money, txRate, rateMoney, realMoney);
        recordWalletDetail(ConsumeType.WithDraw, userId, money, WalletExpendTypeEnum.EXPEND, "");
//        redisService.remove(SystemConstants.PAY_PWD_INPUT.concat(userId.toString()));
        return BaseJson.ofSuccess("提交成功,请耐心等待审核!");
    }

    @Override
    public BaseJson withdrawRecord(CommonQuery query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<Map> records = withdrawalMapper.selectUserWithdrawRecord(query.getUserId());
        return BaseJson.ofSuccess(records);
    }



    /**
     * 记录用户钱包变动
     *
     * @param type     消费类型
     * @param userId   用户ID
     * @param money    金额
     * @param userType 收支类型 0:收入 1:支出
     */
    @Override
    public void recordWalletDetail(ConsumeType type, Long userId, BigDecimal money, WalletExpendTypeEnum userType, String remark) {
        UserWalletRecord insert = new UserWalletRecord();
        insert.setUserId(userId);
        insert.setType(type.getCode());
        insert.setCreateTime(new Date());
        insert.setAmount(money);
        insert.setUseType(userType.getCode());
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        insert.setRmbMoney(userWallet.getMoney());
        if (StrKit.isNotEmpty(remark)) {
            insert.setRemark(remark);
        } else {
            switch (type) {
                case WithDraw:
                    if (userType.equals(WalletExpendTypeEnum.INCOME)) {
                        insert.setRemark("提现失败退款");
                    } else {
                        insert.setRemark("提现");
                    }
                    break;
                case ChargeNp:
                    insert.setRemark(remark);
                    break;
                default:
                    insert.setRemark(type.getSign());
                    break;
            }
        }
        userWalletRecordMapper.insert(insert);
    }

    @Override
    public BaseJson chargeList(CommonQuery query) {
        List<ChargeList> chargeLists = chargeListMapper.selectList(new EntityWrapper<ChargeList>().orderBy("gold_num",true));
        return BaseJson.ofSuccess(chargeLists);
    }

}
