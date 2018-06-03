package cn.mulanbay.pms.web.bean.request.diary;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/2/5.
 */
public class DiaryDateStatSearch extends QueryBulider implements DateStatSearch,BindUser {

    @Query(fieldName = "year", op = Parameter.Operator.GTE)
    private Integer minYear;

    @Query(fieldName = "year", op = Parameter.Operator.LTE)
    private Integer maxYear;

    @Query(fieldName = "words", op = Parameter.Operator.GTE)
    private Integer minWords;

    @Query(fieldName = "words", op = Parameter.Operator.LTE)
    private Integer maxWords;

    @Query(fieldName = "userId", op = Parameter.Operator.EQ)
    public Long userId;

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }

    public Integer getMinWords() {
        return minWords;
    }

    public void setMinWords(Integer minWords) {
        this.minWords = minWords;
    }

    public Integer getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(Integer maxWords) {
        this.maxWords = maxWords;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public Date getEndDate() {
        return null;
    }

    @Override
    public DateGroupType getDateGroupType() {
        return null;
    }

    @Override
    public Boolean isCompliteDate() {
        return null;
    }
}
