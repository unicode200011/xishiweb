package com.stylefeng.guns.admin.modular.areas.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.admin.modular.areas.service.IProvincesService;
import com.stylefeng.guns.persistence.dao.ProvincesMapper;
import com.stylefeng.guns.persistence.model.Provinces;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 省份 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-07-23
 */
@Service
public class ProvincesServiceImpl extends ServiceImpl<ProvincesMapper, Provinces> implements IProvincesService {

}
