package com.stylefeng.guns.admin.modular.moneyManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.systemDataSet.service.ICommonDataService;
import com.stylefeng.guns.persistence.model.CommonData;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.ChargeList;
import com.stylefeng.guns.admin.modular.moneyManage.service.IChargeListService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-06 14:34:38
 */
@Controller
@RequestMapping("/chargeList")
public class ChargeListController extends BaseController {

    private String PREFIX = "/moneyManage/chargeList/";

    @Autowired
    private IChargeListService chargeListService;
    @Autowired
    private ICommonDataService commonDataService;
    /**
     * 跳转到充值列表首页
     */
    @RequestMapping("")
    public String index(Model model) {
        CommonData commonData = commonDataService.selectById(14);
        model.addAttribute("rate",commonData.getValue());
        return PREFIX + "chargeList.html";
    }

    /**
     * 跳转到添加充值列表
     */
    @RequestMapping("/chargeList_add")
    public String chargeListAdd() {
        return PREFIX + "chargeList_add.html";
    }

    /**
     * 跳转到修改充值列表
     */
    @RequestMapping("/chargeList_update/{chargeListId}")
    public String chargeListUpdate(@PathVariable Integer chargeListId, Model model) {
        ChargeList chargeList = chargeListService.selectById(chargeListId);
        model.addAttribute("item",chargeList);
        LogObjectHolder.me().set(chargeList);
        return PREFIX + "chargeList_edit.html";
    }

    /**
     * 获取充值列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<ChargeList> entityWrapper = new EntityWrapper();
        Page<ChargeList> page = chargeListService.selectPage(new Page<ChargeList>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增充值列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增充值列表")
    public Object add(ChargeList chargeList) {
        chargeListService.insert(chargeList);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除充值列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除充值列表")
    @ResponseBody
    public Object delete(@RequestParam Long chargeListId) {
        chargeListService.deleteById(chargeListId);
        return SUCCESS_TIP;
    }

    /**
     * 修改充值列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改充值列表")
    public Object update(ChargeList chargeList) {
        chargeListService.updateById(chargeList);
        return super.SUCCESS_TIP;
    }
    /**
     * 修改汇率
     */
    @RequestMapping(value = "/updateRate")
    @ResponseBody
    @BussinessLog(value = "修改汇率")
    public Object update(String rate) {
        CommonData commonData = commonDataService.selectById(14);
        commonData.setValue(rate);
        commonDataService.updateById(commonData);

        List<ChargeList> chargeLists = chargeListService.selectList(new EntityWrapper<>());
        for(int i=0; i<chargeLists.size(); i++){
            ChargeList chargeList = chargeLists.get(i);
            BigDecimal bigDecimal = new BigDecimal(rate);
            chargeList.setRate(bigDecimal);
            BigDecimal rmb = bigDecimal.multiply(chargeList.getRmb());
            chargeList.setXishiNum(rmb.intValue());

            chargeListService.updateById(chargeList);
        }

        return super.SUCCESS_TIP;
    }

    /**
     * 充值列表详情
     */
    @RequestMapping(value = "/detail/{chargeListId}")
    @ResponseBody
    public Object detail(@PathVariable("chargeListId") Integer chargeListId) {
        return chargeListService.selectById(chargeListId);
    }
}
