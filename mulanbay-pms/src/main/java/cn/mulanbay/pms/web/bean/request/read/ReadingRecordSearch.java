package cn.mulanbay.pms.web.bean.request.read;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.pms.persistent.domain.ReadingRecord;
import cn.mulanbay.web.bean.request.PageSearch;

import java.util.Date;

/**
 * Created by fenghong on 2017/2/1.
 */
public class ReadingRecordSearch extends PageSearch implements BindUser,FullEndDateTime {

    @Query(fieldName = "bookName", op = Parameter.Operator.LIKE)
    private String name;

    private String dateQueryType;

    @Query(fieldName = "finishedDate", op = Parameter.Operator.GTE,referFieldName = "dateQueryType")
    private Date startDate;

    @Query(fieldName = "finishedDate", op = Parameter.Operator.LTE,referFieldName = "dateQueryType")
    private Date endDate;

    @Query(fieldName = "userId", op = Parameter.Operator.EQ)
    public Long userId;

    @Query(fieldName = "status", op = Parameter.Operator.EQ)
    private ReadingRecord.ReadingStatus status;

    @Query(fieldName = "bookCategory.id", op = Parameter.Operator.EQ)
    private Long bookCategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateQueryType() {
        return dateQueryType;
    }

    public void setDateQueryType(String dateQueryType) {
        this.dateQueryType = dateQueryType;
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

    public ReadingRecord.ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingRecord.ReadingStatus status) {
        this.status = status;
    }

    public Long getBookCategoryId() {
        return bookCategoryId;
    }

    public void setBookCategoryId(Long bookCategoryId) {
        this.bookCategoryId = bookCategoryId;
    }
}
