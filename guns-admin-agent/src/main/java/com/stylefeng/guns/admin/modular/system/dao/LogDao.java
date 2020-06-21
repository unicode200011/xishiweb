package com.stylefeng.guns.admin.modular.system.dao;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 日志记录dao
 *
 * @author stylefeng
 * @Date 2017/4/16 23:44
 */
public interface LogDao {

    /**
     * 获取操作日志
     *
     * @author stylefeng
     * @Date 2017/4/16 23:48
     */
    List<Map<String, Object>> getOperationLogs(RowBounds rowBounds, @Param("ew") EntityWrapper<Map<String,Object>> wrapper, @Param("orderByField") String orderByField, @Param("isAsc") String isAsc);

    /**
     * 获取登录日志
     *
     * @author stylefeng
     * @Date 2017/4/16 23:48
     */
    List<Map<String, Object>> getLoginLogs(RowBounds rowBounds,@Param("ew") EntityWrapper<Map<String,Object>> wrapper, @Param("orderByField") String orderByField, @Param("isAsc") String isAsc);
}
