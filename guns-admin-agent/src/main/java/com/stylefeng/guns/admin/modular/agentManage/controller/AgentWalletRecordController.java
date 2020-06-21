package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletRecordService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.persistence.model.AgentWalletRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代理商钱包记录控制器
 *
 * @author stylefeng
 * @Date 2019-12-30 10:38:09
 */
@Controller
@RequestMapping("/agentWalletRecord")
public class AgentWalletRecordController extends BaseController {

    private String PREFIX = "/agentManage/agentWalletRecord/";

    @Autowired
    private IAgentWalletRecordService agentWalletRecordService;

    /**
     * 跳转到代理商钱包记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "agentWalletRecord.html";
    }

    /**
     * 跳转到添加代理商钱包记录
     */
    @RequestMapping("/agentWalletRecord_add")
    public String agentWalletRecordAdd() {
        return PREFIX + "agentWalletRecord_add.html";
    }

    /**
     * 跳转到修改代理商钱包记录
     */
    @RequestMapping("/agentWalletRecord_update/{agentWalletRecordId}")
    public String agentWalletRecordUpdate(@PathVariable Integer agentWalletRecordId, Model model) {
        AgentWalletRecord agentWalletRecord = agentWalletRecordService.selectById(agentWalletRecordId);
        model.addAttribute("item",agentWalletRecord);
        LogObjectHolder.me().set(agentWalletRecord);
        return PREFIX + "agentWalletRecord_edit.html";
    }

    /**
     * 获取代理商钱包记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<AgentWalletRecord> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<AgentWalletRecord> page = agentWalletRecordService.selectPage(new Page<AgentWalletRecord>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增代理商钱包记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增代理商钱包记录")
    public Object add(AgentWalletRecord agentWalletRecord) {
        agentWalletRecordService.insert(agentWalletRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除代理商钱包记录
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除代理商钱包记录")
    @ResponseBody
    public Object delete(@RequestParam Long agentWalletRecordId) {
        agentWalletRecordService.deleteById(agentWalletRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改代理商钱包记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改代理商钱包记录")
    public Object update(AgentWalletRecord agentWalletRecord) {
        agentWalletRecordService.updateById(agentWalletRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 代理商钱包记录详情
     */
    @RequestMapping(value = "/detail/{agentWalletRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("agentWalletRecordId") Integer agentWalletRecordId) {
        return agentWalletRecordService.selectById(agentWalletRecordId);
    }
}
