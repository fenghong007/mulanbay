package cn.mulanbay.pms.web.bean.request.music;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.MusicPracticeTuneType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;
import cn.mulanbay.web.bean.request.PageSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/13.
 */
public class MusicPracticeTuneSearch extends PageSearch implements DateStatSearch ,BindUser,FullEndDateTime {

    @Query(fieldName = "musicPractice.id", op = Parameter.Operator.EQ)
    private Long musicPracticeId;

    @Query(fieldName = "tune", op = Parameter.Operator.LIKE)
    private String tuneName;

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

    public Long getMusicPracticeId() {
        return musicPracticeId;
    }

    public void setMusicPracticeId(Long musicPracticeId) {
        this.musicPracticeId = musicPracticeId;
    }

    public String getTuneName() {
        return tuneName;
    }

    public void setTuneName(String tuneName) {
        this.tuneName = tuneName;
    }

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
}
