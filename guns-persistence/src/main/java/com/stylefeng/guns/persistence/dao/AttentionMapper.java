package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.persistence.model.Attention;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 关注 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2018-06-27
 */
@Repository
public interface AttentionMapper extends BaseMapper<Attention> {

    /**
     * 用户关注记录
     *
     * @param userId
     * @param linkId
     * @return
     */
    Attention selectOneAttentionRecord(@Param("userId") Long userId, @Param("linkId") Long linkId);

    Attention selectAttentionByUserAndLikeId(@Param("userId") Long userId, @Param("linkId") Long linkId);

    List<Map> selectUserFriendList(@Param("ew") EntityWrapper entityWrapper);

    List<Map> findMyfansList(@Param("ew") EntityWrapper entityWrapper);
    List<Map<String, Object>> selectUserList(RowBounds rowBounds,
                                             @Param("ew") EntityWrapper<Map<String, Object>> wrapper,
                                             @Param("map") Map map,
                                             @Param("id") String id
    );
}
