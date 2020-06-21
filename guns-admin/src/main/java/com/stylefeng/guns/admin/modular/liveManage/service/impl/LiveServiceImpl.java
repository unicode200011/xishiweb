package com.stylefeng.guns.admin.modular.liveManage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveRecordService;
import com.stylefeng.guns.persistence.model.Live;
import com.stylefeng.guns.persistence.dao.LiveMapper;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.persistence.model.LiveRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    ILiveRecordService liveRecordService;

    @Override
    public Page<Live> selectLivePage(Page<Live> livePage, EntityWrapper<Live> entityWrapper) {
        List<Live> lives = baseMapper.selectLivePage(livePage, entityWrapper);
        for (Live life : lives) {
            LiveRecord liveRecord = liveRecordService.selectById(life.getNewLiveRecord());
            if(liveRecord != null){
                Date createTime = liveRecord.getCreateTime();
                Date now = new Date();
                Long time = (now.getTime() - createTime.getTime()) / 1000 / 60;
                life.setShowTime(Integer.valueOf(time+""));
            }
        }
        return livePage.setRecords(lives);
    }

    @Override
    public List<Map> selectHotList() {

        return baseMapper.selectHotList();
    }
}
