package com.stylefeng.guns.admin.modular.adManage.controller;

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
import com.stylefeng.guns.persistence.model.Ad;
import com.stylefeng.guns.admin.modular.adManage.service.IAdService;

import java.util.Date;

/**
 * 广告列表控制器
 *
 * @author stylefeng
 * @Date 2019-10-14 16:34:45
 */
@Controller
@RequestMapping("/ad")
public class AdController extends BaseController {

    private String PREFIX = "/adManage/ad/";

    @Autowired
    private IAdService adService;

    /**
     * 跳转到广告列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "ad.html";
    }

    /**
     * 跳转到添加广告列表
     */
    @RequestMapping("/ad_add")
    public String adAdd() {
        return PREFIX + "ad_add.html";
    }

    /**
     * 跳转到修改广告列表
     */
    @RequestMapping("/ad_update/{adId}")
    public String adUpdate(@PathVariable Integer adId, Model model) {
        Ad ad = adService.selectById(adId);
        model.addAttribute("item",ad);
        LogObjectHolder.me().set(ad);
        return PREFIX + "ad_edit.html";
    }

    /**
     * 获取广告列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<Ad> entityWrapper = new EntityWrapper();
//        entityWrapper.orderBy("id",false);
        Page<Ad> page = adService.selectPage(new Page<Ad>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增广告列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增广告列表")
    public Object add(Ad ad) {
        ad.setUpdateTime(new Date());
        adService.insert(ad);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除广告列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除广告列表")
    @ResponseBody
    public Object delete(@RequestParam Long adId) {
        adService.deleteById(adId);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "改变广告状态")
    @ResponseBody
    public Object changeState(@RequestParam Long adId,Integer state) {
        Ad ad = adService.selectById(adId);
        ad.setState(state);
        ad.setUpdateTime(new Date());
        adService.updateById(ad);
        return SUCCESS_TIP;
    }

    /**
     * 修改广告列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改广告列表")
    public Object update(Ad ad) {
        ad.setUpdateTime(new Date());
        adService.updateById(ad);
        return super.SUCCESS_TIP;
    }

    /**
     * 广告列表详情
     */
    @RequestMapping(value = "/detail/{adId}")
    @ResponseBody
    public Object detail(@PathVariable("adId") Integer adId) {
        return adService.selectById(adId);
    }
}
