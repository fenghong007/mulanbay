package cn.mulanbay.pms.web.bean.request.music;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.pms.persistent.domain.MusicPracticeTune;
import cn.mulanbay.pms.persistent.enums.MusicPracticeTuneType;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-15 10:14
 */
public class MusicPracticeTuneFormRequest implements BindUser{

    private Long id;
    private Long userId;
    private Long musicPracticeId;
    private String tune;
    private Integer times;
    private MusicPracticeTune.Level level;
    private MusicPracticeTuneType tuneType;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMusicPracticeId() {
        return musicPracticeId;
    }

    public void setMusicPracticeId(Long musicPracticeId) {
        this.musicPracticeId = musicPracticeId;
    }

    public String getTune() {
        return tune;
    }

    public void setTune(String tune) {
        this.tune = tune;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public MusicPracticeTune.Level getLevel() {
        return level;
    }

    public void setLevel(MusicPracticeTune.Level level) {
        this.level = level;
    }

    public MusicPracticeTuneType getTuneType() {
        return tuneType;
    }

    public void setTuneType(MusicPracticeTuneType tuneType) {
        this.tuneType = tuneType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
