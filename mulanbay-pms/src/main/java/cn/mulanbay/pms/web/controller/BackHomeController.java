package cn.mulanbay.pms.web.controller;

import cn.mulanbay.pms.persistent.bean.BackHomeDateStat;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.BuyRecordService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.web.bean.request.life.BackHomeDateStatSearch;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.pms.web.bean.response.chart.ChartYData;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by fenghong on 2017/5/31.
 * 回家统计
 */
@Controller
@RequestMapping("/backHome")
public class BackHomeController extends BaseController {


    @Value("${system.backhome.keywords}")
    String backhomeKeywords;

    @Autowired
    BuyRecordService buyRecordService;

    /**
     * 回家统计页面
     *
     * @return
     */
    @RequestMapping(value = "dateStatList")
    public String dateStatList() {
        return "life/backHomeDateStatList";
    }

    /**
     * 回家数据统计，基于BuyRecord中车票、火车票这些的购买记录
     *
     * @param sf
     * @return
     */
    @RequestMapping(value = "/dateStat")
    @ResponseBody
    public ResultBean dateStat(BackHomeDateStatSearch sf) {
        //根据回家关键字统计
        sf.setKeywords(backhomeKeywords);
        List<BackHomeDateStat> list = buyRecordService.statBackHomeByDate(sf);
        if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(ChartUtil.createChartCalandarData("回家统计","次数","次",sf,list));
        }
        ChartData chartData = new ChartData();
        chartData.setTitle("回家数据统计");
        chartData.setLegendData(new String[]{"回家次数","交通次数", "路费"});
        ChartYData yData1 = new ChartYData();
        yData1.setName("回家次数");
        ChartYData yData2 = new ChartYData();
        yData2.setName("交通次数");
        ChartYData yData3 = new ChartYData();
        yData3.setName("路费");
        BigDecimal totalCount = new BigDecimal(0);
        BigDecimal totalValue = new BigDecimal(0);
        for (BackHomeDateStat bean : list) {
            chartData.addXData(bean,sf.getDateGroupType());
            yData1.getData().add(Math.ceil(bean.getTotalCount().doubleValue()/2));
            yData2.getData().add(bean.getTotalCount());
            yData3.getData().add(bean.getTotalPrice());
            totalCount=totalCount.add(new BigDecimal(bean.getTotalCount()));
            totalValue=totalValue.add(bean.getTotalPrice());
        }
        String totalString = "回家"+Math.ceil(totalCount.doubleValue()/2)+"(次),交通"+totalCount.longValue()+"(次),路费"+totalValue.doubleValue()+"(元)";
        chartData.setSubTitle(this.getDateTitle(sf,totalString));
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        chartData.getYdata().add(yData3);
        chartData = ChartUtil.completeDate(chartData, sf);
        return callback(chartData);
    }
}
