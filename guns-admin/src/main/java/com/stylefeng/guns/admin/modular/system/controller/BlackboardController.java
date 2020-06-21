package com.stylefeng.guns.admin.modular.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentWalletRecordService;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IChargeRecordService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletRecordService;
import com.stylefeng.guns.admin.modular.system.dao.IndexDataDao;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.DeptMapper;
import com.stylefeng.guns.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/blackboard")
@Slf4j
public class BlackboardController extends BaseController {

    @Autowired
    IndexDataDao indexDataDao;
    @Autowired
    private IAgentWalletRecordService agentWalletRecordService;
    @Autowired
    private IChargeRecordService chargeRecordService;
    @Autowired
    private IUserWalletRecordService userWalletRecordService;
    @Autowired
    private IUserService userService;

    @Autowired
    RedisService redisService;
    @Autowired
    ILiveService liveService;
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

        model.addAttribute("dateStrArr", JSON.toJSONString(reverse));
        model.addAttribute("indexData", map);
        model.addAttribute("userLoginNum", redisService.get("userLoginNum"));
        List<Map> list = liveService.selectHotList();
        model.addAttribute("hotShower", list);
        //统计收益
        String[] chargeDataArr = new String[reverse.length];
        String[] giveDataArr = new String[reverse.length];
        String[] registDataArr = new String[reverse.length];
        String[] visitDataArr = new String[reverse.length];
        for (int i = 0; i < reverse.length; i++) {
            String s = reverse[i];
            //统计充值
            Wrapper<ChargeRecord> wrapper = new EntityWrapper<ChargeRecord>();
            wrapper.between("create_time",s+" 00:00:00",s+" 23:59:59");
            List<ChargeRecord> chargeRecords = chargeRecordService.selectList(wrapper);
            BigDecimal reduce = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(chargeRecords)){
                reduce = chargeRecords.stream().map(ChargeRecord::getRmbAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            chargeDataArr[i] = reduce.toString();

            //统计消耗
            Wrapper<UserWalletRecord> userWalletRecordWrapper = new EntityWrapper<UserWalletRecord>();
            userWalletRecordWrapper.eq("use_type",1);
            userWalletRecordWrapper.between("create_time",s+" 00:00:00",s+" 23:59:59");
            List<UserWalletRecord> userWalletRecords = userWalletRecordService.selectList(userWalletRecordWrapper);
            BigDecimal UserWalletRecordReduce = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(userWalletRecords)){
                UserWalletRecordReduce = userWalletRecords.stream().map(UserWalletRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            giveDataArr[i] = UserWalletRecordReduce.toString();

            //统计注册量
            Wrapper<User> registWapper = new EntityWrapper<User>();
            registWapper.between("create_time",s+" 00:00:00",s+" 23:59:59");
            int i1 = userService.selectCount(registWapper);
            registDataArr[i] =i1 +"";

            //统计访问
            Object o = redisService.get(s + "userLoginNum");
            visitDataArr[i] = o == null?"0":o.toString();

            log.info("remove:{}", o);

        }

        model.addAttribute("chargeDataArr", JSON.toJSONString(chargeDataArr));
        model.addAttribute("giveDataArr", JSON.toJSONString(giveDataArr));
        model.addAttribute("registDataArr", JSON.toJSONString(registDataArr));
        model.addAttribute("visitDataArr", JSON.toJSONString(visitDataArr));

        return "/blackboard.html";
    }

}
