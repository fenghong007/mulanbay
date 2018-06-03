package cn.mulanbay.pms.web.bean.request.music;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/31.
 */
public class MusicPracticeTuneByTunenameSearch extends QueryBulider implements BindUser {


    @Query(fieldName = "tune", op = Parameter.Operator.EQ)
    private String tune;

    @Query(fieldName = "musicPractice.practiceDate", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "musicPractice.practiceDate", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "userId", op = Parameter.Operator.EQ)
    private Long userId;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
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

    public String getTune() {
        return tune;
    }

    public void setTune(String tune) {
        this.tune = tune;
    }
}
