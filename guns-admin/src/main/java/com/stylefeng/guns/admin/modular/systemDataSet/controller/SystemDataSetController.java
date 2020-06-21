package com.stylefeng.guns.admin.modular.systemDataSet.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.modular.systemDataSet.service.ICommonDataService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.SuccessTip;
import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.*;
import com.stylefeng.guns.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/systemDataSet")
public class SystemDataSetController extends BaseController {
    private String PREFIX = "/systemDataSet/commonParams/";
    @Autowired
    private ICommonDataService commonDataService;
    @Autowired
    private RedisService redisService;
    /**
     * 跳转到数据设置页
     */
    @RequestMapping("/dataSet")
    public String dataSet() {
        return PREFIX + "dataSet.html";
    }
    /**
     * 跳转到广告设置页
     */
    @RequestMapping("/orderSet")
    public String adSet(Model model) {
        CommonData overTimeData = commonDataService.selectById(7);
        CommonData sendData = commonDataService.selectById(8);
        CommonData getData = commonDataService.selectById(9);
        model.addAttribute("overTimeData",overTimeData.getValue());
        model.addAttribute("sendData",sendData.getValue());
        model.addAttribute("getData",getData.getValue());
        return PREFIX + "exchangeOrderSet.html";
    }
    /**
     * 跳转到陪伴设置页
     */
    @RequestMapping("/gameSet")
    public String gameSet(Model model) {
        CommonData commonData = commonDataService.selectById(3);
        model.addAttribute("item",commonData);
        return PREFIX + "gameSet.html";
    }


    /**
     * 跳转到编辑数据设置
     */
    @RequestMapping("/dataSet_Edit/{dataSetId}")
    public String dataSetEdit(@PathVariable Integer dataSetId, Model model) {
        CommonData commonParams = commonDataService.selectById(dataSetId);
        model.addAttribute("item", commonParams);
        return PREFIX + "dataSet_add.html";
    }


    /**
     * 获取数据设置列表
     */
    @RequestMapping(value = "/DataSetlist")
    @ResponseBody
    public Object DataSetlist(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                              @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                              String condition, String startTime, String endTime) {
        Page<CommonData> page = new Page<>(pageNumber, pageSize);
        EntityWrapper<CommonData> wrapper = new EntityWrapper<>();
        wrapper.eq("type","sys_data");
        Page<CommonData> commonParamsPage = commonDataService.selectPage(page, wrapper);
        return commonParamsPage;
    }

    /**
     *
     */
    @RequestMapping(value = "/editDataSet")
    @BussinessLog(value = "修改数据设置")
    @ResponseBody
    public Object addDataSet(String value, String description, Integer id) {
        CommonData commonParams = commonDataService.selectById(id);
        commonParams.setValue(value);
        commonParams.setDescription(description);
        commonDataService.updateById(commonParams);
        String cacheKey = SystemConstants.COMMEN_DATA_REDIS_KEY + commonParams.getDataKey();
        redisService.set(cacheKey,commonParams.getValue());
        return super.SUCCESS_TIP;
    }

    @RequestMapping(value = "/editDataSetSm")
    @BussinessLog(value = "修改数据设置")
    @ResponseBody
    public Object editDataSetSm(String text1,String text2,String text3,String text4) {
        CommonData commonData11 = commonDataService.selectById(11);//天书残页
        CommonData commonData12 = commonDataService.selectById(12);//卡片传说说明
        CommonData commonData13 = commonDataService.selectById(13);//百科图鉴说明
        CommonData commonData14 = commonDataService.selectById(14);//指引手册说明
        this.updateData(commonData11,text3);
        this.updateData(commonData12,text2);
        this.updateData(commonData13,text4);
        this.updateData(commonData14,text1);
        return super.SUCCESS_TIP;
    }

    @RequestMapping(value = "/adEditSubmit")
    @BussinessLog(value = "修改广告设置")
    @ResponseBody
    public Object adEditSubmit(String value) {
        CommonData commonParams = commonDataService.selectById(11);
        commonParams.setValue(value);
        commonDataService.updateById(commonParams);
        String cacheKey = SystemConstants.COMMEN_DATA_REDIS_KEY + commonParams.getDataKey();
        redisService.set(cacheKey,commonParams.getValue());
        return super.SUCCESS_TIP;
    }


    @RequestMapping(value = "/doExchangeOrderSet")
    @BussinessLog(value = "修改订单设置")
    @ResponseBody
    public Object adEditSubmit(String overTime,String get,String send) {
        CommonData overTimeData = commonDataService.selectById(7);
        CommonData sendData = commonDataService.selectById(8);
        CommonData getData = commonDataService.selectById(9);
        this.updateData(overTimeData,overTime);
        this.updateData(sendData,send);
        this.updateData(getData,get);
        return super.SUCCESS_TIP;
    }

    public void updateData(CommonData commonData,String value){
        commonData.setValue(value);
        commonDataService.updateById(commonData);
        String cacheKey = SystemConstants.COMMEN_DATA_REDIS_KEY + commonData.getDataKey();
        redisService.set(cacheKey,commonData.getValue());
    }



}
