package com.stylefeng.guns.admin.modular.suggestionManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
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
import com.stylefeng.guns.persistence.model.Suggestion;
import com.stylefeng.guns.admin.modular.suggestionManage.service.ISuggestionService;

import java.util.List;

/**
 * 用户反馈控制器
 *
 * @author stylefeng
 * @Date 2019-12-17 14:44:31
 */
@Controller
@RequestMapping("/suggestion")
public class SuggestionController extends BaseController {

    private String PREFIX = "/suggestionManage/suggestion/";

    @Autowired
    private ISuggestionService suggestionService;
    @Autowired
    private IUserService userService;
    /**
     * 跳转到用户反馈首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "suggestion.html";
    }

    /**
     * 跳转到添加用户反馈
     */
    @RequestMapping("/suggestion_add")
    public String suggestionAdd() {
        return PREFIX + "suggestion_add.html";
    }

    /**
     * 跳转到修改用户反馈
     */
    @RequestMapping("/suggestion_update/{suggestionId}")
    public String suggestionUpdate(@PathVariable Integer suggestionId, Model model) {
        Suggestion suggestion = suggestionService.selectById(suggestionId);
        model.addAttribute("item",suggestion);
        LogObjectHolder.me().set(suggestion);
        return PREFIX + "suggestion_edit.html";
    }

    /**
     * 获取用户反馈列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<Suggestion> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<Suggestion> page = suggestionService.selectPage(new Page<Suggestion>(pageNumber, pageSize), entityWrapper);
        List<Suggestion> records = page.getRecords();
        for (Suggestion record : records) {
            User user = userService.selectById(record.getUserId());
            record.setUserName(user==null?"":user.getName());
        }
        return page;
    }

    /**
     * 新增用户反馈
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增用户反馈")
    public Object add(Suggestion suggestion) {
        suggestionService.insert(suggestion);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除用户反馈
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除用户反馈")
    @ResponseBody
    public Object delete(@RequestParam Long suggestionId) {
        suggestionService.deleteById(suggestionId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户反馈
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改用户反馈")
    public Object update(Suggestion suggestion) {
        suggestionService.updateById(suggestion);
        return super.SUCCESS_TIP;
    }

    /**
     * 用户反馈详情
     */
    @RequestMapping(value = "/detail/{suggestionId}")
    @ResponseBody
    public Object detail(@PathVariable("suggestionId") Integer suggestionId) {
        return suggestionService.selectById(suggestionId);
    }
}
