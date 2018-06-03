package cn.mulanbay.pms.web.bean.request.buy;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter.Operator;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.web.bean.request.PageSearch;

import java.util.Date;

/**
 * 这里采用的sql查询
 */
public class BuyRecordKeywordsSearch extends PageSearch implements BindUser{

	@Query(fieldName = "buy_date", op = Operator.GTE)
	private Date startDate;

	@Query(fieldName = "buy_date", op = Operator.LTE)
	private Date endDate;
	
	@Query(fieldName = "user_id", op = Operator.EQ)
	private Long userId;

	private Boolean needRoot;

	//默认只查询最新的一周内
	private int days=7;

	@Override
	public Long getUserId() {
		return userId;
	}

	@Override
	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Boolean getNeedRoot() {
		return needRoot;
	}

	public void setNeedRoot(Boolean needRoot) {
		this.needRoot = needRoot;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
}
