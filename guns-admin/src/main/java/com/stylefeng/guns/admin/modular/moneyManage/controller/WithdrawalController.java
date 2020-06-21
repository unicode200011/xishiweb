package com.stylefeng.guns.admin.modular.moneyManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentService;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankAccountService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankUserService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletService;
import com.stylefeng.guns.admin.modular.showerManage.service.IUserAuthInfoService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.admin.modular.moneyManage.service.IWithdrawalService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提现记录列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-06 14:35:27
 */
@Controller
@RequestMapping("/withdrawal")
public class WithdrawalController extends BaseController {

    private String PREFIX = "/moneyManage/withdrawal/";
    @Autowired
    private IUserAuthInfoService authInfoService;

    @Autowired
    private IWithdrawalService withdrawalService;
    @Autowired
    private IBankAccountService bankAccountService;
    @Autowired
    private IBankUserService bankUserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IUserWalletService userWalletService;

    @Autowired
    private IAgentWalletService agentWalletService;

    /**
     * 跳转到提现记录列表首页
     */
    @RequestMapping("")
    public String index(Model model) {
        List<BankAccount> bankAccounts = bankAccountService.selectList(new EntityWrapper<>());
        model.addAttribute("bankAccounts",bankAccounts);
        return PREFIX + "withdrawal.html";
    }

    /**
     * 跳转到添加提现记录列表
     */
    @RequestMapping("/withdrawal_add")
    public String withdrawalAdd() {
        return PREFIX + "withdrawal_add.html";
    }

    /**0
     * 跳转到修改提现记录列表
     */
    @RequestMapping("/withdrawal_update/{withdrawalId}")
    public String withdrawalUpdate(@PathVariable Integer withdrawalId, Model model) {
        Withdrawal withdrawal = withdrawalService.selectById(withdrawalId);
        model.addAttribute("item",withdrawal);
        LogObjectHolder.me().set(withdrawal);
        return PREFIX + "withdrawal_edit.html";
    }

