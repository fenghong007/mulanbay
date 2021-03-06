package cn.mulanbay.pms.web.bean.response.chart;

import cn.mulanbay.pms.persistent.bean.DateStat;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.util.ChartUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/7.
 * 图表数据
 */
public class ChartData extends BaseChartData {

    //统计分类
    private String[] legendData;

    private List<Integer> intXData = new ArrayList<>();

    //x轴数据
    private List<String> xdata= new ArrayList<>();

    //y轴数据ChartData
    private List<ChartYData> ydata = new ArrayList<>();

    public String[] getLegendData() {
        return legendData;
    }

    public void setLegendData(String[] legendData) {
        this.legendData = legendData;
    }

    public List<Integer> getIntXData() {
        return intXData;
    }

    public void setIntXData(List<Integer> intXData) {
        this.intXData = intXData;
    }

    public List<String> getXdata() {
        return xdata;
    }

    public void setXdata(List<String> xdata) {
        this.xdata = xdata;
    }

    public List<ChartYData> getYdata() {
        return ydata;
    }

    public void setYdata(List<ChartYData> ydata) {
        this.ydata = ydata;
    }

    /**
     * 添加X轴的数据
     * @param dateStat
     * @param dateGroupType
     */
    public void addXData(DateStat dateStat, DateGroupType dateGroupType){
        this.getIntXData().add(dateStat.getIndexValue());
        this.getXdata().add(ChartUtil.getStringXdata(dateGroupType,dateStat.getIndexValue()));
    }

}
