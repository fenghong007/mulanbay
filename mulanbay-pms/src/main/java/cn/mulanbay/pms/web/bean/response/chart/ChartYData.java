package cn.mulanbay.pms.web.bean.response.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/7.
 * y轴数据
 */
public class ChartYData {

    private String name;

    private List<Object> data= new ArrayList<>();

    public ChartYData() {
    }

    public ChartYData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
