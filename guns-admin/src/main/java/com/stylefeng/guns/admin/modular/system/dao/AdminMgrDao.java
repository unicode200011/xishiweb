package com.stylefeng.guns.admin.modular.system.dao;

import com.stylefeng.guns.admin.core.datascope.DataScope;
import com.stylefeng.guns.persistence.model.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 管理员的dao
 *
 * @author fengshuonan
 * @date 2017年2月12日 下午8:43:52
 */
@Component
public interface AdminMgrDao {

    /**
     * 修改用户状态
     * @date 2017年2月12日 下午8:42:31
     */
    int setStatus(@Param("adminId") Integer adminId, @Param("status") int status);

    /**
     * 修改密码
     *
     * @param adminId
     * @param pwd
     * @date 2017年2月12日 下午8:54:19
     */
    int changePwd(@Param("adminId") Integer adminId, @Param("pwd") String pwd);

    /**
     * 根据条件查询用户列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Map<String, Object>> selectAdmins(RowBounds page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("roleid") String beginTime, @Param("state") String endTime, @Param("deptid") Integer deptid);

    /**
     * 设置用户的角色
     *
     * @return
     * @date 2017年2月13日 下午7:31:30
     */
    int setRoles(@Param("adminId") Integer adminId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     *
     * @param account
     * @return
     * @date 2017年2月17日 下午11:07:46
     */
    Admin getByAccount(@Param("account") String account);

    List<Admin> getByPhone(@Param("phone") String phone);
}
