package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by fenghong on 2017/2/5.
 * 购买记录的关键字统计
 */
public class BuyRecordKeywordsStat{

    private String keywords;

    private BigInteger totalCount;

    // 价格，精确到元，可以为运费、总价
    private BigDecimal totalPrice;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
