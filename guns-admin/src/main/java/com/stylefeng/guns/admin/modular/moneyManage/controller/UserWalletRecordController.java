package com.stylefeng.guns.admin.modular.moneyManage.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.admin.core.util.ExportExcelSeedBack;
import com.stylefeng.guns.admin.modular.moneyManage.service.IChargeRecordService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IWithdrawalService;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletRecordService;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 消费记录列表控制器
 *
 * @author stylefeng
 * @Date 2019-12-06 14:35:12
 */
@Controller
@Slf4j
@RequestMapping("/userWalletRecord")
public class UserWalletRecordController extends BaseController {

    private String PREFIX = "/moneyManage/userWalletRecord/";

    @Autowired
    private IUserWalletRecordService userWalletRecordService;
    @Autowired
    private IChargeRecordService chargeRecordService;
    @Autowired
    private IWithdrawalService withdrawalService;
    /**
     * 跳转到消费记录列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userWalletRecord.html";
    }
    /**
     * 跳转到财务管理首页
     */
    @RequestMapping("/index")
    public String index_money(Model model) {
        List<String> lastDaysDateStr = DateUtils.getLastDaysDateStr(new Date(), -30);
        String[] strings = lastDaysDateStr.toArray(new String[lastDaysDateStr.size()]);
        String[] reverse = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            reverse[i] = strings[strings.length - i - 1];
        }
        model.addAttribute("dateStrArr", JSON.toJSONString(reverse));

        Map map = userWalletRecordService.getData();
        log.info("map【{}】", map);

        //统计收益
        String[] dataChargeArr = new String[reverse.length];
        String[] dataChargeArr1 = new String[reverse.length];
        String[] dataArr = new String[reverse.length];


