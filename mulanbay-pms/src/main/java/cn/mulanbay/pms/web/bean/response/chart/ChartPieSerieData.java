package cn.mulanbay.pms.web.bean.response.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/14.
 * 饼图的数据
 */
public class ChartPieSerieData {

    private String name;

    private List<ChartPieSerieDetailData> data= new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChartPieSerieDetailData> getData() {
        return data;
    }

    public void setData(List<ChartPieSerieDetailData> data) {
        this.data = data;
    }
}
