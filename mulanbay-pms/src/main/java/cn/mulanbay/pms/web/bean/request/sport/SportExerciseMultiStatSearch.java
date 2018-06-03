package cn.mulanbay.pms.web.bean.request.sport;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/30.
 */
public class SportExerciseMultiStatSearch extends QueryBulider implements BindUser {

    @Query(fieldName = "exercise_date", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "exercise_date", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "user_id", op = Parameter.Operator.EQ)
    public Long userId;

    // 由于需要同时支持hql,sql，这个就不做自动绑定
    @Query(fieldName = "sport_type_id", op = Parameter.Operator.EQ)
    private Integer sportTypeId;

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

    public Integer getSportTypeId() {
        return sportTypeId;
    }

    public void setSportTypeId(Integer sportTypeId) {
        this.sportTypeId = sportTypeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
