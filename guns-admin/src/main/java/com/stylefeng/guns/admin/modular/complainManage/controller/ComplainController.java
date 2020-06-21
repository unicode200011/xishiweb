package com.stylefeng.guns.admin.modular.complainManage.controller;

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
import com.stylefeng.guns.persistence.model.Complain;
import com.stylefeng.guns.admin.modular.complainManage.service.IComplainService;

/**
 * 举报列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-17 14:57:32
 */
@Controller
@RequestMapping("/complain")
public class ComplainController extends BaseController {

    private String PREFIX = "/complainManage/complain/";

    @Autowired
    private IComplainService complainService;

    /**
     * 跳转到举报列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "complain.html";
    }

    /**
     * 跳转到添加举报列表
     */
    @RequestMapping("/complain_add")
    public String complainAdd() {
        return PREFIX + "complain_add.html";
    }

    /**
     * 跳转到修改举报列表
     */
    @RequestMapping("/complain_update/{complainId}")
    public String complainUpdate(@PathVariable Integer complainId, Model model) {
        Complain complain = complainService.selectById(complainId);
        model.addAttribute("item",complain);
        LogObjectHolder.me().set(complain);
        return PREFIX + "complain_edit.html";
    }

    /**
     * 获取举报列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<Complain> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<Complain> page = complainService.selectPage(new Page<Complain>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增举报列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增举报列表")
    public Object add(Complain complain) {
        complainService.insert(complain);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除举报列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除举报列表")
    @ResponseBody
    public Object delete(@RequestParam Long complainId) {
        complainService.deleteById(complainId);
        return SUCCESS_TIP;
    }

    /**
     * 修改举报列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改举报列表")
    public Object update(Complain complain) {
        complainService.updateById(complain);
        return super.SUCCESS_TIP;
    }

    /**
     * 举报列表详情
     */
    @RequestMapping(value = "/detail/{complainId}")
    @ResponseBody
    public Object detail(@PathVariable("complainId") Integer complainId) {
        return complainService.selectById(complainId);
    }
}
