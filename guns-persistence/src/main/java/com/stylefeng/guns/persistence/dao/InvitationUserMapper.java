package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.persistence.model.InvitationUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 邀请用户列表 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@Repository
public interface InvitationUserMapper extends BaseMapper<InvitationUser> {

    /**
     * 查询我的推荐人
     *
     * @param userId
     * @return
     */
    InvitationUser selectMyInviteUser(@Param("userId") Long userId);
    String selectToUid(@Param("fromUid") int fromUid);
    List<Map<String, Object>> selectInvitationUser(RowBounds rowBounds,
                                                   @Param("ew") EntityWrapper<Map<String, Object>> wrapper,
                                                   @Param("map") Map map,
                                                   @Param("id") int id);
     InvitationUser selectInviteUserByFromId(@Param("fromUid") int id);

    InvitationUser selectBytoUid(Long id);
}
