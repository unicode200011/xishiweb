package com.stylefeng.guns.admin.modular.system.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 统计数据dao
 *
 */
@Component
public interface IndexDataDao {

    Map indexData(@Param("deptId") Integer agentId);

}
