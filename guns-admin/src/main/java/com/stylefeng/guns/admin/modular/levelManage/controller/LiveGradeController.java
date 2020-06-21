package com.stylefeng.guns.admin.modular.levelManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.systemDataSet.service.ICommonDataService;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.persistence.model.UserGrade;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.LiveGrade;
import com.stylefeng.guns.admin.modular.levelManage.service.ILiveGradeService;

/**
 * 直播等级控制器
 *
 * @author stylefeng
 * @Date 2019-11-20 15:08:00
 */
@Controller
@RequestMapping("/liveGrade")
public class LiveGradeController extends BaseController {

   private final String levelone="http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/grade_image/4cd3d8ee00fa4a4599b3b88e9c6cf076.png";
   private final String levelfive="http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/grade_image/55572731c0f44260b0983072e4f54e20.png";
   private final String levelten="http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/grade_image/511713c410c840e1955ff23282e714f2.png";

    private String PREFIX = "/levelManage/liveGrade/";

    @Autowired
    private ILiveGradeService liveGradeService;
    /**
     * 跳转到直播等级首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "liveGrade.html";
    }

    /**
     * 跳转到添加直播等级
     */
    @RequestMapping("/liveGrade_add")
    public String liveGradeAdd() {
        return PREFIX + "liveGrade_add.html";
    }

    /**
     * 跳转到修改直播等级
     */
    @RequestMapping("/liveGrade_update/{liveGradeId}")
    public String liveGradeUpdate(@PathVariable Integer liveGradeId, Model model) {
        LiveGrade liveGrade = liveGradeService.selectById(liveGradeId);
        model.addAttribute("item",liveGrade);
        model.addAttribute("levelone",levelone);
        model.addAttribute("levelfive",levelfive);
        model.addAttribute("levelten",levelten);
        LogObjectHolder.me().set(liveGrade);
        return PREFIX + "liveGrade_edit.html";
    }

    /**
     * 获取直播等级列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<LiveGrade> entityWrapper = new EntityWrapper();
        Page<LiveGrade> page = liveGradeService.selectPage(new Page<LiveGrade>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增直播等级
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增直播等级")
    public Object add(LiveGrade liveGrade) {
       this.checkLevel(liveGrade);

        liveGradeService.insert(liveGrade);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除直播等级
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除直播等级")
    @ResponseBody
    public Object delete(@RequestParam Integer liveGradeId) {
        LiveGrade liveGrade = liveGradeService.selectById(liveGradeId);
        int level = liveGradeService.selectCount(new EntityWrapper<LiveGrade>().gt("level", liveGrade.getLevel()));
        if(level > 0){
            throw new GunsException(502,"请不要越级删除,建议从最大等级删除");
        }
        liveGradeService.deleteById(liveGradeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改直播等级
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改直播等级")
    public Object update(LiveGrade liveGrade) {
        LiveGrade old = liveGradeService.selectById(liveGrade.getId());
        Integer oldLevel = old.getLevel();
        Integer level = liveGrade.getLevel();
        if(oldLevel != level){
            this.checkLevel(liveGrade);
        }
        liveGradeService.updateById(liveGrade);
        return super.SUCCESS_TIP;
    }

    /**
     * 直播等级详情
     */
    @RequestMapping(value = "/detail/{liveGradeId}")
    @ResponseBody
    public Object detail(@PathVariable("liveGradeId") Integer liveGradeId) {
        return liveGradeService.selectById(liveGradeId);
    }

    public void checkLevel(LiveGrade liveGrade){
        int level = liveGradeService.selectCount(new EntityWrapper<LiveGrade>().eq("level", liveGrade.getLevel()));
        int prelevel = liveGradeService.selectCount(new EntityWrapper<LiveGrade>().eq("level", liveGrade.getLevel()-1));
        if(level > 0){
            throw new GunsException(502,"该等级已存在");
        }
        if(prelevel == 0){
            throw new GunsException(502,"请不要越级添加等级");
        }
        LiveGrade preLevel = liveGradeService.selectOne(new EntityWrapper<LiveGrade>().eq("level", liveGrade.getLevel() - 1));
        LiveGrade lastLevel = liveGradeService.selectOne(new EntityWrapper<LiveGrade>().eq("level", liveGrade.getLevel() + 1));
        if(liveGrade.getAmount().compareTo(preLevel.getAmount()) < 0){
            throw new GunsException(502,"达标金额必须高于小于该等级的金额");
        }
        if(lastLevel != null){
            if(liveGrade.getAmount().compareTo(lastLevel.getAmount()) > 0){
                throw new GunsException(502,"达标金额不能高于大于该等级的金额");
            }
        }
    }
}
