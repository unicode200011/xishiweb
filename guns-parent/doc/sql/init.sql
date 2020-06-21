-- ----------------------------
-- Records of eb_dept
-- ----------------------------
INSERT INTO `eb_dept` VALUES ('24', '1', '0', '[0],', '总公司', '总公司', '', null);
INSERT INTO `eb_dept` VALUES ('25', '2', '24', '[0],[24],', '开发部', '开发部', '', null);
INSERT INTO `eb_dept` VALUES ('26', '3', '24', '[0],[24],', '运营部', '运营部', '', null);
INSERT INTO `eb_dept` VALUES ('27', '4', '24', '[0],[24],', '战略部', '战略部', '', null);
-- ----------------------------
-- Records of eb_dict
-- ----------------------------
INSERT INTO `eb_dict` VALUES ('16', '0', '0', '状态', null);
INSERT INTO `eb_dict` VALUES ('17', '1', '16', '启用', null);
INSERT INTO `eb_dict` VALUES ('18', '2', '16', '禁用', null);
INSERT INTO `eb_dict` VALUES ('29', '0', '0', '性别', null);
INSERT INTO `eb_dict` VALUES ('30', '1', '29', '男', null);
INSERT INTO `eb_dict` VALUES ('31', '2', '29', '女', null);
INSERT INTO `eb_dict` VALUES ('35', '0', '0', '账号状态', null);
INSERT INTO `eb_dict` VALUES ('36', '1', '35', '启用', null);
INSERT INTO `eb_dict` VALUES ('37', '2', '35', '冻结', null);
INSERT INTO `eb_dict` VALUES ('38', '3', '35', '已删除', null);

