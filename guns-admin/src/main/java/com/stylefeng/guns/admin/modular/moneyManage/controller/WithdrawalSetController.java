package com.stylefeng.guns.admin.modular.moneyManage.controller;

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
import com.stylefeng.guns.persistence.model.WithdrawalSet;
import com.stylefeng.guns.admin.modular.moneyManage.service.IWithdrawalSetService;

import java.util.Date;

/**
 * 提现设置控制器
 *
 * @author stylefeng
 * @Date 2020-02-19 16:05:34
 */
@Controller
@RequestMapping("/withdrawalSet")
public class WithdrawalSetController extends BaseController {

    private String PREFIX = "/moneyManage/withdrawalSet/";

    @Autowired
    private IWithdrawalSetService withdrawalSetService;

    /**
     * 跳转到提现设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "withdrawalSet.html";
    }

    /**
     * 跳转到添加提现设置
     */
    @RequestMapping("/withdrawalSet_add")
    public String withdrawalSetAdd() {
        return PREFIX + "withdrawalSet_add.html";
    }

    /**
     * 跳转到修改提现设置
     */
    @RequestMapping("/withdrawalSet_update/{withdrawalSetId}")
    public String withdrawalSetUpdate(@PathVariable Integer withdrawalSetId, Model model) {
        WithdrawalSet withdrawalSet = withdrawalSetService.selectById(withdrawalSetId);
        model.addAttribute("item",withdrawalSet);
        LogObjectHolder.me().set(withdrawalSet);
        return PREFIX + "withdrawalSet_edit.html";
    }

    /**
     * 获取提现设置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<WithdrawalSet> entityWrapper = new EntityWrapper();
        Page<WithdrawalSet> page = withdrawalSetService.selectPage(new Page<WithdrawalSet>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增提现设置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增提现设置")
    public Object add(WithdrawalSet withdrawalSet) {
        withdrawalSetService.insert(withdrawalSet);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除提现设置
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除提现设置")
    @ResponseBody
    public Object delete(@RequestParam Long withdrawalSetId) {
        withdrawalSetService.deleteById(withdrawalSetId);
        return SUCCESS_TIP;
    }

    /**
     * 修改提现设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改提现设置")
    public Object update(WithdrawalSet withdrawalSet) {
        withdrawalSet.setUpdateTime(new Date());
        withdrawalSetService.updateById(withdrawalSet);
        return super.SUCCESS_TIP;
    }

    /**
     * 提现设置详情
     */
    @RequestMapping(value = "/detail/{withdrawalSetId}")
    @ResponseBody
    public Object detail(@PathVariable("withdrawalSetId") Integer withdrawalSetId) {
        return withdrawalSetService.selectById(withdrawalSetId);
    }
}
