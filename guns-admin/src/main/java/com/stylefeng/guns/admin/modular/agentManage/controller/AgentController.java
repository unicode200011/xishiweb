package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentDealRecordService;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.persistence.dao.AdminMapper;
import com.stylefeng.guns.persistence.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * 跳转到代理商首页
     */
    @RequestMapping("")
    public String index() {
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
     * 跳转到设置代理商抽成比
     */
    @RequestMapping("/openRate/{id}")
    public String openAdminRate(@PathVariable Long id, Model model) {
        Agent agent = agentService.selectById(id);
        model.addAttribute("agent", agent);
        return PREFIX + "updateAdminRate.html";
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
        List<Agent> records = page.getRecords();
        List<Agent> list = new ArrayList<>();
        for(int i=0; i<records.size(); i++){
            Agent agent = records.get(i);
            AgentWallet agentWallet = agentWalletService.selectOne(new EntityWrapper<AgentWallet>().eq("agent_id", agent.getId()));
            if(agentWallet != null){
                agent.setTotalGiftAmount(agentWallet.getTotalGiftAmount());
            }else{
                agent.setTotalGiftAmount(BigDecimal.ZERO );
            }
            list.add(agent);
        }

        page.setRecords(list);
        return page;
    }

    /**
     * 新增代理商
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增代理商")
    public Object add(Agent agent) {
        String agentPhone = agent.getAgentPhone();
        User user = userService.selectOne(new EntityWrapper<User>().eq("phone", agentPhone));
        if(user == null){
            throw new GunsException(501,"该手机号未注册成为会员");
        }
        if(user.getShower() == 0){
            throw new GunsException(501,"该用户还未申请成为会员");
        }
        int user_id = agentService.selectCount(new EntityWrapper<Agent>().eq("user_id", user.getId()));
        if(user_id > 0){
            throw new GunsException(501,"该手机号会员,已经是另一个家族的打理人");
        }
        Integer account = adminMapper.selectCount(new EntityWrapper<Admin>().eq("account", agent.getAgentPhone()));
        if(account > 0){
            throw new GunsException(502,"手机号已被注册打理人");
        }
        String s = this.genAgentNum();
        agent.setAgentNum(s);
        agent.setUserId(user.getId());
        agent.setBuildType(1);
        agent.setAuditState(0);
        agent.setShowerNum(1);
        agentService.insert(agent);
        //更新相关代理人信息
        User agentUser = userService.selectById(user.getId());
        agentUser.setApplyAgent(3);
        agentUser.setIsCreateAgent(1);
        agentUser.setNewestApplyAgentId(agent.getId());
        userService.updateById(agentUser);
//
//        //TODO 创建代理商后台账号
//        this.insertAdmin(user,agent);
        return super.SUCCESS_TIP;
    }

    //生成家族编号
    public String genAgentNum() {
        String agentNum = ToolUtil.getRandomNum(12);
        Agent agent = agentService.selectOne(new EntityWrapper<Agent>().eq("agent_num",agentNum));
        while (agent != null) {
            agentNum = ToolUtil.getRandomNum(12);
            agent = agentService.selectOne(new EntityWrapper<Agent>().eq("agent_num",agentNum));
        }
        return agentNum;
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
     * 修改代理商
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改代理商")
    public Object update(Agent agent) {
        agentService.updateById(agent);
        return super.SUCCESS_TIP;
    }

    /**
     * 设置抽成比
     */
    @RequestMapping(value = "/updateAdminRate")
    @ResponseBody
    @BussinessLog(value = "设置抽成比")
    public Object updateAdminRate(Agent agent) {
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
        Admin admin = new Admin();
        admin.setDeptid(Integer.valueOf(agentId+""));
        Admin dbAdmin = adminMapper.selectOne(admin);
        if(dbAdmin != null){
            dbAdmin.setStatus(state+1);
            adminMapper.updateById(dbAdmin);
        }
        return SUCCESS_TIP;
    }

    @Autowired
    private IUserService userService;
    @Autowired
    private IAgentDealRecordService agentDealRecordService;
    @Autowired
    private IAgentWalletService agentWalletService;
    @Autowired
    private AdminMapper adminMapper;

    @Transactional
    @RequestMapping(value = "/pass")
    @BussinessLog(value = "代理商通过审核")
    @ResponseBody
    public Object pass(@RequestParam Long agentId) {
        Agent agent = agentService.selectById(agentId);
        //TODO 创建代理商后台账号
        Integer account = adminMapper.selectCount(new EntityWrapper<Admin>().eq("account", agent.getAgentPhone()));
        if(account > 0){
            throw new GunsException(502,"手机号已被注册为打理人");
        }

        agent.setAuditState(1);
        agent.setShowerNum(1);
        agent.setAuditTime(new Date());
        agentService.updateById(agent);
        //创建代理商钱包
        AgentWallet agentWallet = new AgentWallet();
        agentWallet.setAgentId(agentId);
        agentWalletService.insert(agentWallet);
        //更新用户相关信息
        User user = userService.selectById(agent.getUserId());
        user.setBelongAgent(agent.getId());
        user.setApplyAgent(1);
        userService.updateById(user);
        //创建处理记录
        this.genDealRecord(agentId,ShiroKit.getUser().getName()+"审核通过");
        //TODO 创建代理商后台账号
        this.insertAdmin(user,agent);
        return SUCCESS_TIP;

    }
    @RequestMapping(value = "/resetPwd")
    @BussinessLog(value = "代理商重置密码")
    @ResponseBody
    public Object resetPwd(@RequestParam Long agentId) {
        Agent agent = agentService.selectById(agentId);
        //创建处理记录
        this.genDealRecord(agentId,ShiroKit.getUser().getName()+"重置密码成功");
        Admin admin = new Admin();
        admin.setDeptid(Integer.valueOf(agent.getId().toString()));
        System.out.println("admin:" + admin);
        Admin db = adminMapper.selectOne(admin);
        String randomSalt = ShiroKit.getRandomSalt(5);
        System.out.println("db" + db);
        db.setSalt(randomSalt);
        db.setPassword(ShiroKit.md5("123456", db.getSalt()));
        System.out.println(admin);
        adminMapper.updateById(db);
        return SUCCESS_TIP;

    }

    public void insertAdmin(User user,Agent agent){
        //TODO 创建代理商后台账号
        Integer account = adminMapper.selectCount(new EntityWrapper<Admin>().eq("account", user.getPhone()));
        if(account > 0){
            throw new GunsException(502,"手机号已被注册");
        }
        Admin admin = new Admin();
        admin.setRoleid(2+"");
        admin.setAccount(user.getPhone());
        admin.setDeptid(Integer.valueOf(agent.getId().toString()));
        admin.setSalt(ShiroKit.getRandomSalt(5));
        admin.setPassword(ShiroKit.md5("123456", admin.getSalt()));
        admin.setAvatar(user.getAvatar());
        admin.setName(agent.getAgentName());
        admin.setPhone(user.getPhone());
        admin.setStatus(1);
        adminMapper.insert(admin);
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
