package com.stylefent.guns.entity.form;


import com.stylefeng.guns.core.support.StrKit;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 视频
 *
 * @author: LEIYU
 */
@Setter
public class VideoForm {

    private String playUrl;
    private String cover;
    private String fileId;

    /**
     * 视频ID
     */
    private Long videoId;
    /**
     * 作者
     */
    private Long userId;
    /**
     * 背景音乐iD
     */
    private Long bgmId;
    /**
     * 标题
     */
    private String name;
    /**
     * 经度
     */
    private BigDecimal lgt;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 城市
     */
    private String city;
    /**
     * 省份
     */
    private String province;
    /**
     * 是否草稿 0:否 1:是
     */
    private Integer draft;
    /**
     * 是否米赏视频 0:否 1:是
     */
    private Integer award;
    /**
     * 视频权限: 0:公开 1:好友可见 2:私密
     */
    private Integer perm;
    /**
     * 是否加锁 0:未加锁 1:加锁
     */
    private Integer vLock;
    /**
     * 禁止操作评论 0:否 1:是
     */
    private Integer comment;
    /**
     * 视频可见人
     */
    private String[] friendsArr;
    /**
     * 分类ID
     */
    private String[] typeArr;

    public Long getUserId() {
        return userId;
    }

    public Long getBgmId() {
        return bgmId;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? "" : name;
    }

    public BigDecimal getLgt() {
        return lgt;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public String getCity() {
        return StrKit.isEmpty(city) ? "" : city;
    }

    public String getProvince() {
        return StrKit.isEmpty(province) ? "" : province;
    }

    public Integer getDraft() {
        return draft == null || draft > 1 ? 0 : draft;
    }

    public Integer getAward() {
        return award == null || award > 1 ? 0 : award;
    }

    public Integer getPerm() {
        return perm == null || perm > 2 ? 0 : perm;
    }

    public Integer getVLock() {
        return vLock == null || vLock > 1 ? 0 : vLock;
    }

    public Integer getComment() {
        return comment == null || comment > 1 ? 0 : comment;
    }

    public String[] getFriendsArr() {
        return friendsArr;
    }

    public String[] getTypeArr() {
        return typeArr;
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public String getCover() {
        return cover;
    }

    public String getFileId() {
        return fileId;
    }
}
