package cn.mulanbay.pms.web.bean.response.chart;

/**
 * Created by fenghong on 2017/10/3.
 */
public class ChartCalandarCompareRowData {

    private String targetDate;

    private String sourceDate;

    private int days=-1;

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
