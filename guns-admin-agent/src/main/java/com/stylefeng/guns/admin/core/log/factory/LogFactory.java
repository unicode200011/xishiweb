package com.stylefeng.guns.admin.core.log.factory;

import com.stylefeng.guns.admin.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.admin.common.constant.state.LogSucceed;
import com.stylefeng.guns.admin.common.constant.state.LogType;
import com.stylefeng.guns.persistence.model.LoginLog;
import com.stylefeng.guns.persistence.model.OperationLog;

import java.util.Date;

/**
 * 日志对象创建工厂
 *
 * @author fengshuonan
 * @date 2016年12月6日 下午9:18:27
 */
public class LogFactory {

    /**
     * 创建操作日志
     *
     * @author fengshuonan
     * @Date 2017/3/30 18:45
     */
    public static OperationLog createOperationLog(LogType logType, Integer adminid, String bussinessName, String clazzName, String methodName, String msg, LogSucceed succeed,String ip) {
        OperationLog operationLog = new OperationLog();
        operationLog.setLogtype(logType.getMessage());
        operationLog.setLogname(bussinessName);
        operationLog.setAdminid(adminid);
        operationLog.setIp(ip);
        operationLog.setAdminname(ConstantFactory.me().getUserNameById(adminid));
        operationLog.setClassname(clazzName);
        operationLog.setMethod(methodName);
        operationLog.setCreateTime(new Date());
        operationLog.setSucceed(succeed.getMessage());
        operationLog.setMessage(msg);
        return operationLog;
    }

    /**
     * 创建登录日志
     *
     * @author fengshuonan
     * @Date 2017/3/30 18:46
     */
    public static LoginLog createLoginLog(LogType logType, Integer userId, String msg, String ip) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLogname(logType.getMessage());
        loginLog.setAdminid(userId);
        loginLog.setCreateTime(new Date());
        loginLog.setSucceed(LogSucceed.SUCCESS.getMessage());
        loginLog.setIp(ip);
        loginLog.setMessage(msg);
        return loginLog;
    }
}
