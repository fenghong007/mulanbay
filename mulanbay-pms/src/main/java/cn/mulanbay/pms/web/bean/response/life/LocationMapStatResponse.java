package cn.mulanbay.pms.web.bean.response.life;

import com.github.abel533.echarts.data.MapData;

import java.util.List;
import java.util.Map;

/**
 * Created by fenghong on 2017/1/18.
 */
public class LocationMapStatResponse {

    private int min=0;
    private int max;
    private List<MapData> dataList;
    private Map<String,double[]> geoCoordMapData;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public List<MapData> getDataList() {
        return dataList;
    }

    public void setDataList(List<MapData> dataList) {
        this.dataList = dataList;
    }

    public Map<String, double[]> getGeoCoordMapData() {
        return geoCoordMapData;
    }

    public void setGeoCoordMapData(Map<String, double[]> geoCoordMapData) {
        this.geoCoordMapData = geoCoordMapData;
    }
}
