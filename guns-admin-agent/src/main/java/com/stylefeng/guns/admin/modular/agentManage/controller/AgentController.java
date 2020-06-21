package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.core.shiro.ShiroUser;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentDealRecordService;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletService;
import com.stylefeng.guns.admin.modular.agentManage.service.IBankUserService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankAccountService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IWithdrawalService;

import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.MakeOrderNum;
import com.stylefeng.guns.persistence.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代理商控制器
 *
 * @author stylefeng
 * @Date 2019-11-26 10:08:31
 */
@Controller
@RequestMapping("/agent")
public class AgentController extends BaseController {

    private String PREFIX = "/agentManage/agent/";

    @Autowired
    private IAgentService agentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserWalletService userWalletService;

    @Autowired
    private IWithdrawalService withdrawalService;

    @Autowired
    private IBankUserService bankUserService;

    @Autowired
    private IBankAccountService bankAccountService;

    @Autowired
    private IAgentWalletService agentWalletService;




    /**
     * 跳转到代理商首页
     */
    @RequestMapping("")
    public String index(Model model) {
        Integer deptId = ShiroKit.getUser().getDeptId();
        System.out.println("deptId:" + deptId);
        Agent agent = agentService.selectById(deptId);
        model.addAttribute("agent",agent);
        System.out.println("agent:" + agent);
        AgentWallet agentWallet = agentWalletService.selectOne(new EntityWrapper<AgentWallet>().eq("agent_id", agent.getId()));
       // List<Withdrawal> list = withdrawalService.selectList(new EntityWrapper<Withdrawal>().eq("agent_id", agent.getId()).in("state", "1,4").eq("type", 2));
        //BigDecimal reduce = list.stream().map(Withdrawal::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        //System.out.println("reduce:" + reduce);

        //agentWallet.setGbAmount(agentWallet.getGbAmount().subtract(reduce));
        model.addAttribute("agentWallet", agentWallet);
        User user = userService.selectById(agent.getUserId());
        UserWallet uw = userWalletService.selectOne(new EntityWrapper<UserWallet>().eq("user_id", user.getId()));
        user.setGiftAmount(uw.getGiftAmount());
        user.setGbMoeny(uw.getGbMoeny());
        model.addAttribute("user",user);

        return PREFIX + "agent.html";
    }

    /**
     * 跳转到添加代理商
     */
    @RequestMapping("/agent_add")
    public String agentAdd() {
        return PREFIX + "agent_add.html";
    }

    /**
     * 跳转到添加代理商
     */
    @RequestMapping("/openInfo/{userId}")
    public String openInfo(Model model, @PathVariable Integer userId) {
        List<BankAccount> bankAccounts = bankAccountService.selectList(new EntityWrapper<BankAccount>());
        model.addAttribute("bankAccounts", bankAccounts);
        model.addAttribute("userId", userId);
        return PREFIX + "info_add.html";
    }

    /**
     * 跳转到提现
     */
    @RequestMapping("/openWithdrawal/{userId}")
    public String openWithdrawal(Model model, @PathVariable Integer userId) {
        System.out.println(userId);
        List<BankUser> bankUsers = bankUserService.selectList(new EntityWrapper<BankUser>().eq("user_id", userId).eq("type", 1));
        Agent agent = agentService.selectOne(new EntityWrapper<Agent>().eq("user_id", userId).eq("state", 0).eq("audit_state", 1));
        List<BankAccount> bankAccounts = new ArrayList<>();
        for(BankUser bankUser : bankUsers){
            BankAccount bankAccount = bankAccountService.selectOne(new EntityWrapper<BankAccount>().eq("id", bankUser.getBankAccountId()));
            bankAccounts.add(bankAccount);
        }
        model.addAttribute("bankAccounts", bankAccounts);
        model.addAttribute("agentId", agent.getId());

        return PREFIX + "withdrawal.html";
    }

    /**
     * 跳转到修改代理商
     */
    @RequestMapping("/agent_update/{agentId}")
    public String agentUpdate(@PathVariable Integer agentId, Model model) {
        Agent agent = agentService.selectById(agentId);
        model.addAttribute("item",agent);

        List<AgentDealRecord> records = agentDealRecordService.selectList(new EntityWrapper<AgentDealRecord>().eq("agent_id", agentId));
        model.addAttribute("records",records);
        LogObjectHolder.me().set(agent);
        return PREFIX + "agent_edit.html";
    }
    /**
     * 跳转到修改代理商
     */
    @RequestMapping("/agent_update_account/{agentId}")
    public String agentUpdateAccount(@PathVariable Integer agentId, Model model) {
        Agent agent = agentService.selectById(agentId);
        model.addAttribute("item",agent);


        List<BankUser> bankUsers = bankUserService.selectList(new EntityWrapper<BankUser>().eq("user_id", agent.getUserId()).eq("type", 1));
        for(int i=0; i<bankUsers.size(); i++){
            BankUser bankUser = bankUsers.get(i);
            BankAccount bankAccount = bankAccountService.selectById(bankUser.getBankAccountId());
            bankUser.setUserAccountName(bankAccount.getName());
        }
        model.addAttribute("bankUsers", bankUsers);

        List<AgentDealRecord> records = agentDealRecordService.selectList(new EntityWrapper<AgentDealRecord>().eq("agent_id", agentId));
        model.addAttribute("records",records);
        LogObjectHolder.me().set(agent);
        return PREFIX + "agent_edit_account.html";
    }

