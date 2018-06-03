package cn.mulanbay.pms.web.bean.request.sleep;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/31.
 */
public class SleepAnalyseStatSearch extends QueryBulider implements DateStatSearch,BindUser {


    @Query(fieldName = "sleep_date", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "sleep_date", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "user_id", op = Parameter.Operator.EQ)
    private Long userId;

    private DateGroupType xgroupType;

    private SleepStatType ygroupType;

    public DateGroupType getXgroupType() {
        return xgroupType;
    }

    public void setXgroupType(DateGroupType xgroupType) {
        this.xgroupType = xgroupType;
    }

    public SleepStatType getYgroupType() {
        return ygroupType;
    }

    public void setYgroupType(SleepStatType ygroupType) {
        this.ygroupType = ygroupType;
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

    public enum SleepStatType{
        DURATION("睡眠时长","小时"),SLEEP_TIME("睡眠点","点"),GETUP_TIME("起床点","点");

        private String name;

        private String unit;

        SleepStatType(String name) {
            this.name = name;
        }

        SleepStatType(String name, String unit) {
            this.name = name;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

}
