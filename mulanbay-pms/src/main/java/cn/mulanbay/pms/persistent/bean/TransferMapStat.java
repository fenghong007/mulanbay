package cn.mulanbay.pms.persistent.bean;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/2/26.
 */
public class TransferMapStat {

    private String startCity;

    private String arriveCity;

    private BigInteger totalCount;

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getArriveCity() {
        return arriveCity;
    }

    public void setArriveCity(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }
}