        for (int i = 0; i < reverse.length; i++) {
            String s = reverse[i];
            Wrapper<ChargeRecord> wrapper = new EntityWrapper<ChargeRecord>();
            wrapper.between("create_time",s+" 00:00:00",s+" 23:59:59");
            List<ChargeRecord> chargeRecords = chargeRecordService.selectList(wrapper);
            log.info("chargeRecords【{}】", chargeRecords);
            BigDecimal reduce = BigDecimal.ZERO;

            if(CollectionUtils.isNotEmpty(chargeRecords)){
                reduce = chargeRecords.stream().map(ChargeRecord::getXishiAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            }
            dataChargeArr[i] = reduce.toString() +".00";

            if(CollectionUtils.isNotEmpty(chargeRecords)){
                reduce = chargeRecords.stream().map(ChargeRecord::getXishiAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            }


            Wrapper<Withdrawal> withdrawalWrapper = new EntityWrapper<Withdrawal>();
            withdrawalWrapper.in("state","1,4");
            withdrawalWrapper.between("create_time",s+" 00:00:00",s+" 23:59:59");
            List<Withdrawal> withdrawals = withdrawalService.selectList(withdrawalWrapper);

            BigDecimal with = BigDecimal.ZERO;

            if(CollectionUtils.isNotEmpty(withdrawals)){
                with = withdrawals.stream().map(Withdrawal::getMoney).filter(getMoney -> getMoney!=null).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            dataArr[i] = with.toString();


            String si = "";

            System.out.println(s);
            si = userWalletRecordService.getDayMoney(s+" 23:59:59")+"";

            System.out.println(si);
            dataChargeArr1[i] = si;
            log.info("si【{}】", si);
        }
        System.out.println(dataArr);
        model.addAttribute("dataChargeArr", JSON.toJSONString(dataChargeArr));
        model.addAttribute("dataChargeArr1", JSON.toJSONString(dataChargeArr1));
        model.addAttribute("dataArr", JSON.toJSONString(dataArr));
        System.out.println(map);

        model.addAttribute("data", map);

        return "/moneyManage/money_index.html";
    }

    /**
     * 跳转到添加消费记录列表
     */
    @RequestMapping("/userWalletRecord_add")
    public String userWalletRecordAdd() {
        return PREFIX + "userWalletRecord_add.html";
    }

    /**
     * 跳转到修改消费记录列表
     */
    @RequestMapping("/userWalletRecord_update/{userWalletRecordId}")
    public String userWalletRecordUpdate(@PathVariable Integer userWalletRecordId, Model model) {
        UserWalletRecord userWalletRecord = userWalletRecordService.selectById(userWalletRecordId);
        model.addAttribute("item",userWalletRecord);
        LogObjectHolder.me().set(userWalletRecord);
        return PREFIX + "userWalletRecord_edit.html";
    }

    /**
     * 获取消费记录列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition,String source,String xishiNum,String name,String userId) {
        System.out.println(1);
        EntityWrapper<UserWalletRecord> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(source)) {
            if(source.equals("8")){
                entityWrapper.in("uw.type", Arrays.asList(3,4));
            }else {
                entityWrapper.eq("uw.type",source);
            }
        }
        if(StrKit.isNotEmpty(xishiNum)) entityWrapper.like("u.xishi_num",xishiNum);
        if(StrKit.isNotEmpty(name)) entityWrapper.like("u.name",name);
        //if(StrKit.isNotEmpty(userId)) entityWrapper.like("uw.user_id",userId);
        if(StrKit.isNotEmpty(condition)) {
            String[] split = condition.split("[ - ]");
            entityWrapper.between("uw.create_time",split[0]+"00:00:00",split[2]+"23:59:59");
        }
        entityWrapper.eq("uw.use_type",1);
        entityWrapper.orderBy("uw.id",false);
        Page<UserWalletRecord> page = userWalletRecordService.selectUserWalletRecordPage(new Page<UserWalletRecord>(pageNumber, pageSize), entityWrapper);

        return page;
    }
/**
     * 获取消费记录列表列表
     */
    @RequestMapping(value = "/showerList")
    @ResponseBody
    public Object showerList(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition,String source,String xishiNum,String name,String userId) {
        EntityWrapper<UserWalletRecord> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(source)) {
            if(source.equals("8")){
                entityWrapper.in("uw.type", Arrays.asList(3,4));
            }else {
                entityWrapper.eq("uw.type",source);
            }
        }
        if(StrKit.isNotEmpty(xishiNum)) entityWrapper.like("u.xishi_num",xishiNum);
        if(StrKit.isNotEmpty(name)) entityWrapper.like("u.name",name);
        if(StrKit.isNotEmpty(userId)) entityWrapper.eq("uw.user_id",userId);
        if(StrKit.isNotEmpty(condition)) {
            String[] split = condition.split("[ - ]");
            entityWrapper.between("uw.create_time",split[0]+"00:00:00",split[2]+"23:59:59");
        }
        entityWrapper.eq("uw.use_type",0);
//        entityWrapper.eq("uw.type",2);
        //entityWrapper.eq("uw.type", 2);
        entityWrapper.orderBy("uw.id",false);
        Page<UserWalletRecord> page = userWalletRecordService.selectUserWalletRecordPage(new Page<UserWalletRecord>(pageNumber, pageSize), entityWrapper);
        return page;
    }

    /**
     * 新增消费记录列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增消费记录列表")
    public Object add(UserWalletRecord userWalletRecord) {
        userWalletRecordService.insert(userWalletRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除消费记录列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除消费记录列表")
    @ResponseBody
    public Object delete(@RequestParam Long userWalletRecordId) {
        userWalletRecordService.deleteById(userWalletRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改消费记录列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改消费记录列表")
    public Object update(UserWalletRecord userWalletRecord) {
        userWalletRecordService.updateById(userWalletRecord);
        return super.SUCCESS_TIP;
    }

    /**
     * 消费记录列表详情
     */
    @RequestMapping(value = "/detail/{userWalletRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("userWalletRecordId") Integer userWalletRecordId) {
        return userWalletRecordService.selectById(userWalletRecordId);
    }

    /**
     * 导出
     * @param response
     *
     * @throws ParseException
     */
    @RequestMapping("/getExcel")
    public void getExcel(HttpServletResponse response) {

        EntityWrapper<UserWalletRecord> entityWrapper = new EntityWrapper();

        entityWrapper.eq("uw.use_type",1);

        entityWrapper.orderBy("uw.id",false);
        List<UserWalletRecord> userWalletRecordList = userWalletRecordService.selectUserWalletRecordList(entityWrapper);
        List<UserWalletRecord> userWalletRecords = new ArrayList<>();
        for(int i=0; i<userWalletRecordList.size(); i++){
            UserWalletRecord userWalletRecord = userWalletRecordList.get(i);
            Integer type = userWalletRecord.getType();

            String typeStr = "";

            if(type == 1) typeStr = "打赏";
            if(type == 3 || type == 4) typeStr = "直播间";
            if(type == 5) typeStr = "电影";
            if(type == 6) typeStr = "坐骑";
            if(type == 7) typeStr = "靓号";

            userWalletRecord.setTypeStr(typeStr);

            userWalletRecords.add(userWalletRecord);

        }

        String title = "消费记录.xls";
        String[] headers = new String[]{
                "编号",
                "用户西施号",
                "用户昵称",
                "消费时间",
                "类型",
                "具体事项",
                "数量",
                "西施币",
                "收益对象",
                "所属家族"
        };


        List<Object[]> dataList = new ArrayList<>(userWalletRecords.size());
        Object[] objs;
        int len = headers.length;
        for (UserWalletRecord one : userWalletRecords) {
            System.out.println(one);

            objs = new Object[len];
            objs[0] = one.getId();
            objs[1] = one.getXishiNum();
            objs[2] = one.getUserName();
            objs[3] = DateUtils.formatDateTime(one.getCreateTime());
            objs[4] = one.getTypeStr();
            objs[5] = one.getCustName();
            objs[6] = one.getCustNum();
            objs[7] = one.getAmount();
            objs[8] = one.getLinkUName();
            objs[9] = one.getAgent();
            dataList.add(objs);
        }
        //使用流将数据导出
        OutputStream out = null;
        try {
            //防止中文乱码
            String headStr = "attachment; filename=\"" + new String(title.getBytes("gb2312"), "ISO8859-1") + "\"";
            response.setContentType("octets/stream");
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            out = response.getOutputStream();
            ExportExcelSeedBack ex = new ExportExcelSeedBack(title, headers, dataList);//没有标题
            ex.export(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }


    }
}
