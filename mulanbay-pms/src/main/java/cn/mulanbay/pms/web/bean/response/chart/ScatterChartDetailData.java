package cn.mulanbay.pms.web.bean.response.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/10/11.
 */
public class ScatterChartDetailData {

    private String name;

    private Object xAxisAverage;

    private List<Object[]> data = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getxAxisAverage() {
        return xAxisAverage;
    }

    public void setxAxisAverage(Object xAxisAverage) {
        this.xAxisAverage = xAxisAverage;
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    public void addData(Object[] os){
        data.add(os);
    }
}
