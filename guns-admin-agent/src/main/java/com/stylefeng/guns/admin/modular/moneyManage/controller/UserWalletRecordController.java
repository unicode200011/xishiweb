package com.stylefeng.guns.admin.modular.moneyManage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.core.support.StrKit;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.UserWalletRecord;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletRecordService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 消费记录列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-06 14:35:12
 */
@Controller
@RequestMapping("/userWalletRecord")
public class UserWalletRecordController extends BaseController {

    private String PREFIX = "/moneyManage/userWalletRecord/";

    @Autowired
    private IUserWalletRecordService userWalletRecordService;

    /**
     * 跳转到消费记录列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userWalletRecord.html";
    }
    /**
     * 跳转到财务管理首页
     */
    @RequestMapping("/index")
    public String index_money(Model model) {
        List<String> lastDaysDateStr = DateUtils.getLastDaysDateStr(new Date(), -30);
        String[] strings = lastDaysDateStr.toArray(new String[lastDaysDateStr.size()]);
        model.addAttribute("dateStrArr", JSON.toJSONString(strings));
        return "/moneyManage/money_index.html";
    }

    /**
     * 跳转到添加消费记录列表
     */
    @RequestMapping("/userWalletRecord_add")
    public String userWalletRecordAdd() {
        return PREFIX + "userWalletRecord_add.html";
    }

    /**
     * 跳转到修改消费记录列表
     */
    @RequestMapping("/userWalletRecord_update/{userWalletRecordId}")
    public String userWalletRecordUpdate(@PathVariable Integer userWalletRecordId, Model model) {
        UserWalletRecord userWalletRecord = userWalletRecordService.selectById(userWalletRecordId);
        model.addAttribute("item",userWalletRecord);
        LogObjectHolder.me().set(userWalletRecord);
        return PREFIX + "userWalletRecord_edit.html";
    }

    /**
     * 获取消费记录列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition,String source,String xishiNum,String name) {
        EntityWrapper<UserWalletRecord> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(source)) {
            if(source.equals("8")){
                entityWrapper.in("uw.type", Arrays.asList(3,4));
            }else {
                entityWrapper.eq("uw.type",source);
            }
        }
        if(StrKit.isNotEmpty(xishiNum)) entityWrapper.like("u.xishi_num",xishiNum);
        if(StrKit.isNotEmpty(name)) entityWrapper.like("u.name",name);
        if(StrKit.isNotEmpty(condition)) {
            String[] split = condition.split("[ - ]");
            entityWrapper.between("uw.create_time",split[0]+"00:00:00",split[2]+"23:59:59");
        }
        entityWrapper.eq("uw.use_type",0);
        entityWrapper.orderBy("uw.id",false);
        Page<UserWalletRecord> page = userWalletRecordService.selectUserWalletRecordPage(new Page<UserWalletRecord>(pageNumber, pageSize), entityWrapper);
        System.out.println(page.getRecords());

        return page;
    }

    /**
     * 获取消费记录列表列表
     */
    @RequestMapping(value = "/showerList")
    @ResponseBody
    public Object showerList(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       Long userId, String startTime, String endTime) {


        System.out.println("userId:" + userId);

        Page<UserWalletRecord> page = userWalletRecordService.selectInfos(new Page<>(pageNumber, pageSize), userId, startTime, endTime);

        return page;
    }

    /**
     * 新增消费记录列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增消费记录列表")
    public Object add(UserWalletRecord userWalletRecord) {
        userWalletRecordService.insert(userWalletRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除消费记录列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除消费记录列表")
    @ResponseBody
    public Object delete(@RequestParam Long userWalletRecordId) {
        userWalletRecordService.deleteById(userWalletRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改消费记录列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改消费记录列表")
    public Object update(UserWalletRecord userWalletRecord) {
        userWalletRecordService.updateById(userWalletRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 消费记录列表详情
     */
    @RequestMapping(value = "/detail/{userWalletRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("userWalletRecordId") Integer userWalletRecordId) {
        return userWalletRecordService.selectById(userWalletRecordId);
    }
}
