package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.persistence.model.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 操作日志 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
@Component
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    List<Map<String, Object>> selectLogList(RowBounds rowBounds, @Param("ew") EntityWrapper<Map<String, Object>> wrapper);

}