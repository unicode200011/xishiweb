package com.stylefeng.guns.admin.modular.liveManage.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveRecordService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletRecordService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.persistence.model.LiveRecord;
import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.persistence.model.UserWalletRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播记录控制器
 *
 * @author stylefeng
 * @Date 2019-12-24 16:39:41
 */
@Controller
@RequestMapping("/liveRecord")
public class LiveRecordController extends BaseController {

    private String PREFIX = "/liveManage/liveRecord/";

    @Autowired
    private ILiveRecordService liveRecordService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserWalletRecordService userWalletRecordService;
    /**
     * 跳转到直播记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "liveRecord.html";
    }

    /**
     * 跳转到添加直播记录
     */
    @RequestMapping("/liveRecord_add")
    public String liveRecordAdd() {
        return PREFIX + "liveRecord_add.html";
    }

    /**
     * 跳转到修改直播记录
     */
    @RequestMapping("/liveRecord_update/{liveRecordId}")
    public String liveRecordUpdate(@PathVariable Integer liveRecordId, Model model) {
        LiveRecord liveRecord = liveRecordService.selectById(liveRecordId);
        model.addAttribute("item",liveRecord);
        LogObjectHolder.me().set(liveRecord);
        return PREFIX + "liveRecord_edit.html";
    }

    /**
     * 获取直播记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String userId) {
        EntityWrapper<LiveRecord> entityWrapper = new EntityWrapper();
        entityWrapper.eq("user_id",userId);
        entityWrapper.orderBy("id",false);
        Page<LiveRecord> page = liveRecordService.selectPage(new Page<LiveRecord>(pageNumber, pageSize), entityWrapper);
        List<LiveRecord> records = page.getRecords();
        List<Integer> arry = new ArrayList<>();
        arry.add(2);
        arry.add(3);
        arry.add(4);
        for (LiveRecord record : records) {
            User user = userService.selectById(record.getUserId());
            record.setXishiNum(user==null?"":user.getXishiNum());
            List<UserWalletRecord> userWalletRecords = userWalletRecordService.selectList(new EntityWrapper<UserWalletRecord>().eq("live_record_id", record.getId()).in("type", arry).eq("use_type", 0));
            BigDecimal reduce = userWalletRecords.stream().map(UserWalletRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Integer add = userWalletRecords.stream().filter(x -> x.getType() == 2).mapToInt(UserWalletRecord::getCustNum).sum();
            record.setAmount(reduce);
            record.setLiveGiftNum(add);
        }
        return page;
    }

    /**
     * 新增直播记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增直播记录")
    public Object add(LiveRecord liveRecord) {
        liveRecordService.insert(liveRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除直播记录
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除直播记录")
    @ResponseBody
    public Object delete(@RequestParam Long liveRecordId) {
        liveRecordService.deleteById(liveRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改直播记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改直播记录")
    public Object update(LiveRecord liveRecord) {
        liveRecordService.updateById(liveRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 直播记录详情
     */
    @RequestMapping(value = "/detail/{liveRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("liveRecordId") Integer liveRecordId) {
        return liveRecordService.selectById(liveRecordId);
    }
}