    /**
     * 获取提现记录列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                           String condition,String type,Integer state,String source, Integer stateTwo) {
        EntityWrapper<Withdrawal> entityWrapper = new EntityWrapper();
        //状态 0: 待审核 1:通过 2:进行中 3:驳回4已打款

        if(StrKit.isNotEmpty(type)) entityWrapper.eq("type",type);
        if(state != null) entityWrapper.eq("state",state);
        if(stateTwo != null) entityWrapper.eq("state",stateTwo);
        if(StrKit.isNotEmpty(condition)) {
            String[] split = condition.split("[ - ]");
            entityWrapper.between("create_time",split[0]+"00:00:00",split[2]+"23:59:59");
        }

        if(StrKit.isNotEmpty(source)){
            List<BankUser> bankUsers = bankUserService.selectList(new EntityWrapper<BankUser>().eq("bank_account_id", source));
            if(CollectionUtils.isNotEmpty(bankUsers)){
                List<Long> collect = bankUsers.stream().map(BankUser::getId).collect(Collectors.toList());
                entityWrapper.in("account",collect);
            }else {
                entityWrapper.eq("account",0);
            }
        }


        entityWrapper.orderBy("id",false);
        entityWrapper.ne("type", 2);

        Page<Withdrawal> page = withdrawalService.selectPage(new Page<Withdrawal>(pageNumber, pageSize), entityWrapper);
        List<Withdrawal> records = page.getRecords();
        List<Withdrawal> list = new ArrayList<>();

        System.out.println("records:" + records);
        for (Withdrawal record : records) {
            record.setAuditState(record.getState());

            User user = userService.selectOne(new EntityWrapper<User>().eq("id", record.getUserId()).eq("belong_agent", 0));
            if(record.getType() == 0){
                //User user = userService.selectById(record.getUserId());
                System.out.println("admin:" + user);
                if(user != null){
                    record.setUserName(user.getName());
                    record.setXishiNum(user.getXishiNum());
                    List<UserAuthInfo> userAuthInfos = authInfoService.selectList(new EntityWrapper<UserAuthInfo>().eq("user_id", record.getUserId()));
                    if(CollectionUtils.isNotEmpty(userAuthInfos)){
                        UserAuthInfo userAuthInfo = userAuthInfos.get(0);
                        record.setRealName(userAuthInfo.getName());
                    }


                }


            }else {
                Agent agent = agentService.selectById(record.getAgentId());
                record.setAgentNum(agent.getAgentNum());
                record.setUserName(agent.getAgentName());
                List<UserAuthInfo> userAuthInfos = authInfoService.selectList(new EntityWrapper<UserAuthInfo>().eq("user_id", agent.getUserId()));
                if(CollectionUtils.isNotEmpty(userAuthInfos)){
                    UserAuthInfo userAuthInfo = userAuthInfos.get(0);
                    record.setRealName(userAuthInfo.getName());
                }
            }

            Agent agent = agentService.selectById(record.getAgentId());

            if(agent != null){

                BankUser bankUser = bankUserService.selectOne(new EntityWrapper<BankUser>().eq("user_id", agent.getUserId()).eq("bank_account_id", record.getAccount()));
                System.out.println("bankUser:" + bankUser);
                System.out.println("agentId:" + agent.getUserId());

                if(bankUser != null){
                    BankAccount bankAccount = bankAccountService.selectById(bankUser.getBankAccountId());
                    record.setAccountName(bankAccount.getName());
                    record.setAccountNum(bankUser.getBankCard());
                    record.setQrcode(bankUser.getQrcode());
                    System.out.println(bankAccount);
                }
            }else {
                BankUser bankUser = bankUserService.selectById(record.getAccount());
                if(bankUser != null){
                    BankAccount bankAccount = bankAccountService.selectById(bankUser.getBankAccountId());
                    record.setAccountName(bankAccount.getName());
                    record.setAccountNum(bankUser.getBankCard());
                    record.setQrcode(bankUser.getQrcode());
                }
            }



                list.add(record);


        }



        return page.setRecords(list);
    }

    /**
     * 新增提现记录列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增提现记录列表")
    public Object add(Withdrawal withdrawal) {
        withdrawalService.insert(withdrawal);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除提现记录列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除提现记录列表")
    @ResponseBody
    public Object delete(@RequestParam Long withdrawalId) {
        withdrawalService.deleteById(withdrawalId);
        return SUCCESS_TIP;
    }
 /**
     * 删除提现记录列表
     */
    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "审核")
    @ResponseBody
    public Object changeState(Long withdrawalId,Integer state) {
        Withdrawal withdrawal = withdrawalService.selectById(withdrawalId);
        if(state != 4 && withdrawal.getState() != 0){
            throw new GunsException(502,"该记录已审核");
        }else if(state == 4 && withdrawal.getState() != 1){
            throw new GunsException(502,"该记录未审核通过");
        }
        withdrawal.setState(state);

        withdrawalService.updateById(withdrawal);
        if(state == 3){//驳回
            if(withdrawal.getType() == 0){
                UserWallet userWallet = userWalletService.selectByUserId(withdrawal.getUserId());
                BigDecimal giftAmount = userWallet.getGiftAmount();
                BigDecimal add = giftAmount.add(withdrawal.getMoney());
                userWallet.setGiftAmount(add);
                userWalletService.updateById(userWallet);
            }else{
                AgentWallet agentWallet = agentWalletService.selectOne(new EntityWrapper<AgentWallet>().eq("agent_id", withdrawal.getAgentId()));
                agentWallet.setGbAmount(agentWallet.getGbAmount().add(withdrawal.getMoney()));
                agentWalletService.updateById(agentWallet);
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改提现记录列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改提现记录列表")
    public Object update(Withdrawal withdrawal) {
        withdrawalService.updateById(withdrawal);
        return super.SUCCESS_TIP;
    }

    /**
     * 提现记录列表详情
     */
    @RequestMapping(value = "/detail/{withdrawalId}")
    @ResponseBody
    public Object detail(@PathVariable("withdrawalId") Integer withdrawalId) {
        return withdrawalService.selectById(withdrawalId);
    }
}
