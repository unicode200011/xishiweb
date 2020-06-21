package com.stylefeng.guns.admin.modular.giftManage.service.impl;

import com.stylefeng.guns.persistence.model.Gift;
import com.stylefeng.guns.persistence.dao.GiftMapper;
import com.stylefeng.guns.admin.modular.giftManage.service.IGiftService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 礼物 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-20
 */
@Service
public class GiftServiceImpl extends ServiceImpl<GiftMapper, Gift> implements IGiftService {

}
