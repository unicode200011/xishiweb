package com.stylefeng.guns.rest.modular.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.persistence.model.*;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefent.guns.entity.query.CommonPageQuery;
import com.stylefent.guns.entity.query.CommonQuery;
import com.stylefent.guns.entity.vo.AccountVo;
import com.stylefent.guns.entity.vo.SessionUserVo;
import com.stylefent.guns.entity.vo.UpdateUserInfoVo;


public interface IUserService extends IService<User> {

    /**
     * 检查用户状态
     *
     * @param dbUser
     * @return
     */
    boolean checkUser(User dbUser);

    /**
     * 缓存获取用户
     *
     * @param userId
     * @return
     */
    User getFromCache(Long userId);

    /**
     * 重载缓存
     *
     * @param userId
     */
    void reloadUserToCache(Long userId);

    /**
     * 修改支付密码
     *
     * @param accountVo
     * @return
     */
    BaseJson changePwd(AccountVo accountVo);


    /**
     * 验证支付密码
     *
     * @param query
     * @return
     */
    BaseJson payPwdValidate(CommonQuery query);

    /**
     * 用户个人资料
     *
     * @param userId
     * @return
     */
    BaseJson userInfo(Long userId);

    BaseJson checkUserInfo(Long id,Long userId);

    /**
     * 更新用户个人资料
     *
     * @param
     * @return
     */
    BaseJson updateUserInfo(UpdateUserInfoVo updateUserInfoVo);



    /**
     * 查看用户主页
     *
     * @param query
     * @return
     */
    BaseJson otherUserIndexInfo(CommonQuery query);



    BaseJson addAttention(CommonQuery query);

    BaseJson cancelAttention(CommonQuery query);

    BaseJson selectWalletIndex(Long userId);


    BaseJson getGiftList();



    BaseJson userIndexInfo(Long userId);

    void userReport(CommonQuery query);

    BaseJson validatePwd(AccountVo accountVo);
}
