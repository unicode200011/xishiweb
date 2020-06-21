package com.stylefeng.guns.admin.modular.system.service.impl;


import com.stylefeng.guns.admin.modular.system.dao.IndexDataDao;
import com.stylefeng.guns.admin.modular.system.service.IndexDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 首页数据 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-08
 */
@Service
public class IndexDataServiceImpl implements IndexDataService {

    @Autowired
    IndexDataDao indexDataDao;


}
