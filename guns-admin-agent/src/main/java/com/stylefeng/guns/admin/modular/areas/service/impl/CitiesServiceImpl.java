package com.stylefeng.guns.admin.modular.areas.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.admin.modular.areas.service.ICitiesService;
import com.stylefeng.guns.persistence.dao.CitiesMapper;
import com.stylefeng.guns.persistence.model.Cities;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 城市 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-07-23
 */
@Service
public class CitiesServiceImpl extends ServiceImpl<CitiesMapper, Cities> implements ICitiesService {

}