    /**
     * 获取代理商列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String agentPhone,String state,String auditState,String permission,String agentName) {
        EntityWrapper<Agent> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(agentPhone)) entityWrapper.like("agent_phone",agentPhone);
        if(StrKit.isNotEmpty(state)) entityWrapper.eq("state",state);
        if(StrKit.isNotEmpty(auditState)) entityWrapper.eq("audit_state",auditState);
        if(StrKit.isNotEmpty(permission)) entityWrapper.eq("permission",permission);
        if(StrKit.isNotEmpty(agentName)) entityWrapper.like("agent_name",agentName);
        entityWrapper.orderBy("id",false);
        Page<Agent> page = agentService.selectPage(new Page<Agent>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增代理商
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增代理商")
    public Object add(Agent agent) {
        agentService.insert(agent);
        return super.SUCCESS_TIP;
    }

    /**
     * 新增信息
     */
    @RequestMapping(value = "/addInfo")
    @ResponseBody
    @BussinessLog(value = "新增代理商")
    public Object addInfo(BankUser bankUser, Integer userId, String agentLogo) {
        bankUser.setUserId(userId);
        bankUser.setType(1);
        bankUser.setCreateTime(new Date());
        bankUser.setQrcode(agentLogo);
        bankUserService.insert(bankUser);
        //agentService.insert(agent);
        return super.SUCCESS_TIP;
    }

    /**
     * 提现
     */
    @RequestMapping(value = "/withdrawal")
    @ResponseBody
    @BussinessLog(value = "申请提现")
    @Transactional
    public Object withdrawal(Long agentId, BigDecimal money, String bankAccountId) {

        Agent agent = agentService.selectById(agentId);
        System.out.println(agent);
        System.out.println(agent.getId());
        AgentWallet agentWallet = agentWalletService.selectOne(new EntityWrapper<AgentWallet>().eq("agent_id", agent.getId()));

        if(agentWallet.getGbAmount().compareTo(money) < 1){
            throw new  RuntimeException("余额不足");
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUserName(agent.getAgentUserName());
        withdrawal.setUserId(agent.getUserId());
        withdrawal.setAccount(bankAccountId);
        withdrawal.setMoney(money);
        withdrawal.setState(0);
        withdrawal.setOrderNum(MakeOrderNum.makeOrderNum("EX"));
        withdrawal.setCreateTime(new Date());
        withdrawal.setType(1);
        withdrawal.setAgentId(agentId);

        agentWallet.setGbAmount(agentWallet.getGbAmount().subtract(money));


        agentWalletService.updateById(agentWallet);
        withdrawalService.insert(withdrawal);

        //agentService.insert(agent);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除代理商
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除代理商")
    @ResponseBody
    public Object delete(@RequestParam Long agentId) {
        agentService.deleteById(agentId);
        return SUCCESS_TIP;
    }

    /**
     * 删除账户信息
     */
    @RequestMapping(value = "/deleteBankUser/{id}")
    @BussinessLog(value = "删除代理商")
    @ResponseBody
    public Object deleteBankUser(@RequestBody @PathVariable Long id) {
        bankUserService.deleteById(id);

        return SUCCESS_TIP;
    }

    /**
     * 修改代理商
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改代理商")
    public Object update(Agent agent) {
        agentService.updateById(agent);
        return super.SUCCESS_TIP;
    }


    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "改变代理商状态")
    @ResponseBody
    public Object changeState(@RequestParam Long agentId,Integer state) {
        Agent agent = agentService.selectById(agentId);
        agent.setState(state);
        agentService.updateById(agent);
        return SUCCESS_TIP;
    }

    @Autowired
    private IAgentDealRecordService agentDealRecordService;

    @RequestMapping(value = "/pass")
    @BussinessLog(value = "代理商通过审核")
    @ResponseBody
    public Object pass(@RequestParam Long agentId) {
        Agent agent = agentService.selectById(agentId);
        agent.setAuditState(1);
        agent.setAuditTime(new Date());
        agentService.updateById(agent);
        //更新用户相关信息
        User user = userService.selectById(agent.getUserId());
        user.setBelongAgent(agent.getId());
        user.setApplyAgent(1);
        userService.updateById(user);
        //创建处理记录
        this.genDealRecord(agentId,ShiroKit.getUser().getName()+"审核通过");
        //TODO 创建代理商后台账号
        return SUCCESS_TIP;

    }

    @RequestMapping(value = "/refuse")
    @BussinessLog(value = "代理商审核驳回")
    @ResponseBody
    public Object refuse(@RequestParam Long agentId,String reason) {
        Agent agent = agentService.selectById(agentId);
        agent.setAuditState(2);
        agent.setReason(reason);
        agent.setAuditTime(new Date());
        agentService.updateById(agent);
        //更新用户相关信息
        User user = userService.selectById(agent.getUserId());
        user.setApplyAgent(2);
        userService.updateById(user);
        //创建处理记录
        this.genDealRecord(agentId,ShiroKit.getUser().getName()+" 审核驳回。  备注:"+reason);
        return SUCCESS_TIP;
    }

    public void genDealRecord(Long agentId,String remark){
        AgentDealRecord agentDealRecord = new AgentDealRecord();
        agentDealRecord.setAgentId(agentId);
        agentDealRecord.setAdminName(ShiroKit.getUser().getName());
        agentDealRecord.setAdminId(ShiroKit.getUser().getId());
        agentDealRecord.setRemark(remark);
        agentDealRecordService.insert(agentDealRecord);
    }

    /**
     * 代理商详情
     */
    @RequestMapping(value = "/detail/{agentId}")
    @ResponseBody
    public Object detail(@PathVariable("agentId") Integer agentId) {
        return agentService.selectById(agentId);
    }
}
