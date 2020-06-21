package com.stylefeng.guns.admin.modular.liveManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
import com.stylefeng.guns.persistence.model.Live;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveService;

import java.util.Date;

/**
 * 直播管理控制器
 *
 * @author stylefeng
 * @Date 2019-12-05 13:49:07
 */
@Controller
@RequestMapping("/live")
public class LiveController extends BaseController {

    private String PREFIX = "/liveManage/live/";

    @Autowired
    private ILiveService liveService;

    /**
     * 跳转到直播管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "live.html";
    }

    /**
     * 跳转到添加直播管理
     */
    @RequestMapping("/live_add")
    public String liveAdd() {
        return PREFIX + "live_add.html";
    }

    /**
     * 跳转到修改直播管理
     */
    @RequestMapping("/live_update/{liveId}")
    public String liveUpdate(@PathVariable Integer liveId, Model model) {
        Live live = liveService.selectById(liveId);
        model.addAttribute("item",live);
        LogObjectHolder.me().set(live);
        return PREFIX + "live_edit.html";
    }
    /**
     * 跳转到修改直播管理
     */
    @RequestMapping("/live_warn/{liveId}")
    public String liveWarn(@PathVariable Integer liveId, Model model) {
        Live live = liveService.selectById(liveId);
        model.addAttribute("item",live);
        LogObjectHolder.me().set(live);
        return PREFIX + "live_warn.html";
    }

    /**
     * 获取直播管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String name,String xishiNum,String roomNum,String liveMode,String showerName) {
        EntityWrapper<Live> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(name)) entityWrapper.like("lr.name",name);
        if(StrKit.isNotEmpty(xishiNum)) entityWrapper.like("u.xishi_num",xishiNum);
        if(StrKit.isNotEmpty(roomNum)) entityWrapper.like("l.room_num",roomNum);
        if(StrKit.isNotEmpty(liveMode)){
            if(Integer.valueOf(liveMode) == 4){//直播结束的
                entityWrapper.eq("l.live_state",0);
            }else {
                entityWrapper.eq("lr.live_mode",liveMode);
                entityWrapper.eq("l.live_state",1);
            }
        }
        if(StrKit.isNotEmpty(showerName)) entityWrapper.like("lr.shower_name",showerName);
        entityWrapper.orderBy("l.id",false);
        Page<Live> page = liveService.selectLivePage(new Page<Live>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增直播管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增直播管理")
    public Object add(Live live) {
        liveService.insert(live);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除直播管理
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除直播管理")
    @ResponseBody
    public Object delete(@RequestParam Long liveId) {
        liveService.deleteById(liveId);
        return SUCCESS_TIP;
    }
    /**
     * 禁播直播
     */
    @RequestMapping(value = "/stop")
    @BussinessLog(value = "禁播直播")
    @ResponseBody
    public Object stop(@RequestParam Long liveId) {
        Live live = liveService.selectById(liveId);
        live.setState(1);
        live.setUpdateTime(new Date());
        liveService.updateById(live);
        return SUCCESS_TIP;
    }
    /**
     * start直播
     */
    @RequestMapping(value = "/start")
    @BussinessLog(value = "启用直播")
    @ResponseBody
    public Object start(@RequestParam Long liveId) {
        Live live = liveService.selectById(liveId);
        live.setState(0);
        live.setUpdateTime(new Date());
        liveService.updateById(live);
        return SUCCESS_TIP;
    }

    /**
     * 修改直播管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改直播管理")
    public Object update(Live live) {
        liveService.updateById(live);
        return super.SUCCESS_TIP;
    }

    /**
     * 直播管理详情
     */
    @RequestMapping(value = "/detail/{liveId}")
    @ResponseBody
    public Object detail(@PathVariable("liveId") Integer liveId) {
        return liveService.selectById(liveId);
    }
}
