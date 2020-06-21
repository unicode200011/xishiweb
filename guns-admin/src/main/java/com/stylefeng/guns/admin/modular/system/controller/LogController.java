package com.stylefeng.guns.admin.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlRunner;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.common.annotion.Permission;
import com.stylefeng.guns.admin.common.constant.Const;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.modular.system.dao.LogDao;
import com.stylefeng.guns.admin.modular.system.warpper.LogWarpper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.BeanKit;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.dao.OperationLogMapper;
import com.stylefeng.guns.persistence.model.OperationLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

    private static String PREFIX = "/system/log/";

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private LogDao logDao;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "log.html";
    }

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       @RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName,
                       @RequestParam(required = false) String sortName,
                       @RequestParam(required = false) String sortOrder,
                       @RequestParam(required = false) String admin
    ) {

        Page<Map<String,Object>> page = new Page<>(pageNumber,pageSize);
        EntityWrapper<Map<String,Object>> wrapper = new EntityWrapper<>();
        if(StrKit.isNotEmpty(beginTime) && StrKit.isNotEmpty(endTime)) wrapper.between("create_time",beginTime+" 00:00:00",endTime+" 23:59:59");
        if(StrKit.isNotEmpty(logName)) wrapper.like("logname",logName);
        if(StrKit.isNotEmpty(admin)) {
            wrapper.like("adminname",admin);
        }
//        //获取对应管理员
//        Integer deptId = ShiroKit.getUser().getDeptId();
//        if(deptId != 1){
//            wrapper.eq("adminid", ShiroKit.getUser().getId());
//        }
        wrapper.eq("logtype","业务日志");
        List<Map<String, Object>> result = logDao.getOperationLogs(page, wrapper, sortName, sortOrder);
        new LogWarpper(result).warp();
        page.setRecords(result);
        return page;
    }

    /**
     * 查询操作日志详情
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable Integer id) {
        OperationLog operationLog = operationLogMapper.selectById(id);
        Map<String, Object> stringObjectMap = BeanKit.beanToMap(operationLog);
        return super.warpObject(new LogWarpper(stringObjectMap));
    }

    /**
     * 清空日志
     */
    @BussinessLog(value = "清空业务日志")
    @RequestMapping("/delLog")
    @ResponseBody
    public Object delLog() {
        SqlRunner.db().delete("delete from eb_operation_log");
        return super.SUCCESS_TIP;
    }
}
