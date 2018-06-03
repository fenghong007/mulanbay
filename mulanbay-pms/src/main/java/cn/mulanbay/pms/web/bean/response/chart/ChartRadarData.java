package cn.mulanbay.pms.web.bean.response.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/8/7.
 * 雷达图
 */
public class ChartRadarData extends BaseChartData {

    private List<String> legendData = new ArrayList<>();

    private List<ChartRadarIndicatorData> indicatorData = new ArrayList<>();

    private List<ChartRadarSerieData> series = new ArrayList<>();

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<ChartRadarIndicatorData> getIndicatorData() {
        return indicatorData;
    }

    public void setIndicatorData(List<ChartRadarIndicatorData> indicatorData) {
        this.indicatorData = indicatorData;
    }

    public List<ChartRadarSerieData> getSeries() {
        return series;
    }

    public void setSeries(List<ChartRadarSerieData> series) {
        this.series = series;
    }
}
