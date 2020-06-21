package com.stylefeng.guns.admin.modular.liveManage.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.Live;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-05
 */
public interface ILiveService extends IService<Live> {

    Page<Live> selectLivePage(Page<Live> livePage, EntityWrapper<Live> entityWrapper);

    List<Map> selectHotList();

}
