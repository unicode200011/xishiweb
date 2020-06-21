package com.stylefeng.guns.admin.modular.moneyManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.core.shiro.ShiroUser;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.persistence.model.UserWallet;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.Withdrawal;
import com.stylefeng.guns.admin.modular.moneyManage.service.IWithdrawalService;

import java.math.BigDecimal;

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
    private IWithdrawalService withdrawalService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserWalletService userWalletService;

    /**
     * 跳转到提现记录列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "withdrawal.html";
    }

    /**
     * 跳转到添加提现记录列表
     */
    @RequestMapping("/withdrawal_add")
    public String withdrawalAdd() {
        return PREFIX + "withdrawal_add.html";
    }

    /**
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
                       Withdrawal withdrawal) {
        Integer id = ShiroKit.getUser().getDeptId();

        withdrawal.setAgentId(id.longValue());
        Page page = withdrawalService.selectWithdrawal(withdrawal, new Page(pageNumber, pageSize));

        return page;
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
     * 审核
     * @param withdrawalId
     * @param state
     * @return
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
            UserWallet userWallet = userWalletService.selectByUserId(withdrawal.getUserId());
            BigDecimal giftAmount = userWallet.getGiftAmount();
            BigDecimal add = giftAmount.add(withdrawal.getMoney());
            userWallet.setGiftAmount(add);
            userWalletService.updateById(userWallet);
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
