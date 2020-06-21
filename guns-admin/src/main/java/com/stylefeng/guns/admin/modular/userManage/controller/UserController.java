package com.stylefeng.guns.admin.modular.userManage.controller;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.admin.core.util.ExportExcelSeedBack;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletService;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.persistence.model.Agent;
import com.stylefeng.guns.persistence.model.Movie;
import com.stylefeng.guns.persistence.model.UserWallet;
import org.apache.commons.io.IOUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户列表控制器
 *
 * @author stylefeng
 * @Date 2019-10-10 15:20:17
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private String PREFIX = "/userManage/user/";

    @Autowired
    private IUserService userService;
    @Autowired
    private IAgentService agentService;

    @Autowired
    private IUserWalletService userWalletService;
    /**
     * 跳转到用户列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user.html";
    }

    /**
     * 跳转到主播列表首页
     */
    @RequestMapping("/shower")
    public String shower() {
        return PREFIX + "shower.html";
    }

    /**
     * 跳转到添加用户列表
     */
    @RequestMapping("/user_add")
    public String userAdd() {
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到修改用户列表
     */
    @RequestMapping("/user_update/{userId}")
    public String userUpdate(@PathVariable Integer userId, Model model) {
        User user = userService.selectById(userId);
        model.addAttribute("item",user);
        LogObjectHolder.me().set(user);
        return PREFIX + "user_edit.html";
    }
    /**
     * 跳转到修改用户列表
     */
    @RequestMapping("/shower_update/{userId}")
    public String shower_update(@PathVariable Integer userId, Model model) {
        User user = userService.selectById(userId);

        if(user.getApplyAgent() == 1){
            Agent agent = agentService.selectById(user.getApplyAgent());
            model.addAttribute("agentAdminRate",agent.getAdminRate());
            model.addAttribute("agentId",agent.getId());
        }

        model.addAttribute("item",user);
        LogObjectHolder.me().set(user);
        return PREFIX + "shower_edit.html";
    }

    /**
     * 获取用户列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String state,String name,String phone,String xishiNum) {
        EntityWrapper<User> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(state)){
            if(state.equals("3")){
                entityWrapper.eq("state",1);
            }else {
                entityWrapper.eq("online_state",state);
            }
        }
        if(StrKit.isNotEmpty(name))entityWrapper.like("name",name);
        if(StrKit.isNotEmpty(phone))entityWrapper.like("phone",phone);
        if(StrKit.isNotEmpty(xishiNum))entityWrapper.like("xishi_num",xishiNum);
        entityWrapper.orderBy("id",false);
        Page<User> page = userService.selectPage(new Page<User>(pageNumber, pageSize), entityWrapper);
        List<User> records = page.getRecords();
        for (User record : records) {
            UserWallet userWallet = userWalletService.selectOne(new EntityWrapper<UserWallet>().eq("user_id",record.getId()));
            record.setGiveAmount(userWallet==null? BigDecimal.ZERO:userWallet.getGiveAmount());
            record.setGbMoeny(userWallet==null? BigDecimal.ZERO:userWallet.getGbMoeny());
            record.setGiftAmount(userWallet==null? BigDecimal.ZERO:userWallet.getGiftAmount());
        }
        return page;
    }

    /**
     * 获取用户列表列表
     */
    @RequestMapping(value = "/showerList")
    @ResponseBody
    public Object showerList(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String state,String name,String phone,String xishiNum) {
        EntityWrapper<User> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(state))entityWrapper.eq("state",state);
        if(StrKit.isNotEmpty(name))entityWrapper.like("name",name);
        if(StrKit.isNotEmpty(phone))entityWrapper.like("phone",phone);
        if(StrKit.isNotEmpty(xishiNum))entityWrapper.like("xishi_num",xishiNum);
        entityWrapper.eq("shower",1);
        entityWrapper.ne("state", 2);
        entityWrapper.orderBy("id",false);
        Page<User> page = userService.selectPage(new Page<User>(pageNumber, pageSize), entityWrapper);
        List<User> records = page.getRecords();
        for (User record : records) {
            Agent agent = agentService.selectById(record.getBelongAgent());
            record.setAgentName(agent==null?"":agent.getAgentName());
            UserWallet userWallet = userWalletService.selectByUserId(record.getId());

            BigDecimal gbMoeny = userWallet.getGbMoeny();
            BigDecimal nearCharge = userWalletService.selectNearCharge(record.getId());
            BigDecimal giftAmount = userWallet.getGiftAmount();
            record.setGiftAmount(giftAmount);
            record.setGbMoeny(gbMoeny);
            record.setNearCharge(nearCharge);
        }
        return page;
    }

    /**
     * 新增用户列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增用户列表")
    public Object add(User user) {
        userService.insert(user);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除用户列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除用户列表")
    @ResponseBody
    public Object delete(@RequestParam Long userId) {
        userService.deleteById(userId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional
    @BussinessLog(value = "修改用户列表")
    public Object update(User user) {
        System.out.println(user);
        System.out.println("在这");
        userService.updateById(user);
        return super.SUCCESS_TIP;
    }

    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "改变用户状态")
    @ResponseBody
    public Object changeState(@RequestParam Long userId,Integer state) {

        User user = userService.selectById(userId);
        user.setState(state);
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return SUCCESS_TIP;
    }
    /**
     * 用户列表详情
     */
    @RequestMapping(value = "/detail/{userId}")
    @ResponseBody
    public Object detail(@PathVariable("userId") Integer userId) {

        return userService.selectById(userId);
    }


    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, String state,String name,String phone,String xishiNum) {
        String title = "用户列表" + ToolUtil.getNowEpochSecond() + ".xls";
        String[] headers = new String[]{
                "序号",
                "绑定手机号",
                "用户昵称",
                "注册时间",
                "用户等级",
                "ip地址",
        };
        EntityWrapper<User> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(state)){
            if(state.equals("3")){
                entityWrapper.eq("state",1);
            }else {
                entityWrapper.eq("online_state",state);
            }
        }
        if(StrKit.isNotEmpty(name))entityWrapper.like("name",name);
        if(StrKit.isNotEmpty(phone))entityWrapper.like("phone",phone);
        if(StrKit.isNotEmpty(xishiNum))entityWrapper.like("xishi_num",xishiNum);
        entityWrapper.orderBy("id",false);
        List<User> records = userService.selectList(entityWrapper);

        List<Object[]> dataList = new ArrayList<>(records.size());
        Object[] objs;
        int len = headers.length;
        for (User one : records) {
            objs = new Object[len];
            objs[0] = 0;
            objs[1] = one.getPhone();
            objs[2] = one.getName();
            objs[3] = DateUtils.formatDateTime(one.getCreateTime());
            objs[4] = one.getUserLevel();
            objs[5] = one.getIp();
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
