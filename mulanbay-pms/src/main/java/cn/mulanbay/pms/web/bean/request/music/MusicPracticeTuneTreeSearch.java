package cn.mulanbay.pms.web.bean.request.music;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.MusicPracticeTuneType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/13.
 */
public class MusicPracticeTuneTreeSearch extends QueryBulider implements DateStatSearch ,BindUser {

    @Query(fieldName = "musicPractice.practiceDate", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "musicPractice.practiceDate", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "musicPractice.userId", op = Parameter.Operator.EQ)
    public Long userId;

    @Query(fieldName = "musicPractice.musicInstrument.id", op = Parameter.Operator.EQ)
    private Long musicInstrumentId;

    @Query(fieldName = "tuneType", op = Parameter.Operator.EQ)
    private MusicPracticeTuneType tuneType;

    private Boolean needRoot;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public DateGroupType getDateGroupType() {
        return null;
    }

    @Override
    public Boolean isCompliteDate() {
        return null;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMusicInstrumentId() {
        return musicInstrumentId;
    }

    public void setMusicInstrumentId(Long musicInstrumentId) {
        this.musicInstrumentId = musicInstrumentId;
    }

    public MusicPracticeTuneType getTuneType() {
        return tuneType;
    }

    public void setTuneType(MusicPracticeTuneType tuneType) {
        this.tuneType = tuneType;
    }

    public Boolean getNeedRoot() {
        return needRoot;
    }

    public void setNeedRoot(Boolean needRoot) {
        this.needRoot = needRoot;
    }
}
