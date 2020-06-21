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
import com.stylefeng.guns.persistence.model.BankUser;
import com.stylefeng.guns.admin.modular.moneyManage.service.IBankUserService;

/**
 * 银行账户控制器
 *
 * @author stylefeng
 * @Date 2020-03-02 20:39:57
 */
@Controller
@RequestMapping("/bankUser")
public class BankUserController extends BaseController {

    private String PREFIX = "/moneyManage/bankUser/";

    @Autowired
    private IBankUserService bankUserService;

    /**
     * 跳转到银行账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bankUser.html";
    }

    /**
     * 跳转到添加银行账户
     */
    @RequestMapping("/bankUser_add")
    public String bankUserAdd() {
        return PREFIX + "bankUser_add.html";
    }

    /**
     * 跳转到修改银行账户
     */
    @RequestMapping("/bankUser_update/{bankUserId}")
    public String bankUserUpdate(@PathVariable Integer bankUserId, Model model) {
        BankUser bankUser = bankUserService.selectById(bankUserId);
        model.addAttribute("item",bankUser);
        LogObjectHolder.me().set(bankUser);
        return PREFIX + "bankUser_edit.html";
    }

    /**
     * 获取银行账户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<BankUser> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<BankUser> page = bankUserService.selectPage(new Page<BankUser>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增银行账户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增银行账户")
    public Object add(BankUser bankUser) {
        bankUserService.insert(bankUser);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除银行账户
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除银行账户")
    @ResponseBody
    public Object delete(@RequestParam Long bankUserId) {
        bankUserService.deleteById(bankUserId);
        return SUCCESS_TIP;
    }

    /**
     * 修改银行账户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改银行账户")
    public Object update(BankUser bankUser) {
        bankUserService.updateById(bankUser);
        return super.SUCCESS_TIP;
    }

    /**
     * 银行账户详情
     */
    @RequestMapping(value = "/detail/{bankUserId}")
    @ResponseBody
    public Object detail(@PathVariable("bankUserId") Integer bankUserId) {
        return bankUserService.selectById(bankUserId);
    }
}
