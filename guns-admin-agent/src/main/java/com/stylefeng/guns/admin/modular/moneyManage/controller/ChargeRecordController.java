package com.stylefeng.guns.admin.modular.moneyManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.model.UserWalletRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.ChargeRecord;
import com.stylefeng.guns.admin.modular.moneyManage.service.IChargeRecordService;

import java.util.Arrays;

/**
 * 充值记录列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-06 14:34:50
 */
@Controller
@RequestMapping("/chargeRecord")
public class ChargeRecordController extends BaseController {

    private String PREFIX = "/moneyManage/chargeRecord/";

    @Autowired
    private IChargeRecordService chargeRecordService;

    /**
     * 跳转到充值记录列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "chargeRecord.html";
    }

    /**
     * 跳转到添加充值记录列表
     */
    @RequestMapping("/chargeRecord_add")
    public String chargeRecordAdd() {
        return PREFIX + "chargeRecord_add.html";
    }

    /**
     * 跳转到修改充值记录列表
     */
    @RequestMapping("/chargeRecord_update/{chargeRecordId}")
    public String chargeRecordUpdate(@PathVariable Integer chargeRecordId, Model model) {
        ChargeRecord chargeRecord = chargeRecordService.selectById(chargeRecordId);
        model.addAttribute("item",chargeRecord);
        LogObjectHolder.me().set(chargeRecord);
        return PREFIX + "chargeRecord_edit.html";
    }

    /**
     * 获取充值记录列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition,String source,String xishiNum,String name, Long userId) {

        EntityWrapper<ChargeRecord> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(source)) {
            entityWrapper.eq("cr.source",source);
        }
        if(StrKit.isNotEmpty(xishiNum)) entityWrapper.like("u.xishi_num",xishiNum);
        if(StrKit.isNotEmpty(name)) entityWrapper.like("u.name",name);
        if(userId != null) entityWrapper.eq("cr.userId",userId);
        if(StrKit.isNotEmpty(condition)) {
            String[] split = condition.split("[ - ]");
            entityWrapper.between("cr.create_time",split[0]+"00:00:00",split[2]+"23:59:59");
        }
        entityWrapper.orderBy("cr.id",false);
        Page<ChargeRecord> page = chargeRecordService.selectChargeRecordPage(new Page<ChargeRecord>(pageNumber, pageSize), entityWrapper);

        return page;
    }

    /**
     * 新增充值记录列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增充值记录列表")
    public Object add(ChargeRecord chargeRecord) {
        chargeRecordService.insert(chargeRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除充值记录列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除充值记录列表")
    @ResponseBody
    public Object delete(@RequestParam Long chargeRecordId) {
        chargeRecordService.deleteById(chargeRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改充值记录列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改充值记录列表")
    public Object update(ChargeRecord chargeRecord) {
        chargeRecordService.updateById(chargeRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 充值记录列表详情
     */
    @RequestMapping(value = "/detail/{chargeRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("chargeRecordId") Integer chargeRecordId) {
        return chargeRecordService.selectById(chargeRecordId);
    }
}
