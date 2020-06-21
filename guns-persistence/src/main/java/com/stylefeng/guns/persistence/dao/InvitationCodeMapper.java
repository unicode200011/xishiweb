package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.persistence.model.InvitationCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 邀请码 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@Repository
public interface InvitationCodeMapper extends BaseMapper<InvitationCode> {

    /**
     * 根据邀请码查询
     *
     * @param code
     * @return
     */
    InvitationCode selectByCode(@Param("code") String code);

    /**
     * 查询用户邀请码
     *
     * @param id
     * @return
     */
    InvitationCode selectOneByUserId(@Param("userId") Long userId);
}
