package cn.mulanbay.pms.web.bean.request.buy;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.FullEndDateTime;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.persistent.query.QueryBulider;
import cn.mulanbay.pms.persistent.enums.BuyRecordPriceType;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;

import java.util.Date;

/**
 * 这里采用的是sql查询
 * Created by fenghong on 2017/2/5.
 */
public class BuyRecordDateStatSearch extends QueryBulider implements DateStatSearch,BindUser,FullEndDateTime {

    @Query(fieldName = "buy_date", op = Parameter.Operator.GTE)
    private Date startDate;

    @Query(fieldName = "buy_date", op = Parameter.Operator.LTE)
    private Date endDate;

    @Query(fieldName = "user_id", op = Parameter.Operator.EQ)
    public Long userId;

    @Query(fieldName = "secondhand", op = Parameter.Operator.EQ)
    private Boolean secondhand;

    @Query(fieldName = "total_price", op = Parameter.Operator.GTE)
    private Double startTotalPrice;

    @Query(fieldName = "total_price", op = Parameter.Operator.LTE)
    private Double endTotalPrice;

    @Query(fieldName = "goods_type_id", op = Parameter.Operator.EQ)
    private Integer goodsType;

    @Query(fieldName = "buy_type_id", op = Parameter.Operator.EQ)
    private Integer buyType;

    @Query(fieldName = "sub_goods_type_id", op = Parameter.Operator.EQ)
    private Integer subGoodsType;

    private DateGroupType dateGroupType;

    private BuyRecordPriceType priceType;

    // 是否补全日期
    private Boolean compliteDate;

    private boolean useStatable;

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

    public Boolean getSecondhand() {
        return secondhand;
    }

    public void setSecondhand(Boolean secondhand) {
        this.secondhand = secondhand;
    }

    public Double getStartTotalPrice() {
        return startTotalPrice;
    }

    public void setStartTotalPrice(Double startTotalPrice) {
        this.startTotalPrice = startTotalPrice;
    }

    public Double getEndTotalPrice() {
        return endTotalPrice;
    }

    public void setEndTotalPrice(Double endTotalPrice) {
        this.endTotalPrice = endTotalPrice;
    }

    public Boolean getCompliteDate() {
        return compliteDate;
    }

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

    public DateGroupType getDateGroupType() {
        return dateGroupType;
    }

    public void setDateGroupType(DateGroupType dateGroupType) {
        this.dateGroupType = dateGroupType;
    }

    public BuyRecordPriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(BuyRecordPriceType priceType) {
        this.priceType = priceType;
    }

    public Boolean isCompliteDate() {
        return compliteDate;
    }

    public void setCompliteDate(Boolean compliteDate) {
        this.compliteDate = compliteDate;
    }

    public boolean isUseStatable() {
        return useStatable;
    }

    public void setUseStatable(boolean useStatable) {
        this.useStatable = useStatable;
    }
}
