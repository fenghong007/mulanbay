package cn.mulanbay.pms.web.bean.response.life;

import java.util.Map;

/**
 * Created by fenghong on 2017/2/26.
 * 迁徙地图数据封装
 */
public class TransferMapStatResponse {

    //标题
    private String title;

    //子标题
    private String subTitle;

    private Map<String,double[]> geoCoordMapData;


    public Map<String, double[]> getGeoCoordMapData() {
        return geoCoordMapData;
    }

    public void setGeoCoordMapData(Map<String, double[]> geoCoordMapData) {
        this.geoCoordMapData = geoCoordMapData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

}
