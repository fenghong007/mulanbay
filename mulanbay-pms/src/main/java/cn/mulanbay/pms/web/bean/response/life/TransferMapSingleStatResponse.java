package cn.mulanbay.pms.web.bean.response.life;

import cn.mulanbay.pms.persistent.bean.TransferMapStat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/26.
 * 迁徙地图单向数据封装
 */
public class TransferMapSingleStatResponse extends TransferMapStatResponse{

    private List<String> legendData= new ArrayList<>();

    private List<List<TransferMapStat>> statData = new ArrayList<>();

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<List<TransferMapStat>> getStatData() {
        return statData;
    }

    public void setStatData(List<List<TransferMapStat>> statData) {
        this.statData = statData;
    }

}
