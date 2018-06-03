package cn.mulanbay.pms.web.bean.request.data;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.UserBehaviorType;

import javax.validation.constraints.NotNull;

/**
 * Created by fenghong on 2017/9/4.
 */
public class UserBehaviorStatSearch implements BindUser {

    @NotNull(message = "{validate.stat.year.NotNull}")
    private Integer year;

    @NotNull(message = "{validate.stat.month.NotNull}")
    private Integer month;

    public Long userId;

    private String name;

    private Long userBehaviorId;

    private DateGroupType dateGroupType;

    private UserBehaviorType behaviorType;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserBehaviorType getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(UserBehaviorType behaviorType) {
        this.behaviorType = behaviorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateGroupType getDateGroupType() {
        return dateGroupType;
    }

    public void setDateGroupType(DateGroupType dateGroupType) {
        this.dateGroupType = dateGroupType;
    }

    public Long getUserBehaviorId() {
        return userBehaviorId;
    }

    public void setUserBehaviorId(Long userBehaviorId) {
        this.userBehaviorId = userBehaviorId;
    }
}
