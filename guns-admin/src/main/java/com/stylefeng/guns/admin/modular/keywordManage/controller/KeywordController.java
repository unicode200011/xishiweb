package com.stylefeng.guns.admin.modular.keywordManage.controller;

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
import com.stylefeng.guns.persistence.model.Keyword;
import com.stylefeng.guns.admin.modular.keywordManage.service.IKeywordService;

/**
 * 敏感词控制器
 *
 * @author stylefeng
 * @Date 2019-12-17 14:58:06
 */
@Controller
@RequestMapping("/keyword")
public class KeywordController extends BaseController {

    private String PREFIX = "/keywordManage/keyword/";

    @Autowired
    private IKeywordService keywordService;

    /**
     * 跳转到敏感词首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "keyword.html";
    }

    /**
     * 跳转到添加敏感词
     */
    @RequestMapping("/keyword_add")
    public String keywordAdd() {
        return PREFIX + "keyword_add.html";
    }

    /**
     * 跳转到修改敏感词
     */
    @RequestMapping("/keyword_update/{keywordId}")
    public String keywordUpdate(@PathVariable Integer keywordId, Model model) {
        Keyword keyword = keywordService.selectById(keywordId);
        model.addAttribute("item",keyword);
        LogObjectHolder.me().set(keyword);
        return PREFIX + "keyword_edit.html";
    }

    /**
     * 获取敏感词列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<Keyword> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        Page<Keyword> page = keywordService.selectPage(new Page<Keyword>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增敏感词
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增敏感词")
    public Object add(Keyword keyword) {
        keywordService.insert(keyword);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除敏感词
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除敏感词")
    @ResponseBody
    public Object delete(@RequestParam Integer keywordId) {
        keywordService.deleteById(keywordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改敏感词
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改敏感词")
    public Object update(Keyword keyword) {
        keywordService.updateById(keyword);
        return super.SUCCESS_TIP;
    }

    /**
     * 敏感词详情
     */
    @RequestMapping(value = "/detail/{keywordId}")
    @ResponseBody
    public Object detail(@PathVariable("keywordId") Integer keywordId) {
        return keywordService.selectById(keywordId);
    }
}
