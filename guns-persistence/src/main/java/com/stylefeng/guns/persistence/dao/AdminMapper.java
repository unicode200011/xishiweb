package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.persistence.model.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 管理员表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    List<Map<String, Object>> selectAdmins(RowBounds rowBounds, @Param("ew") EntityWrapper<Map<String, Object>> wrapper, @Param("map") Map map);

    List<Map<String, Object>> findVideoAuditStaList(RowBounds rowBounds, @Param("ew") EntityWrapper<Map<String, Object>> wrapper, @Param("map") Map map);

}