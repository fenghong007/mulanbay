package cn.mulanbay.pms.web.bean.request.life;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.persistent.query.CrossType;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.web.bean.request.PageSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/14.
 */
public class LifeExperienceDetailSearch extends PageSearch implements BindUser,FullEndDateTime {

    @Query(fieldName = "lifeExperience.id", op = Parameter.Operator.EQ)
    private Long lifeExperienceId;

    @Query(fieldName = "country,province,city,location", op = Parameter.Operator.LIKE,crossType = CrossType.OR)
    private String name;

    @Query(fieldName = "occurDate", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "occurDate", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "userId", op = Parameter.Operator.EQ)
    public Long userId;

    public Long getLifeExperienceId() {
        return lifeExperienceId;
    }

    public void setLifeExperienceId(Long lifeExperienceId) {
        this.lifeExperienceId = lifeExperienceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
