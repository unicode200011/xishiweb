package com.stylefeng.guns.admin.modular.system.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.persistence.model.Admin;

import java.util.Map;

/**
 * <p>
 * 管理员 服务类
 * </p>
 *
 * @author lyz
 * @since 2018-07-03
 */
public interface AdminService extends IService<Admin> {

    Page<Map<String,Object>> selectAdmins(Page<Map<String, Object>> page, EntityWrapper<Map<String, Object>> wrapper,Map map);

    Page<Map<String,Object>> findVideoAuditStaList(Page<Map<String, Object>> page, EntityWrapper<Map<String, Object>> wrapper,Map map);

}
