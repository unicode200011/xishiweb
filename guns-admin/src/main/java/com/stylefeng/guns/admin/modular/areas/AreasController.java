package com.stylefeng.guns.admin.modular.areas;

import com.stylefeng.guns.admin.core.util.AreasUtil;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/areas")
public class AreasController extends BaseController {
    @RequestMapping("/getCities")
    @ResponseBody
    public Object getCities(Integer provinceId){
        return AreasUtil.getCityListByPid(provinceId + "");
    }

    @RequestMapping(value = "/getCounties")
    @ResponseBody
    public Object getCounties(Integer cityId) {
        return AreasUtil.getCountiesListByCid(cityId+"");
    }
}
