package com.stylefeng.guns.admin.modular.suggestionManage.controller;

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
import com.stylefeng.guns.persistence.model.SuggestionType;
import com.stylefeng.guns.admin.modular.suggestionManage.service.ISuggestionTypeService;

/**
 * 用户反馈类型控制器
 *
 * @author stylefeng
 * @Date 2019-12-17 14:44:52
 */
@Controller
@RequestMapping("/suggestionType")
public class SuggestionTypeController extends BaseController {

    private String PREFIX = "/suggestionManage/suggestionType/";

    @Autowired
    private ISuggestionTypeService suggestionTypeService;

    /**
     * 跳转到用户反馈类型首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "suggestionType.html";
    }

    /**
     * 跳转到添加用户反馈类型
     */
    @RequestMapping("/suggestionType_add")
    public String suggestionTypeAdd() {
        return PREFIX + "suggestionType_add.html";
    }

    /**
     * 跳转到修改用户反馈类型
     */
    @RequestMapping("/suggestionType_update/{suggestionTypeId}")
    public String suggestionTypeUpdate(@PathVariable Integer suggestionTypeId, Model model) {
        SuggestionType suggestionType = suggestionTypeService.selectById(suggestionTypeId);
        model.addAttribute("item",suggestionType);
        LogObjectHolder.me().set(suggestionType);
        return PREFIX + "suggestionType_edit.html";
    }

    /**
     * 获取用户反馈类型列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<SuggestionType> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<SuggestionType> page = suggestionTypeService.selectPage(new Page<SuggestionType>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增用户反馈类型
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增用户反馈类型")
    public Object add(SuggestionType suggestionType) {
        suggestionTypeService.insert(suggestionType);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除用户反馈类型
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除用户反馈类型")
    @ResponseBody
    public Object delete(@RequestParam Long suggestionTypeId) {
        suggestionTypeService.deleteById(suggestionTypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户反馈类型
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改用户反馈类型")
    public Object update(SuggestionType suggestionType) {
        suggestionTypeService.updateById(suggestionType);
        return super.SUCCESS_TIP;
    }

    /**
     * 用户反馈类型详情
     */
    @RequestMapping(value = "/detail/{suggestionTypeId}")
    @ResponseBody
    public Object detail(@PathVariable("suggestionTypeId") Integer suggestionTypeId) {
        return suggestionTypeService.selectById(suggestionTypeId);
    }
}
