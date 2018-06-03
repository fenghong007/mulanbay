package cn.mulanbay.pms.web.bean.request.buy;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.persistent.query.CrossType;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Parameter.Operator;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.pms.persistent.domain.BuyRecord;
import cn.mulanbay.pms.persistent.enums.MoneyFlow;
import cn.mulanbay.web.bean.request.PageSearch;

import java.util.Date;

public class BuyRecordSearch extends PageSearch implements BindUser,FullEndDateTime {

	@Query(fieldName = "goodsType.id", op = Operator.EQ)
	private Integer goodsType;

	@Query(fieldName = "subGoodsType.id", op = Parameter.Operator.EQ)
	private Integer subGoodsType;

	@Query(fieldName = "buyType.id", op = Operator.EQ)
	private Integer buyType;
	
	@Query(fieldName = "goodsName,keywords,shopName,remark", op = Operator.LIKE,crossType = CrossType.OR)
	private String name;

	@Query(fieldName = "buyDate", op = Operator.GTE)
	private Date startDate;

	@Query(fieldName = "buyDate", op = Operator.LTE)
	private Date endDate;
	
	@Query(fieldName = "status", op = Operator.EQ)
	private BuyRecord.Status status;
	
	@Query(fieldName = "secondhand", op = Operator.EQ)
	private Boolean secondhand;

	@Query(fieldName = "userId", op = Operator.EQ)
	public Long userId;

	private MoneyFlow moneyFlow;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	private String sortField;
	
	private String sortType;
	
	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public Integer getSubGoodsType() {
		return subGoodsType;
	}

	public void setSubGoodsType(Integer subGoodsType) {
		this.subGoodsType = subGoodsType;
	}

	public Integer getBuyType() {
		return buyType;
	}

	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
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

	public BuyRecord.Status getStatus() {
		return status;
	}

	public void setStatus(BuyRecord.Status status) {
		this.status = status;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public Boolean getSecondhand() {
		return secondhand;
	}

	public void setSecondhand(Boolean secondhand) {
		this.secondhand = secondhand;
	}

	public MoneyFlow getMoneyFlow() {
		return moneyFlow;
	}

	public void setMoneyFlow(MoneyFlow moneyFlow) {
		this.moneyFlow = moneyFlow;
	}
}
