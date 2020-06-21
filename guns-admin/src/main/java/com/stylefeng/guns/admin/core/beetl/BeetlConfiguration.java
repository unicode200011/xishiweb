package com.stylefeng.guns.admin.core.beetl;

import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.admin.core.util.KaptchaUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    @Override
    public void initOther() {

        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
        groupTemplate.registerFunctionPackage("dateUtil", new DateUtils());

    }

}
