package com.stylefeng.guns.admin.modular.system.warpper;

import com.stylefeng.guns.admin.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;

import java.util.List;
import java.util.Map;

/**
 * Created by hejia on 2018/3/16.
 */
public class UserWarpper extends BaseControllerWarpper {

    public UserWarpper(List<Map<String, Object>> list) {
        super(list);
    }
    @Override
    protected void warpTheMap(Map<String, Object> map) {
        map.put("stateName", ConstantFactory.me().getStatusName(Integer.parseInt(String.valueOf(map.get("state")))));
    }
}
