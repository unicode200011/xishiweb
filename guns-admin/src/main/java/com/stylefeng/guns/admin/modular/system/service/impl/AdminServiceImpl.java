package com.stylefeng.guns.admin.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.admin.modular.system.service.AdminService;
import com.stylefeng.guns.persistence.dao.AdminMapper;
import com.stylefeng.guns.persistence.dao.RoleMapper;
import com.stylefeng.guns.persistence.model.Admin;
import com.stylefeng.guns.persistence.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author lyz
 * @since 2018-07-03
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    RoleMapper roleMapper;

    @Override
    public Page<Map<String, Object>> selectAdmins(Page<Map<String, Object>> page, EntityWrapper<Map<String, Object>> wrapper,Map param) {
        List<Map<String, Object>> list = adminMapper.selectAdmins(page,wrapper,param);
        for(Map<String, Object> map : list){
           Object roleid =map.get("roleid");
           if(roleid != null && !roleid.equals("")){
               String Roleid = roleid.toString();
               String[] ids = Roleid.split("[,]");
               String roleName = "";
               for(int i =0 ;i<ids.length;i++){
                   Integer roleId = Integer.parseInt(ids[i]);
                   Role role = roleMapper.selectById(roleId);
                   if(role != null){
                       roleName += role.getName() + " ";
                       map.put("roleName",roleName);
                   }
               }
           }
        }
        return page.setRecords(list);
    }

    @Override
    public Page<Map<String, Object>> findVideoAuditStaList(Page<Map<String, Object>> page, EntityWrapper<Map<String, Object>> wrapper,Map map) {
        return page.setRecords(adminMapper.findVideoAuditStaList(page,wrapper,map));
    }

}
