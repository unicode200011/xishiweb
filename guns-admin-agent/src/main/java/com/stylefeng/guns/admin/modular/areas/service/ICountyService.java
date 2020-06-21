package com.stylefeng.guns.admin.modular.areas.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.persistence.model.County;

/**
 * <p>
 * 区县 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-03
 */
public interface ICountyService extends IService<County> {

    Page<County> selectCountyList(Page<County> page, EntityWrapper<County> entityWrapper);
}
