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
import com.stylefeng.guns.persistence.model.ComplainType;
import com.stylefeng.guns.admin.modular.complainManage.service.IComplainTypeService;

/**
 * 举报类型列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-17 14:57:44
 */
@Controller
@RequestMapping("/complainType")
public class ComplainTypeController extends BaseController {

    private String PREFIX = "/complainManage/complainType/";

    @Autowired
    private IComplainTypeService complainTypeService;

    /**
     * 跳转到举报类型列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "complainType.html";
    }

    /**
     * 跳转到添加举报类型列表
     */
    @RequestMapping("/complainType_add")
    public String complainTypeAdd() {
        return PREFIX + "complainType_add.html";
    }

    /**
     * 跳转到修改举报类型列表
     */
    @RequestMapping("/complainType_update/{complainTypeId}")
    public String complainTypeUpdate(@PathVariable Integer complainTypeId, Model model) {
        ComplainType complainType = complainTypeService.selectById(complainTypeId);
        model.addAttribute("item",complainType);
        LogObjectHolder.me().set(complainType);
        return PREFIX + "complainType_edit.html";
    }

    /**
     * 获取举报类型列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<ComplainType> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<ComplainType> page = complainTypeService.selectPage(new Page<ComplainType>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增举报类型列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增举报类型列表")
    public Object add(ComplainType complainType) {
        complainTypeService.insert(complainType);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除举报类型列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除举报类型列表")
    @ResponseBody
    public Object delete(@RequestParam Long complainTypeId) {
        complainTypeService.deleteById(complainTypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改举报类型列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改举报类型列表")
    public Object update(ComplainType complainType) {
        complainTypeService.updateById(complainType);
        return super.SUCCESS_TIP;
    }

    /**
     * 举报类型列表详情
     */
    @RequestMapping(value = "/detail/{complainTypeId}")
    @ResponseBody
    public Object detail(@PathVariable("complainTypeId") Integer complainTypeId) {
        return complainTypeService.selectById(complainTypeId);
    }
}
