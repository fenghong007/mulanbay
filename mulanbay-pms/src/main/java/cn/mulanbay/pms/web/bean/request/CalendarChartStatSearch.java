package cn.mulanbay.pms.web.bean.request;

import cn.mulanbay.common.aop.BindUser;

/**
 * Created by fenghong on 2017/9/4.
 */
public class CalendarChartStatSearch implements BindUser {

    private Integer year;

    private Long userId;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
