package cn.mulanbay.pms.persistent.bean;

/**
 * Created by fenghong on 2017/1/1.
 */
public class BuyRecordRealTimeStat {

    private Integer id;
    private String name;
    private double value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
