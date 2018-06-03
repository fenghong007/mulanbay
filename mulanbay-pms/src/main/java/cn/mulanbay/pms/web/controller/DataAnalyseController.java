package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.pms.persistent.bean.AfterEightHourStat;
import cn.mulanbay.pms.persistent.service.DataService;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.GroupType;
import cn.mulanbay.pms.web.bean.request.data.AfterEightHourAnalyseStatSearch;
import cn.mulanbay.pms.web.bean.response.chart.*;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by fenghong on 2017/1/21.
 */
@Controller
@RequestMapping("/dataAnalyse")
public class DataAnalyseController extends BaseController {

    @Autowired
    DataService dataService;

    /**
     * 八小时外分析
     * @return
     */
    @RequestMapping(value = "afterEightHourAnalyseStatList")
    public String afterEightHourAnalyseStatList() {
        return "data/afterEightHourAnalyseStatList";
    }

    /**
     * @return
     */
    @RequestMapping(value = "/afterEightHourAnalyseStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean afterEightHourAnalyseStat(AfterEightHourAnalyseStatSearch sf) {
        List<AfterEightHourStat> list = dataService.statAfterEightHour(sf);
        if(sf.getChartType()== ChartType.PIE){
            return callback(this.createAfterEightHourAnalyseStatPieData(list,sf));
        }else{
            return callback(this.createAfterEightHourAnalyseStatScatterData(list,sf));
        }

    }

    private ScatterChartData createAfterEightHourAnalyseStatScatterData(List<AfterEightHourStat> list,AfterEightHourAnalyseStatSearch sf){
        ScatterChartData chartData = new ScatterChartData();
        chartData.setTitle("八小时之外统计");
        for(AfterEightHourStat stat : list){
            ScatterChartDetailData detailData = chartData.findDetailDataByName(stat.getName());
            if(detailData==null){
                detailData = new ScatterChartDetailData();
                if(sf.getDataGroup()== AfterEightHourAnalyseStatSearch.DataGroup.DETAIL){
                    detailData.setName(stat.getName());
                }else {
                    detailData.setName(stat.getGroupName());
                }
                chartData.addSeriesData(detailData);
                chartData.addLegent(stat.getName());
            }
            detailData.addData(new Object[]{stat.getIndexValue(),DateUtil.minutesToHours(stat.getTotalTime().doubleValue()),stat.getTotalCountValue()});
            chartData.valueCompare(stat.getTotalCountValue());
        }
        return chartData;
    }

    private ChartPieData createAfterEightHourAnalyseStatPieData(List<AfterEightHourStat> list,AfterEightHourAnalyseStatSearch sf){
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("八小时外统计");
        chartPieData.setSubTitle(this.getDateTitle(sf));
        ChartPieSerieData serieData = new ChartPieSerieData();
        if(sf.getType()== GroupType.COUNT){
            serieData.setName("次数(次)");
        }else{
            serieData.setName("花费时间(小时)");
        }
        for (AfterEightHourStat mp : list){
            chartPieData.getXdata().add(mp.getName());
            ChartPieSerieDetailData cp = new ChartPieSerieDetailData();
            if(sf.getDataGroup()== AfterEightHourAnalyseStatSearch.DataGroup.DETAIL){
                cp.setName(mp.getName());
            }else {
                cp.setName(mp.getGroupName());
            }
            if(sf.getType()== GroupType.COUNT){
                cp.setValue(mp.getTotalCountValue());
            }else{
                cp.setValue(DateUtil.minutesToHours(mp.getTotalTime().doubleValue()));
            }
            serieData.getData().add(cp);
        }
        chartPieData.getDetailData().add(serieData);
        return chartPieData;
    }

}
