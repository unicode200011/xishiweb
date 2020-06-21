package com.stylefeng.guns.admin.modular.adManage.service.impl;

import com.stylefeng.guns.persistence.model.Ad;
import com.stylefeng.guns.persistence.dao.AdMapper;
import com.stylefeng.guns.admin.modular.adManage.service.IAdService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-14
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {

}
