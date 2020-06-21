package com.stylefeng.guns.admin.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.common.constant.cache.Cache;
import com.stylefeng.guns.admin.common.constant.dictmap.RoleDict;
import com.stylefeng.guns.admin.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.admin.common.exception.BizExceptionEnum;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.modular.system.dao.RoleDao;
import com.stylefeng.guns.admin.modular.system.service.RoleService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.persistence.dao.AdminMapper;
import com.stylefeng.guns.persistence.dao.RoleMapper;
import com.stylefeng.guns.persistence.model.Admin;
import com.stylefeng.guns.persistence.model.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 *
 * @author fengshuonan
 * @Date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static String PREFIX = "/system/role";

    @Resource
    AdminMapper adminMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    RoleDao roleDao;

    @Resource
    RoleService roleService;

    /**
     * 跳转到角色列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/role.html";
    }

    /**
     * 跳转到添加角色
     */
    @RequestMapping(value = "/role_add")
    public String roleAdd() {
        return PREFIX + "/role_add.html";
    }

    /**
     * 跳转到修改角色
     */
    @RequestMapping(value = "/role_edit/{roleId}")
    public String roleEdit(@PathVariable Integer roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Role role = this.roleMapper.selectById(roleId);
        model.addAttribute(role);
        LogObjectHolder.me().set(role);
        return PREFIX + "/role_edit.html";
    }

    /**
     * 跳转到角色分配
     */
    @RequestMapping(value = "/role_assign/{roleId}")
    public String roleAssign(@PathVariable("roleId") Integer roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("roleId", roleId);
        model.addAttribute("roleName", ConstantFactory.me().getSingleRoleName(roleId));
        return PREFIX + "/role_assign.html";
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       @RequestParam(required = false) String roleName) {
        Page<Map<String,Object>> page = new Page<>(pageNumber, pageSize);
        EntityWrapper<Map<String,Object>> wrapper = new EntityWrapper<>();
        if(StrKit.isNotEmpty(roleName)){
            wrapper.like("name",roleName);
        }
        Integer deptId = ShiroKit.getUser().getDeptId();
        wrapper.eq("deptid",deptId);
//        if(deptId.compareTo(1) != 0){
//        }
        wrapper.gt("id",1);
        List<Map<String, Object>> roles = this.roleDao.selectRoles(page,wrapper);
        return page.setRecords(roles);
    }

    /**
     * 角色新增
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加角色")
    @ResponseBody
    public Tip add(Role role) {
        int i = role.selectCount(new EntityWrapper().eq("name", role.getName()).eq("deptid", ShiroKit.getUser().getDeptId()));
        if(i>0){
            throw new GunsException(502,"角色名已存在");
        }
        role.setDeptid(ShiroKit.getUser().getDeptId());
        roleMapper.insert(role);
        return SUCCESS_TIP;
    }

    /**
     * 校验角色名
     */
    @RequestMapping(value = "/checkName")
    @ResponseBody
    public Map checkName(String name) {
        Map map = new HashMap();
        Role role = roleMapper.findByName(name);
        if(role != null){
            map.put("code",500);
        }
        return map;
    }
    /**
     * 角色修改
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)
    @ResponseBody
    public Tip edit(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleMapper.updateById(role);

        //删除缓存
        CacheKit.removeAll(Cache.CONSTANT);
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除角色", key = "roleId", dict = RoleDict.class)
    @ResponseBody
    public Map remove(@RequestParam Integer roleId) {
        Map map = new HashMap();
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }

        Page<Map<String,Object>> page = new Page<>(1, 10);
        EntityWrapper<Map<String,Object>> wrapper = new EntityWrapper<>();
        if(StrKit.isNotEmpty(roleId+"")) wrapper.like("roleid",roleId+"");
        List<Map<String,Object>> userPage=adminMapper.selectAdmins(page,wrapper,map);

        if(userPage!=null && userPage.size()>0){
            map.put("msg","该角色下还有管理员，不能删除");
            map.put("code","300");
            return map;
        }
        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        this.roleService.delRoleById(roleId);

        //删除缓存
        CacheKit.removeAll(Cache.CONSTANT);
        map.put("code",200);
        return map;
    }

    /**
     * 查看角色
     */
    @RequestMapping(value = "/view/{roleId}")
    @ResponseBody
    public Tip view(@PathVariable Integer roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleMapper.selectById(roleId);
        return SUCCESS_TIP;
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    @BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)
    @ResponseBody
    public Tip setAuthority(@RequestParam("roleId") Integer roleId, @RequestParam("ids") String ids) {
        if (ToolUtil.isOneEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.setAuthority(roleId, ids);
        return SUCCESS_TIP;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeList")
    @ResponseBody
    public List<ZTreeNode> roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleDao.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeListByUserId/{userId}")
    @ResponseBody
    public List<ZTreeNode> roleTreeListByUserId(@PathVariable Integer userId) {
        Admin admin = this.adminMapper.selectById(userId);
        String roleid = admin.getRoleid();
        if (ToolUtil.isEmpty(roleid)) {
            List<ZTreeNode> roleTreeList = this.roleDao.roleTreeList();
            return roleTreeList;
        } else {
            String[] strArray = Convert.toStrArray(",", roleid);
            List<ZTreeNode> roleTreeListByUserId = this.roleDao.roleTreeListByRoleId(strArray);
            return roleTreeListByUserId;
        }
    }

}
