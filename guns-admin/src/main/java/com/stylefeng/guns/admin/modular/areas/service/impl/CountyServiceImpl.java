package com.stylefeng.guns.admin.modular.areas.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.admin.modular.areas.service.ICountyService;
import com.stylefeng.guns.persistence.dao.CountyMapper;
import com.stylefeng.guns.persistence.model.County;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 区县 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-03
 */
@Service
public class CountyServiceImpl extends ServiceImpl<CountyMapper, County> implements ICountyService {

    @Autowired
    CountyMapper countyMapper;
    @Override
    public Page<County> selectCountyList(Page<County> page, EntityWrapper<County> entityWrapper) {
        return page.setRecords(countyMapper.selectCountyList(page,entityWrapper));
    }
}
