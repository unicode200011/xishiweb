package com.stylefeng.guns.admin.modular.liveManage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.Live;
import com.stylefeng.guns.persistence.dao.LiveMapper;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-05
 */
@Service
public class LiveServiceImpl extends ServiceImpl<LiveMapper, Live> implements ILiveService {


    @Override
    public Page<Live> selectLivePage(Page<Live> livePage, EntityWrapper<Live> entityWrapper) {
        return livePage.setRecords(baseMapper.selectLivePage(livePage,entityWrapper));
    }
}
