package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.persistence.model.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 角色表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {

    List<Map> findAllRoles();

    List<Map<String,Object>> selectRoleList(RowBounds rowBounds, @Param("ew") EntityWrapper<Map<String, Object>> wrapper);

    Role findByName(String name);

}