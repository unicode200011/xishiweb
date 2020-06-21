package com.stylefeng.guns.admin.modular.giftManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.Gift;
import com.stylefeng.guns.admin.modular.giftManage.service.IGiftService;

import java.util.Date;

/**
 * 礼物控制器
 *
 * @author stylefeng
 * @Date 2019-11-20 11:14:22
 */
@Controller
@RequestMapping("/gift")
public class GiftController extends BaseController {

    private String PREFIX = "/giftManage/gift/";

    @Autowired
    private IGiftService giftService;

    /**
     * 跳转到礼物首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "gift.html";
    }

    /**
     * 跳转到添加礼物
     */
    @RequestMapping("/gift_add")
    public String giftAdd() {
        return PREFIX + "gift_add.html";
    }

    /**
     * 跳转到修改礼物
     */
    @RequestMapping("/gift_update/{giftId}")
    public String giftUpdate(@PathVariable Integer giftId, Model model) {
        Gift gift = giftService.selectById(giftId);
        model.addAttribute("item",gift);
        LogObjectHolder.me().set(gift);
        return PREFIX + "gift_edit.html";
    }

    /**
     * 获取礼物列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition,String state) {
        EntityWrapper<Gift> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(condition)) entityWrapper.like("name",condition);
        if(StrKit.isNotEmpty(state)) entityWrapper.eq("state",state);
        entityWrapper.orderBy("id",false);
        Page<Gift> page = giftService.selectPage(new Page<Gift>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增礼物
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增礼物")
    public Object add(Gift gift) {
        giftService.insert(gift);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除礼物
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除礼物")
    @ResponseBody
    public Object delete(@RequestParam Long giftId) {
        giftService.deleteById(giftId);
        return SUCCESS_TIP;
    }

    /**
     * 修改礼物
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改礼物")
    public Object update(Gift gift) {
        giftService.updateById(gift);
        return super.SUCCESS_TIP;
    }


    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "改变礼物状态")
    @ResponseBody
    public Object changeState(@RequestParam Long giftId,Integer state) {
        Gift gift = giftService.selectById(giftId);
        gift.setState(state);
        giftService.updateById(gift);
        return SUCCESS_TIP;
    }

    /**
     * 礼物详情
     */
    @RequestMapping(value = "/detail/{giftId}")
    @ResponseBody
    public Object detail(@PathVariable("giftId") Integer giftId) {
        return giftService.selectById(giftId);
    }
}
