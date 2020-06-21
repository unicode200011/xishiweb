package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.persistence.model.AgentWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代理商钱包控制器
 *
 * @author stylefeng
 * @Date 2019-12-30 10:37:56
 */
@Controller
@RequestMapping("/agentWallet")
public class AgentWalletController extends BaseController {

    private String PREFIX = "/agentManage/agentWallet/";

    @Autowired
    private IAgentWalletService agentWalletService;

    /**
     * 跳转到代理商钱包首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "agentWallet.html";
    }

    /**
     * 跳转到添加代理商钱包
     */
    @RequestMapping("/agentWallet_add")
    public String agentWalletAdd() {
        return PREFIX + "agentWallet_add.html";
    }

    /**
     * 跳转到修改代理商钱包
     */
    @RequestMapping("/agentWallet_update/{agentWalletId}")
    public String agentWalletUpdate(@PathVariable Integer agentWalletId, Model model) {
        AgentWallet agentWallet = agentWalletService.selectById(agentWalletId);
        model.addAttribute("item",agentWallet);
        LogObjectHolder.me().set(agentWallet);
        return PREFIX + "agentWallet_edit.html";
    }

    /**
     * 获取代理商钱包列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<AgentWallet> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<AgentWallet> page = agentWalletService.selectPage(new Page<AgentWallet>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增代理商钱包
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增代理商钱包")
    public Object add(AgentWallet agentWallet) {
        agentWalletService.insert(agentWallet);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除代理商钱包
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除代理商钱包")
    @ResponseBody
    public Object delete(@RequestParam Long agentWalletId) {
        agentWalletService.deleteById(agentWalletId);
        return SUCCESS_TIP;
    }

    /**
     * 修改代理商钱包
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改代理商钱包")
    public Object update(AgentWallet agentWallet) {
        agentWalletService.updateById(agentWallet);
        return super.SUCCESS_TIP;
    }

    /**
     * 代理商钱包详情
     */
    @RequestMapping(value = "/detail/{agentWalletId}")
    @ResponseBody
    public Object detail(@PathVariable("agentWalletId") Integer agentWalletId) {
        return agentWalletService.selectById(agentWalletId);
    }
}