-- ----------------------------
-- Records of eb_menu
-- ----------------------------
INSERT INTO `eb_menu` VALUES ('105', 'system', '0', '[0],', '系统管理', 'fa-user', '#', '4', '1', '1', null, '1', '1');
INSERT INTO `eb_menu` VALUES ('106', 'mgr', 'system', '[0],[system],', '用户管理', '', '/mgr', '1', '2', '1', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('107', 'mgr_add', 'mgr', '[0],[system],[mgr],', '添加用户', null, '/mgr/add', '1', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('108', 'mgr_edit', 'mgr', '[0],[system],[mgr],', '修改用户', null, '/mgr/edit', '2', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('109', 'mgr_delete', 'mgr', '[0],[system],[mgr],', '删除用户', null, '/mgr/delete', '3', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('110', 'mgr_reset', 'mgr', '[0],[system],[mgr],', '重置密码', null, '/mgr/reset', '4', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('111', 'mgr_freeze', 'mgr', '[0],[system],[mgr],', '冻结用户', null, '/mgr/freeze', '5', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('112', 'mgr_unfreeze', 'mgr', '[0],[system],[mgr],', '解除冻结用户', null, '/mgr/unfreeze', '6', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('113', 'mgr_setRole', 'mgr', '[0],[system],[mgr],', '分配角色', null, '/mgr/setRole', '7', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('114', 'role', 'system', '[0],[system],', '角色管理', null, '/role', '2', '2', '1', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('115', 'role_add', 'role', '[0],[system],[role],', '添加角色', null, '/role/add', '1', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('116', 'role_edit', 'role', '[0],[system],[role],', '修改角色', null, '/role/edit', '2', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('117', 'role_remove', 'role', '[0],[system],[role],', '删除角色', null, '/role/remove', '3', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('118', 'role_setAuthority', 'role', '[0],[system],[role],', '配置权限', null, '/role/setAuthority', '4', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('119', 'menu', 'system', '[0],[system],', '菜单管理', null, '/menu', '4', '2', '1', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('120', 'menu_add', 'menu', '[0],[system],[menu],', '添加菜单', null, '/menu/add', '1', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('121', 'menu_edit', 'menu', '[0],[system],[menu],', '修改菜单', null, '/menu/edit', '2', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('122', 'menu_remove', 'menu', '[0],[system],[menu],', '删除菜单', null, '/menu/remove', '3', '3', '0', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('128', 'log', 'system', '[0],[system],', '业务日志', null, '/log', '6', '2', '1', null, '1', '0');
INSERT INTO `eb_menu` VALUES ('130', 'druid', 'system', '[0],[system],', '监控管理', null, '/druid', '7', '2', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('131', 'dept', 'system', '[0],[system],', '部门管理', null, '/dept', '3', '2', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('132', 'dict', 'system', '[0],[system],', '字典管理', null, '/dict', '4', '2', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('133', 'loginLog', 'system', '[0],[system],', '登录日志', null, '/loginLog', '6', '2', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('134', 'log_clean', 'log', '[0],[system],[log],', '清空日志', null, '/log/delLog', '3', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('135', 'dept_add', 'dept', '[0],[system],[dept],', '添加部门', null, '/dept/add', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('136', 'dept_update', 'dept', '[0],[system],[dept],', '修改部门', null, '/dept/update', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('137', 'dept_delete', 'dept', '[0],[system],[dept],', '删除部门', null, '/dept/delete', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('138', 'dict_add', 'dict', '[0],[system],[dict],', '添加字典', null, '/dict/add', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('139', 'dict_update', 'dict', '[0],[system],[dict],', '修改字典', null, '/dict/update', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('140', 'dict_delete', 'dict', '[0],[system],[dict],', '删除字典', null, '/dict/delete', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('141', 'notice', 'system', '[0],[system],', '通知管理', null, '/notice', '9', '2', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('142', 'notice_add', 'notice', '[0],[system],[notice],', '添加通知', null, '/notice/add', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('143', 'notice_update', 'notice', '[0],[system],[notice],', '修改通知', null, '/notice/update', '2', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('144', 'notice_delete', 'notice', '[0],[system],[notice],', '删除通知', null, '/notice/delete', '3', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('145', 'hello', '0', '[0],', '通知', 'fa-rocket', '/notice/hello', '1', '1', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('148', 'code', '0', '[0],', '代码生成', 'fa-code', '/code', '3', '1', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('149', 'api_mgr', '0', '[0],', '接口文档', 'fa-leaf', '/swagger-ui.html', '2', '1', '1', null, '1', null);
INSERT INTO `eb_menu` VALUES ('150', 'to_menu_edit', 'menu', '[0],[system],[menu],', '菜单编辑跳转', '', '/menu/menu_edit', '4', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('151', 'menu_list', 'menu', '[0],[system],[menu],', '菜单列表', '', '/menu/list', '5', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('152', 'to_dept_update', 'dept', '[0],[system],[dept],', '修改部门跳转', '', '/dept/dept_update', '4', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('153', 'dept_list', 'dept', '[0],[system],[dept],', '部门列表', '', '/dept/list', '5', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('154', 'dept_detail', 'dept', '[0],[system],[dept],', '部门详情', '', '/dept/detail', '6', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('155', 'to_dict_edit', 'dict', '[0],[system],[dict],', '修改菜单跳转', '', '/dict/dict_edit', '4', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('156', 'dict_list', 'dict', '[0],[system],[dict],', '字典列表', '', '/dict/list', '5', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('157', 'dict_detail', 'dict', '[0],[system],[dict],', '字典详情', '', '/dict/detail', '6', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('158', 'log_list', 'log', '[0],[system],[log],', '日志列表', '', '/log/list', '2', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('159', 'log_detail', 'log', '[0],[system],[log],', '日志详情', '', '/log/detail', '3', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('160', 'del_login_log', 'loginLog', '[0],[system],[loginLog],', '清空登录日志', '', '/loginLog/delLoginLog', '1', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('161', 'login_log_list', 'loginLog', '[0],[system],[loginLog],', '登录日志列表', '', '/loginLog/list', '2', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('162', 'to_role_edit', 'role', '[0],[system],[role],', '修改角色跳转', '', '/role/role_edit', '5', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('163', 'to_role_assign', 'role', '[0],[system],[role],', '角色分配跳转', '', '/role/role_assign', '6', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('164', 'role_list', 'role', '[0],[system],[role],', '角色列表', '', '/role/list', '7', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('165', 'to_assign_role', 'mgr', '[0],[system],[mgr],', '分配角色跳转', '', '/mgr/role_assign', '8', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('166', 'to_user_edit', 'mgr', '[0],[system],[mgr],', '编辑用户跳转', '', '/mgr/user_edit', '9', '3', '0', null, '1', null);
INSERT INTO `eb_menu` VALUES ('167', 'mgr_list', 'mgr', '[0],[system],[mgr],', '用户列表', '', '/mgr/list', '10', '3', '0', null, '1', null);

-- ----------------------------
-- Records of eb_notice
-- ----------------------------
INSERT INTO `eb_notice` VALUES ('1', '世界', '10', '欢迎使用彩讯管理系统', '2017-01-11 08:53:20', '1');


-- ----------------------------
-- Records of eb_relation
-- ----------------------------
INSERT INTO `eb_relation` VALUES ('3737', '105', '1');
INSERT INTO `eb_relation` VALUES ('3738', '106', '1');
INSERT INTO `eb_relation` VALUES ('3739', '107', '1');
INSERT INTO `eb_relation` VALUES ('3740', '108', '1');
INSERT INTO `eb_relation` VALUES ('3741', '109', '1');
INSERT INTO `eb_relation` VALUES ('3742', '110', '1');
INSERT INTO `eb_relation` VALUES ('3743', '111', '1');
INSERT INTO `eb_relation` VALUES ('3744', '112', '1');
INSERT INTO `eb_relation` VALUES ('3745', '113', '1');
INSERT INTO `eb_relation` VALUES ('3746', '165', '1');
INSERT INTO `eb_relation` VALUES ('3747', '166', '1');
INSERT INTO `eb_relation` VALUES ('3748', '167', '1');
INSERT INTO `eb_relation` VALUES ('3749', '114', '1');
INSERT INTO `eb_relation` VALUES ('3750', '115', '1');
INSERT INTO `eb_relation` VALUES ('3751', '116', '1');
INSERT INTO `eb_relation` VALUES ('3752', '117', '1');
INSERT INTO `eb_relation` VALUES ('3753', '118', '1');
INSERT INTO `eb_relation` VALUES ('3754', '162', '1');
INSERT INTO `eb_relation` VALUES ('3755', '163', '1');
INSERT INTO `eb_relation` VALUES ('3756', '164', '1');
INSERT INTO `eb_relation` VALUES ('3757', '119', '1');
INSERT INTO `eb_relation` VALUES ('3758', '120', '1');
INSERT INTO `eb_relation` VALUES ('3759', '121', '1');
INSERT INTO `eb_relation` VALUES ('3760', '122', '1');
INSERT INTO `eb_relation` VALUES ('3761', '150', '1');
INSERT INTO `eb_relation` VALUES ('3762', '151', '1');
INSERT INTO `eb_relation` VALUES ('3763', '128', '1');
INSERT INTO `eb_relation` VALUES ('3764', '134', '1');
INSERT INTO `eb_relation` VALUES ('3765', '158', '1');
INSERT INTO `eb_relation` VALUES ('3766', '159', '1');
INSERT INTO `eb_relation` VALUES ('3767', '130', '1');
INSERT INTO `eb_relation` VALUES ('3768', '131', '1');
INSERT INTO `eb_relation` VALUES ('3769', '135', '1');
INSERT INTO `eb_relation` VALUES ('3770', '136', '1');
INSERT INTO `eb_relation` VALUES ('3771', '137', '1');
INSERT INTO `eb_relation` VALUES ('3772', '152', '1');
INSERT INTO `eb_relation` VALUES ('3773', '153', '1');
INSERT INTO `eb_relation` VALUES ('3774', '154', '1');
INSERT INTO `eb_relation` VALUES ('3775', '132', '1');
INSERT INTO `eb_relation` VALUES ('3776', '138', '1');
INSERT INTO `eb_relation` VALUES ('3777', '139', '1');
INSERT INTO `eb_relation` VALUES ('3778', '140', '1');
INSERT INTO `eb_relation` VALUES ('3779', '155', '1');
INSERT INTO `eb_relation` VALUES ('3780', '156', '1');
INSERT INTO `eb_relation` VALUES ('3781', '157', '1');
INSERT INTO `eb_relation` VALUES ('3782', '133', '1');
INSERT INTO `eb_relation` VALUES ('3783', '160', '1');
INSERT INTO `eb_relation` VALUES ('3784', '161', '1');
INSERT INTO `eb_relation` VALUES ('3785', '141', '1');
INSERT INTO `eb_relation` VALUES ('3786', '142', '1');
INSERT INTO `eb_relation` VALUES ('3787', '143', '1');
INSERT INTO `eb_relation` VALUES ('3788', '144', '1');
INSERT INTO `eb_relation` VALUES ('3789', '145', '1');
INSERT INTO `eb_relation` VALUES ('3790', '148', '1');
INSERT INTO `eb_relation` VALUES ('3791', '149', '1');

-- ----------------------------
-- Records of eb_role
-- ----------------------------
INSERT INTO `eb_role` VALUES ('1', '1', '0', '超级管理员', '24', 'administrator', '1','');


-- ----------------------------
-- Records of eb_admin
-- ----------------------------
INSERT INTO `eb_admin` VALUES ('1', 'girl.gif', 'admin', 'ecfadcde9305f8891bcfe5a1e28c253e', '8pgby', '张三', '2017-05-05 00:00:00', '2', 'sn93@qq.com', '18200000000', '1', '27', '1', '2016-01-29 08:49:53', '25');