package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2017/2/5.
 * 时间分组类型
 */
public enum DateGroupType {
    MINUTE("分钟"),HOUR("点"),HOURMINUTE("时分"),DAY("天"),WEEK("周"),MONTH("月"),YEAR("年"),DAYOFMONTH("号"),DAYOFWEEK("(周号)"), YEARMONTH("年月"),DAYCALENDAR("日历");

    private String name;

    DateGroupType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
