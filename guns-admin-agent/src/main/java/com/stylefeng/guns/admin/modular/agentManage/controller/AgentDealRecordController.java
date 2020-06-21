package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.AgentDealRecord;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentDealRecordService;

/**
 * 代理商处理记录控制器
 *
 * @author stylefeng
 * @Date 2019-11-26 16:08:24
 */
@Controller
@RequestMapping("/agentDealRecord")
public class AgentDealRecordController extends BaseController {

    private String PREFIX = "/agentManage/agentDealRecord/";

    @Autowired
    private IAgentDealRecordService agentDealRecordService;

    /**
     * 跳转到代理商处理记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "agentDealRecord.html";
    }

    /**
     * 跳转到添加代理商处理记录
     */
    @RequestMapping("/agentDealRecord_add")
    public String agentDealRecordAdd() {
        return PREFIX + "agentDealRecord_add.html";
    }

    /**
     * 跳转到修改代理商处理记录
     */
    @RequestMapping("/agentDealRecord_update/{agentDealRecordId}")
    public String agentDealRecordUpdate(@PathVariable Integer agentDealRecordId, Model model) {
        AgentDealRecord agentDealRecord = agentDealRecordService.selectById(agentDealRecordId);
        model.addAttribute("item",agentDealRecord);
        LogObjectHolder.me().set(agentDealRecord);
        return PREFIX + "agentDealRecord_edit.html";
    }

    /**
     * 获取代理商处理记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<AgentDealRecord> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<AgentDealRecord> page = agentDealRecordService.selectPage(new Page<AgentDealRecord>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增代理商处理记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增代理商处理记录")
    public Object add(AgentDealRecord agentDealRecord) {
        agentDealRecordService.insert(agentDealRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除代理商处理记录
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除代理商处理记录")
    @ResponseBody
    public Object delete(@RequestParam Long agentDealRecordId) {
        agentDealRecordService.deleteById(agentDealRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改代理商处理记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改代理商处理记录")
    public Object update(AgentDealRecord agentDealRecord) {
        agentDealRecordService.updateById(agentDealRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 代理商处理记录详情
     */
    @RequestMapping(value = "/detail/{agentDealRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("agentDealRecordId") Integer agentDealRecordId) {
        return agentDealRecordService.selectById(agentDealRecordId);
    }
}
