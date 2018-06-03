package cn.mulanbay.pms.web.bean.request.dream;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/1/22.
 */
public class DreamStatListSearch extends QueryBulider implements DateStatSearch,BindUser {

    @Query(fieldName = "start_date", op = Parameter.Operator.GTE,referFieldName = "dateQueryType")
    private Date startDate;

    @Query(fieldName = "start_date", op = Parameter.Operator.LTE,referFieldName = "dateQueryType")
    private Date endDate;

    @Query(fieldName = "user_id", op = Parameter.Operator.EQ)
    public Long userId;

    private String dateQueryType;

    private GroupType groupType;

    private ChartType chartType;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getDateQueryType() {
        return dateQueryType;
    }

    public void setDateQueryType(String dateQueryType) {
        this.dateQueryType = dateQueryType;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public enum GroupType{
        STATUS("status"),DIFFICULTY("difficulty"),IMPORTANTLEVEL("important_level");

        private String column;

        GroupType(String column) {
            this.column = column;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }
    }
}
