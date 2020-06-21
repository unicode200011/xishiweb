package com.stylefeng.guns.admin.modular.moneyManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.exception.GunsException;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.BankAccount;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankAccountService;

/**
 * 账户分类控制器
 *
 * @author stylefeng
 * @Date 2019-12-09 15:28:12
 */
@Controller
@RequestMapping("/bankAccount")
public class BankAccountController extends BaseController {

    private String PREFIX = "/moneyManage/bankAccount/";

    @Autowired
    private IBankAccountService bankAccountService;

    /**
     * 跳转到账户分类首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bankAccount.html";
    }

    /**
     * 跳转到添加账户分类
     */
    @RequestMapping("/bankAccount_add")
    public String bankAccountAdd() {
        return PREFIX + "bankAccount_add.html";
    }

    /**
     * 跳转到修改账户分类
     */
    @RequestMapping("/bankAccount_update/{bankAccountId}")
    public String bankAccountUpdate(@PathVariable Integer bankAccountId, Model model) {
        BankAccount bankAccount = bankAccountService.selectById(bankAccountId);
        model.addAttribute("item",bankAccount);
        LogObjectHolder.me().set(bankAccount);
        return PREFIX + "bankAccount_edit.html";
    }

    /**
     * 获取账户分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<BankAccount> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<BankAccount> page = bankAccountService.selectPage(new Page<BankAccount>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增账户分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增账户分类")
    public Object add(BankAccount bankAccount) {

        bankAccountService.insert(bankAccount);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除账户分类
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除账户分类")
    @ResponseBody
    public Object delete(@RequestParam Long bankAccountId) {
        bankAccountService.deleteById(bankAccountId);
        return SUCCESS_TIP;
    }

    /**
     * 修改账户分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改账户分类")
    public Object update(BankAccount bankAccount) {
        bankAccountService.updateById(bankAccount);
        return super.SUCCESS_TIP;
    }

    /**
     * 账户分类详情
     */
    @RequestMapping(value = "/detail/{bankAccountId}")
    @ResponseBody
    public Object detail(@PathVariable("bankAccountId") Integer bankAccountId) {
        return bankAccountService.selectById(bankAccountId);
    }
}
