package com.stylefeng.guns.admin.modular.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.common.constant.Const;
import com.stylefeng.guns.admin.common.constant.dictmap.AdminDict;
import com.stylefeng.guns.admin.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.admin.common.constant.state.ManagerStatus;
import com.stylefeng.guns.admin.common.exception.BizExceptionEnum;
import com.stylefeng.guns.admin.modular.system.dao.RoleDao;
import com.stylefeng.guns.admin.modular.system.service.AdminService;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.dao.AdminMapper;
import com.stylefeng.guns.persistence.dao.RoleMapper;
import com.stylefeng.guns.admin.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.db.Db;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.admin.modular.system.dao.AdminMgrDao;
import com.stylefeng.guns.admin.modular.system.factory.UserFactory;
import com.stylefeng.guns.admin.modular.system.transfer.AdminDto;
import com.stylefeng.guns.persistence.model.Admin;
import com.stylefeng.guns.persistence.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * 系统管理员控制器
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
public class AdminMgrController extends BaseController {

    private static String PREFIX = "/system/admin/";

    @Resource
    private GunsProperties gunsProperties;

    @Resource
    private AdminMgrDao managerDao;
    @Autowired
    private RoleDao roleDao;

    @Resource
    private AdminMapper adminMapper;
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;


    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Role> deptRoles = this.getDeptRoles(ShiroKit.getUser().getDeptId());
        model.addAttribute("item",deptRoles);
        return PREFIX + "admin.html";
    }

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("/admin_add")
    public String addView(Model model) {
        List<Role> deptRoles = this.getDeptRoles(ShiroKit.getUser().getDeptId());
        model.addAttribute("allRoles",deptRoles);
        return PREFIX + "admin_add.html";
    }

    /**
     * 跳转到角色分配页面
     */
    //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
    @RequestMapping("/role_assign/{adminId}")
    public String roleAssign(@PathVariable Integer adminId, Model model) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Admin admin = Db.create(AdminMapper.class).selectOneByCon("id", adminId);
        model.addAttribute("adminId", adminId);
        model.addAttribute("userAccount", admin.getAccount());
        return PREFIX + "admin_roleassign.html";
    }

    /**
     * 跳转到编辑管理员页面
     */

    @RequestMapping("/admin_edit/{adminId}")
    public String userEdit(@PathVariable Integer adminId, Model model) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
       /* assertAuth(adminId);*/
        List<Role> deptRoles = this.getDeptRoles(ShiroKit.getUser().getDeptId());
        model.addAttribute("allRoles",deptRoles);

        Admin admin = this.adminMapper.selectById(adminId);
        String[] roleids = admin.getRoleid().split("[,]");
        List<Integer> ids = new ArrayList<>();
        for(int i= 0 ;i< roleids.length;i++){
            ids.add(Integer.parseInt(roleids[i]));
        }
        model.addAttribute(admin);
        model.addAttribute("roleids",ids);
        model.addAttribute("adminId",adminId);
        LogObjectHolder.me().set(admin);
        return PREFIX + "admin_edit.html";
    }

    /**
     * 跳转到编辑管理员页面
     */

    @RequestMapping("/admin_user_edit/{adminId}")
    public String admin_user_edit(@PathVariable Integer adminId, Model model) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(adminId);
        List<Role> deptRoles = this.getDeptRoles(ShiroKit.getUser().getDeptId());

        model.addAttribute("allRoles",deptRoles);
        Admin admin = this.adminMapper.selectById(adminId);
        String[] roleids = admin.getRoleid().split("[,]");
        List<Integer> ids = new ArrayList<>();
        for(int i= 1 ;i< roleids.length;i++){
            ids.add(Integer.parseInt(roleids[i]));
        }
        model.addAttribute(admin);
        model.addAttribute("roleids",ids);
        LogObjectHolder.me().set(admin);
        return PREFIX + "admin_user_edit.html";
    }


    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/admin_info")
    public String userInfo(Model model) {
        Integer userId = ShiroKit.getUser().getId();
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Admin user = this.adminMapper.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        LogObjectHolder.me().set(user);
        return PREFIX + "admin_view.html";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/admin_chpwd")
    public String chPwd() {
        return PREFIX + "admin_chpwd.html";
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new GunsException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
        }
        Integer userId = ShiroKit.getUser().getId();
        Admin admin = adminMapper.selectById(userId);
        String oldMd5 = ShiroKit.md5(oldPwd, admin.getSalt());
        if (admin.getPassword().equals(oldMd5)) {
            String newMd5 = ShiroKit.md5(newPwd, admin.getSalt());
            admin.setPassword(newMd5);
            admin.updateById();
            return SUCCESS_TIP;
        } else {
            throw new GunsException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       @RequestParam(required = false) String condition,
                       @RequestParam(required = false) String state,
                       @RequestParam(required = false) String roleid
                       ) {
        Map param = new HashMap();
        Page<Map<String,Object>> page =new Page<>(pageNumber,pageSize);
        EntityWrapper<Map<String,Object>> wrapper  = new EntityWrapper<>();
        wrapper.ne("roleid",2);
        if(StrKit.isNotEmpty(condition)) wrapper.like("account",condition).or().like("name",condition).or().like("phone",condition);

        Integer deptId = ShiroKit.getUser().getDeptId();
        if(deptId.compareTo(1) != 0){
            wrapper.eq("deptid",deptId);
        }else {
            wrapper.eq( "parentid",1);
        }

        Page<Map<String,Object>> userPage=adminService.selectAdmins(page,wrapper,param);
        return userPage;
    }

    /**
     * 添加管理员
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加管理员", key = "account", dict = AdminDict.class)
    @ResponseBody
    public Tip add(@Valid AdminDto user, BindingResult result) {

        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }

        String account = user.getAccount();
        Integer account1 = adminMapper.selectCount(new EntityWrapper<Admin>().eq("account", account));
        if(account1>0){
            throw new GunsException(BizExceptionEnum.ADMIN_IS_USED);
        }

        String phone = user.getPhone();
        Integer account2 = adminMapper.selectCount(new EntityWrapper<Admin>().eq("phone", phone));
        if(account2>0){
            throw new GunsException(BizExceptionEnum.PHONE_IS_USED);
        }

        String roleid = user.getRoleid();
        List<String> strings = JSON.parseArray(roleid, String.class);
        String join = String.join(",", strings);
        user.setRoleid(join);


        // 完善账号信息
        user.setDeptid(ShiroKit.getUser().getDeptId());
        user.setParentid(ShiroKit.getUser().getId());//父级
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        Admin user1 = UserFactory.createUser(user);
        user1.setParentid(ShiroKit.getUser().getId());

        this.adminMapper.insert(user1);
        return SUCCESS_TIP;
    }

    @RequestMapping("/checkAccount")
    @ResponseBody
    public Map checkAccount(String account){
        Map map = new HashMap();
        // 判断账号是否重复
        Admin admin = managerDao.getByAccount(account);
        if (admin != null) {
           map.put("code","500");
        }
        return map;
    }

    @RequestMapping("/checkPhone")
    @ResponseBody
    public Map checkPhone(String phone){
        Map map = new HashMap();
        // 判断账号是否重复
        List<Admin> admin = managerDao.getByPhone(phone);
        if (admin != null && admin.size()>0) {
            map.put("code","500");
        }
        return map;
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "修改管理员")
    @ResponseBody
    public Tip edit(@Valid AdminDto adminDto, BindingResult result) throws NoPermissionException {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Admin oldadmin =  adminMapper.selectById(adminDto.getId());
        String account = oldadmin.getAccount();
        String account1 = adminDto.getAccount();
        if(!account.equals(account1)){
            Integer accountNum = adminMapper.selectCount(new EntityWrapper<Admin>().eq("account", account1));
            if(accountNum > 0){
                throw new GunsException(BizExceptionEnum.ADMIN_IS_USED);
            }
        }

        String phone = adminDto.getPhone();
        String phone1 = oldadmin.getPhone();
        if(!phone.equals(phone1)){
            Integer accountNum = adminMapper.selectCount(new EntityWrapper<Admin>().eq("phone", phone));
            if(accountNum > 0){
                throw new GunsException(BizExceptionEnum.PHONE_IS_USED);
            }
        }
        if(StrKit.isNotEmpty(adminDto.getPassword())){
            String newPwd = ShiroKit.md5(adminDto.getPassword(), oldadmin.getSalt());
            String oldPassword = oldadmin.getPassword();

            if(!newPwd.equals(oldPassword)){
                adminDto.setPassword(newPwd);
            }
        }else {
            String password = oldadmin.getPassword();
            adminDto.setPassword(password);
        }
        String roleid = adminDto.getRoleid();
        if(StrKit.isNotEmpty(roleid)){
            List<String> strings = JSON.parseArray(roleid, String.class);
            String join = String.join(",", strings);
            adminDto.setRoleid(join);
        }

            this.adminMapper.updateById(UserFactory.createUser(adminDto));
            return SUCCESS_TIP;
    }

    /**
     * 忘记密码
     * @param phone
     * @param pwd
     * @return
     */
    @RequestMapping("/forgetPWD")
    @ResponseBody
    public Object forgetPWD(String phone,String pwd){
        Integer adminNum = adminMapper.selectCount(new EntityWrapper<Admin>().eq("phone", phone));
        if(adminNum == 0){
            return new ErrorTip(405,"此号码未注册");
        }
        List<Admin> admins = adminMapper.selectList(new EntityWrapper<Admin>().eq("phone", phone));
        Admin admin = admins.get(0);
        admin.setSalt(ShiroKit.getRandomSalt(5));
        admin.setPassword(ShiroKit.md5(pwd, admin.getSalt()));
        adminMapper.updateById(admin);
        return SUCCESS_TIP;
    }


    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除管理员")
    @ResponseBody
    public Tip delete(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
//        assertAuth(userId);
//        this.managerDao.setStatus(userId, ManagerStatus.DELETED.getCode());
        adminMapper.deleteById(userId);
        return SUCCESS_TIP;
    }

    /**
     * 查看管理员详情
     */
    @RequestMapping("/view/{adminId}")
    @ResponseBody
    public Admin view(@PathVariable Integer adminId) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(adminId);
        return this.adminMapper.selectById(adminId);
    }

    /**
     * 重置管理员的密码
     */
    @RequestMapping("/reset")
    @BussinessLog(value = "重置管理员密码", key = "adminId", dict = AdminDict.class)
    @ResponseBody
    public Tip reset(@RequestParam Integer adminId) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(adminId);
        Admin admin = this.adminMapper.selectById(adminId);
        admin.setSalt(ShiroKit.getRandomSalt(5));
        admin.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, admin.getSalt()));
        this.adminMapper.updateById(admin);
        return SUCCESS_TIP;
    }

    /**
     * 冻结管理员
     */
    @RequestMapping("/freeze")
    @BussinessLog(value = "冻结管理员", key = "adminId", dict = AdminDict.class)
    @ResponseBody
    public Tip freeze(@RequestParam Integer adminId) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (adminId.equals(Const.ADMIN_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        assertAuth(adminId);
        this.managerDao.setStatus(adminId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结管理员
     */
    @RequestMapping("/unfreeze")
    @BussinessLog(value = "解除冻结管理员", key = "adminId", dict = AdminDict.class)
    @ResponseBody
    public Tip unfreeze(@RequestParam Integer adminId) {
        if (ToolUtil.isEmpty(adminId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(adminId);
        this.managerDao.setStatus(adminId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
    @BussinessLog(value = "分配角色", key = "adminId,roleIds", dict = AdminDict.class)
    @ResponseBody
    public Tip setRole(@RequestParam("adminId") Integer adminId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(adminId, roleIds)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (adminId.equals(Const.ADMIN_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        assertAuth(adminId);
        this.managerDao.setRoles(adminId, roleIds);
        return SUCCESS_TIP;
    }

    /**
     * 上传图片(上传到项目的webapp/static/img)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        String pictureName = UUID.randomUUID().toString() + ".jpg";
        try {
            String fileSavePath = gunsProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return pictureName;
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    private void assertAuth(Integer userId) {

        List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
        Admin admin = this.adminMapper.selectById(userId);
        Integer deptid = admin.getDeptid();
        if (deptDataScope.contains(deptid)) {
            return;
        } else {
            throw new GunsException(BizExceptionEnum.NO_PERMITION);
        }

    }
    public List<Role> getDeptRoles(Integer deptid){
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("deptid",deptid);
        entityWrapper.gt("id",1);
        List list = roleMapper.selectList(entityWrapper);
        return list;
    }

}
