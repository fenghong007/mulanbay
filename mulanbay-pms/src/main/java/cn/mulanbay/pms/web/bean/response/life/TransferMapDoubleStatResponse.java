package cn.mulanbay.pms.web.bean.response.life;

import cn.mulanbay.pms.persistent.bean.TransferMapStat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/26.
 * 迁徙地图双向数据封装
 */
public class TransferMapDoubleStatResponse extends TransferMapStatResponse{

    private List<String> legendData= new ArrayList<>();

    private List<TransferMapStat> statData = new ArrayList<>();

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<TransferMapStat> getStatData() {
        return statData;
    }

    public void setStatData(List<TransferMapStat> statData) {
        this.statData = statData;
    }

}
