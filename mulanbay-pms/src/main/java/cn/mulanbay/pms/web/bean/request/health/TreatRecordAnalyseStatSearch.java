package cn.mulanbay.pms.web.bean.request.health;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.CrossType;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;
import cn.mulanbay.pms.web.bean.request.GroupType;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/31.
 */
public class TreatRecordAnalyseStatSearch extends QueryBulider implements DateStatSearch ,BindUser {


    @Query(fieldName = "treat_date", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "treat_date", op = Parameter.Operator.LTE)
    private Date endDate;

    // 支持多个字段查询
    @Query(fieldName = "hospital,department,organ,disease,diagnosed_disease", op = Parameter.Operator.LIKE,crossType = CrossType.OR)
    private String name;

    @Query(fieldName = "is_sick", op = Parameter.Operator.EQ)
    private Boolean sick;

    //分组字段
    private String groupField;

    //需要统计的费用字段
    private String feeField;

    private GroupType groupType;

    private ChartType chartType;

    @Query(fieldName = "user_id", op = Parameter.Operator.EQ)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSick() {
        return sick;
    }

    public void setSick(Boolean sick) {
        this.sick = sick;
    }

    public String getGroupField() {
        return groupField;
    }

    public void setGroupField(String groupField) {
        this.groupField = groupField;
    }

    public String getFeeField() {
        return feeField;
    }

    public void setFeeField(String feeField) {
        this.feeField = feeField;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }
}
