package cn.mulanbay.pms.persistent.bean;

import cn.mulanbay.pms.persistent.domain.ReportConfig;

/**
 * Created by fenghong on 2017/2/2.
 * 报表结果封装类
 */
public class ReportResult {

    //界面上做portal使用，从1开始连续
    private int id;

    private ReportConfig reportConfig;

    private String result;

    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
