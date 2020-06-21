package com.stylefeng.guns.admin.modular.levelManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.persistence.model.LiveGrade;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.UserGrade;
import com.stylefeng.guns.admin.modular.levelManage.service.IUserGradeService;

/**
 * 用户等级控制器
 *
 * @author stylefeng
 * @Date 2019-11-20 15:07:45
 */
@Controller
@RequestMapping("/userGrade")
public class UserGradeController extends BaseController {

    private String PREFIX = "/levelManage/userGrade/";

    @Autowired
    private IUserGradeService userGradeService;

    /**
     * 跳转到用户等级首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userGrade.html";
    }

    /**
     * 跳转到添加用户等级
     */
    @RequestMapping("/userGrade_add")
    public String userGradeAdd() {
        return PREFIX + "userGrade_add.html";
    }

    /**
     * 跳转到修改用户等级
     */
    @RequestMapping("/userGrade_update/{userGradeId}")
    public String userGradeUpdate(@PathVariable Integer userGradeId, Model model) {
        UserGrade userGrade = userGradeService.selectById(userGradeId);
        model.addAttribute("item",userGrade);
        LogObjectHolder.me().set(userGrade);
        return PREFIX + "userGrade_edit.html";
    }

    /**
     * 获取用户等级列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<UserGrade> entityWrapper = new EntityWrapper();
        Page<UserGrade> page = userGradeService.selectPage(new Page<UserGrade>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增用户等级
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增用户等级")
    public Object add(UserGrade userGrade) {
        this.checkLevel(userGrade);
        userGradeService.insert(userGrade);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除用户等级
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除用户等级")
    @ResponseBody
    public Object delete(@RequestParam Integer userGradeId) {

        UserGrade userGrade = userGradeService.selectById(userGradeId);
        int level = userGradeService.selectCount(new EntityWrapper<UserGrade>().gt("level", userGrade.getLevel()));
        if(level > 0){
            throw new GunsException(502,"请不要越级删除,建议从最大等级删除");
        }
        userGradeService.deleteById(userGradeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户等级
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改用户等级")
    public Object update(UserGrade userGrade) {
        UserGrade old = userGradeService.selectById(userGrade);
        Integer oldLevel = old.getLevel();
        Integer level = userGrade.getLevel();
        if(oldLevel != level){
            this.checkLevel(userGrade);
        }
        userGradeService.updateById(userGrade);
        return super.SUCCESS_TIP;
    }

    public void checkLevel(UserGrade userGrade){
        int level = userGradeService.selectCount(new EntityWrapper<UserGrade>().eq("level", userGrade.getLevel()));
        int prelevel = userGradeService.selectCount(new EntityWrapper<UserGrade>().eq("level", userGrade.getLevel()-1));
        if(level > 0){
            throw new GunsException(502,"该等级已存在");
        }
        if(prelevel == 0){
            throw new GunsException(502,"请不要越级添加等级");
        }

        UserGrade preLevel = userGradeService.selectOne(new EntityWrapper<UserGrade>().eq("level", userGrade.getLevel() - 1));
        UserGrade lastLevel = userGradeService.selectOne(new EntityWrapper<UserGrade>().eq("level", userGrade.getLevel() + 1));
        if(userGrade.getAmount().compareTo(preLevel.getAmount()) < 0){
            throw new GunsException(502,"达标金额必须高于小于该等级的金额");
        }
        if(lastLevel != null){
            if(userGrade.getAmount().compareTo(lastLevel.getAmount()) > 0){
                throw new GunsException(502,"达标金额不能高于大于该等级的金额");
            }
        }

    }


    /**
     * 用户等级详情
     */
    @RequestMapping(value = "/detail/{userGradeId}")
    @ResponseBody
    public Object detail(@PathVariable("userGradeId") Integer userGradeId) {
        return userGradeService.selectById(userGradeId);
    }
}
