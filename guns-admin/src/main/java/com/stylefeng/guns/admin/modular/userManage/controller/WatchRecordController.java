package com.stylefeng.guns.admin.modular.userManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveRecordService;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletRecordService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.persistence.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.admin.modular.userManage.service.IWatchRecordService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 观看记录控制器
 *
 * @author stylefeng
 * @Date 2020-02-25 14:47:40
 */
@Controller
@RequestMapping("/watchRecord")
public class WatchRecordController extends BaseController {

    private String PREFIX = "/userManage/watchRecord/";

    @Autowired
    private IWatchRecordService watchRecordService;
    @Autowired
    private ILiveRecordService liveRecordService;
    @Autowired
    private ILiveService liveService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserWalletRecordService userWalletRecordService;
    /**
     * 跳转到观看记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "watchRecord.html";
    }

    /**
     * 跳转到添加观看记录
     */
    @RequestMapping("/watchRecord_add")
    public String watchRecordAdd() {
        return PREFIX + "watchRecord_add.html";
    }

    /**
     * 跳转到修改观看记录
     */
    @RequestMapping("/watchRecord_update/{watchRecordId}")
    public String watchRecordUpdate(@PathVariable Integer watchRecordId, Model model) {
        WatchRecord watchRecord = watchRecordService.selectById(watchRecordId);
        model.addAttribute("item",watchRecord);
        LogObjectHolder.me().set(watchRecord);
        return PREFIX + "watchRecord_edit.html";
    }

    /**
     * 获取观看记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String userId) {
        EntityWrapper<WatchRecord> entityWrapper = new EntityWrapper();
        entityWrapper.eq("user_id",userId);
        entityWrapper.orderBy("id",false);
        Page<WatchRecord> page = watchRecordService.selectPage(new Page<WatchRecord>(pageNumber, pageSize), entityWrapper);
        List<WatchRecord> records = page.getRecords();
        List<Integer> arry = new ArrayList<>();
        arry.add(1);
        arry.add(3);
        arry.add(4);
        for (WatchRecord record : records) {
            Integer liveRecordId = record.getLiveRecordId();
            LiveRecord liveRecord = liveRecordService.selectById(liveRecordId);
            if(liveRecord != null){
                Live live = liveService.selectById(liveRecord.getLiveId());
                User user = userService.selectById(liveRecord.getUserId());
                User watchUser = userService.selectById(record.getUserId());
                record.setRoomNum(live.getRoomNum());
                record.setShowerName(user.getName());
                record.setUserXishiNum(watchUser.getXishiNum());
                record.setShowerXishiNum(user.getXishiNum());
                List<UserWalletRecord> userWalletRecords = userWalletRecordService.selectList(new EntityWrapper<UserWalletRecord>().eq("user_id", record.getUserId()).eq("use_type", 1).in("type", arry).eq("live_record_id",liveRecordId));
                BigDecimal reduce = userWalletRecords.stream().map(UserWalletRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                Integer giftNum = userWalletRecords.stream().filter(x -> x.getType() == 1).mapToInt(UserWalletRecord::getCustNum).sum();
                record.setAmount(reduce);
                record.setGiftNum(giftNum);
            }
        }
        return page;
    }

    /**
     * 新增观看记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增观看记录")
    public Object add(WatchRecord watchRecord) {
        watchRecordService.insert(watchRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除观看记录
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除观看记录")
    @ResponseBody
    public Object delete(@RequestParam Long watchRecordId) {
        watchRecordService.deleteById(watchRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改观看记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改观看记录")
    public Object update(WatchRecord watchRecord) {
        watchRecordService.updateById(watchRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 观看记录详情
     */
    @RequestMapping(value = "/detail/{watchRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("watchRecordId") Integer watchRecordId) {
        return watchRecordService.selectById(watchRecordId);
    }
}
