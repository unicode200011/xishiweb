package com.stylefeng.guns.admin.modular.system.dao;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 通知dao
 *
 * @author fengshuonan
 * @date 2017-05-09 23:03
 */
public interface NoticeDao {

    List<Map<String, Object>> list(RowBounds rowBounds, @Param("ew")EntityWrapper<Map<String,Object>> wrapper);
}
