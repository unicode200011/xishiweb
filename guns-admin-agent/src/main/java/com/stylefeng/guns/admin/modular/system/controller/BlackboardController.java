package com.stylefeng.guns.admin.modular.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletRecordService;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveRecordService;
import com.stylefeng.guns.admin.modular.system.dao.IndexDataDao;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.dao.DeptMapper;
import com.stylefeng.guns.persistence.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/blackboard")
public class BlackboardController extends BaseController {

    @Autowired
    IndexDataDao indexDataDao;

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private IAgentWalletRecordService agentWalletRecordService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILiveRecordService liveRecordService;
    /**
     * 跳转到黑板
     */
    @RequestMapping("")
    public String blackboard(Model model) {
        List<String> lastDaysDateStr = DateUtils.getLastDaysDateStr(new Date(), -30);
        String[] strings = lastDaysDateStr.toArray(new String[lastDaysDateStr.size()]);
        String[] reverse = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            reverse[i] = strings[strings.length - i - 1];
        }
        Integer deptId = ShiroKit.getUser().getDeptId();
        Map map = indexDataDao.indexData(deptId);

        System.out.println("map:" + map);

        model.addAttribute("dateStrArr", JSON.toJSONString(reverse));
        model.addAttribute("indexData", map);

        //统计直播总场次
        List<User> users = userService.selectList(new EntityWrapper<User>().eq("belong_agent", deptId));
        List<Long> collect = users.stream().map(User::getId).collect(Collectors.toList());
        int liveRecordNum = liveRecordService.selectCount(new EntityWrapper<LiveRecord>().in("user_id", collect));
        model.addAttribute("liveRecordNum", liveRecordNum);

        //统计收益
        String[] dataArr = new String[reverse.length];
        for (int i = 0; i < reverse.length; i++) {
            String s = reverse[i];
            Wrapper<AgentWalletRecord> wrapper = new EntityWrapper<AgentWalletRecord>().eq("agent_id", deptId);
            wrapper.between("create_time",s+" 00:00:00",s+" 23:59:59");
            List<AgentWalletRecord> agentWalletRecords = agentWalletRecordService.selectList(wrapper);
            BigDecimal reduce = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(agentWalletRecords)){
                reduce = agentWalletRecords.stream().map(AgentWalletRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            dataArr[i] = reduce.toString();
        }


        model.addAttribute("dateArr", JSON.toJSONString(dataArr));
        return "/moneyManage/money_index.html";
    }

}
