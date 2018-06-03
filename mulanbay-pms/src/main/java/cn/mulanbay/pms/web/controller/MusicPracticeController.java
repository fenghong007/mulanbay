package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/12.
 */

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.*;
import cn.mulanbay.pms.persistent.domain.MusicInstrument;
import cn.mulanbay.pms.persistent.domain.MusicPractice;
import cn.mulanbay.pms.persistent.domain.MusicPracticeTune;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.MusicPracticeService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.util.Constant;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.GroupType;
import cn.mulanbay.pms.web.bean.request.music.*;
import cn.mulanbay.pms.web.bean.response.chart.*;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 音乐练习记录
 */
@Controller
@RequestMapping("/musicPractice")
public class MusicPracticeController extends BaseController{

    private static Class<MusicPractice> beanClass = MusicPractice.class;

    @Autowired
    MusicPracticeService musicPracticeService;

    @RequestMapping(value = "list")
    public String list() {
        return "music/musicPracticeList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(MusicPracticeSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("practiceDate", Sort.DESC);
        pr.addSort(s);
        PageResult<MusicPractice> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid MusicPracticeFormRequest formRequest) {
        MusicPractice bean = new MusicPractice();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCreatedTime(new Date());
        bean.setPracticeDate(DateUtil.getDate(bean.getPracticeStartTime(),DateUtil.FormatDay1));
        MusicInstrument musicInstrument=this.getUserEntity(MusicInstrument.class, formRequest.getMusicInstrumentId(),formRequest.getUserId());
        bean.setMusicInstrument(musicInstrument);
        baseService.saveObject(bean);
        return callback(bean);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        MusicPractice bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid MusicPracticeFormRequest formRequest) {
        MusicPractice bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setLastModifyTime(new Date());
        bean.setPracticeDate(DateUtil.getDate(bean.getPracticeStartTime(),DateUtil.FormatDay1));
        MusicInstrument musicInstrument=this.getUserEntity(MusicInstrument.class, formRequest.getMusicInstrumentId(),formRequest.getUserId());
        bean.setMusicInstrument(musicInstrument);
        baseService.updateObject(bean);
        return callback(bean);
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        Long[] ids = NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(","));
        for(Long id: ids){
            MusicPractice bean=this.getUserEntity(beanClass, id,deleteRequest.getUserId());
            musicPracticeService.deleteMusicPractice(bean);
        }
        return callback(null);
    }

    /**
     * 总的概要统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(MusicPracticeStatSearch sf){
        MusicPracticeSummaryStat data = musicPracticeService.musicPracticeSummaryStat(sf);
        if(sf.getStartDate()!=null&&sf.getEndDate()!=null){
            int days = DateUtil.getIntervalDays(sf.getStartDate(),sf.getEndDate());
            data.setAverageMinutes(NumberUtil.getAverageValue(data.getTotalMinutes().doubleValue(),days,2));
        }
        // 获取乐器的分析信息
        List<MusicPracticeInstrumentStat> list = musicPracticeService.musicPracticeInstrumentStat(sf);
        //统计医保、个人的支付比例
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("音乐练习统计");
        chartPieData.setSubTitle(this.getDateTitle(sf));
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName("音乐练习统计(小时)");
        ChartPieSerieData serieDataCount = new ChartPieSerieData();
        serieDataCount.setName("音乐练习统计(次)");
        for (MusicPracticeInstrumentStat mp : list){
            chartPieData.getXdata().add(mp.getName());
            ChartPieSerieDetailData cp = new ChartPieSerieDetailData();
            cp.setName(mp.getName());
            cp.setValue(minutesToHours(mp.getTotalMinutes().doubleValue()));
            serieData.getData().add(cp);

            ChartPieSerieDetailData cpCount = new ChartPieSerieDetailData();
            cpCount.setName(mp.getName());
            cpCount.setValue(mp.getTotalCount().longValue());
            serieDataCount.getData().add(cpCount);
        }
        chartPieData.getDetailData().add(serieDataCount);
        chartPieData.getDetailData().add(serieData);
        data.setPieData(chartPieData);
        return callback(data);
    }

    private double minutesToHours(double minutes){
        return DateUtil.minutesToHours(minutes);
    }

    @RequestMapping(value = "/dateStatList")
    public String dateStatList() {
        return "music/musicPracticeDateStatList";
    }


    /**
     * 按照日期统计
     * @return
     */
    @RequestMapping(value = "/dateStat")
    @ResponseBody
    public ResultBean dateStat(MusicPracticeDateStatSearch sf) {
        if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(createChartCalandarData(sf));
        }
        List<MusicPracticeDateStat> list = musicPracticeService.statDateMusicPractice(sf);
        ChartData chartData = new ChartData();
        chartData.setTitle(getChartTitle(sf.getMusicInstrumentId()));
        chartData.setLegendData(new String[]{"次数","总时长(小时)"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        ChartYData yData2= new ChartYData();
        yData2.setName("总时长(小时)");
        ChartYData yData3= new ChartYData();
        yData3.setName("平均每天(小时)");
        ChartYData yData4= new ChartYData();
        yData4.setName("平均每次(小时)");
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        BigDecimal totalValue = new BigDecimal(0);
        int year=DateUtil.getYear(sf.getEndDate()==null ? new Date() : sf.getEndDate());
        for(MusicPracticeDateStat bean : list){
            chartData.getIntXData().add(bean.getIndexValue());
            if(sf.getDateGroupType()== DateGroupType.MONTH){
                chartData.getXdata().add(bean.getIndexValue()+"月份");
                int days = DateUtil.getDayOfMonth(year,bean.getIndexValue()-1);
                yData3.getData().add(minutesToHours(bean.getTotalMinutes().longValue()/days));
            }else if(sf.getDateGroupType()== DateGroupType.YEAR){
                chartData.getXdata().add(bean.getIndexValue()+"年");
            }else if(sf.getDateGroupType()== DateGroupType.WEEK){
                chartData.getXdata().add("第"+bean.getIndexValue()+"周");
                yData3.getData().add(minutesToHours(bean.getTotalMinutes().longValue()/ Constant.DAYS_WEEK));
            }else{
                chartData.getXdata().add(bean.getIndexValue().toString());
            }
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(minutesToHours(bean.getTotalMinutes().doubleValue()));
            yData4.getData().add(minutesToHours(bean.getTotalMinutes().doubleValue()/bean.getTotalCount().intValue()));
            totalCount=totalCount.add(new BigDecimal(bean.getTotalCount()));
            totalValue=totalValue.add(bean.getTotalMinutes());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        if(sf.getDateGroupType()== DateGroupType.WEEK||sf.getDateGroupType()== DateGroupType.MONTH||sf.getDateGroupType()== DateGroupType.YEAR){
            //如果是周，计算每天锻炼值
            chartData.setLegendData(new String[]{"次数","总时长(小时)","平均每天(小时)","平均每次(小时)"});
            chartData.getYdata().add(yData3);
            chartData.getYdata().add(yData4);
        }
        String totalString = totalCount.longValue()+"(次),"+minutesToHours(totalValue.doubleValue())+"(小时)";
        chartData.setSubTitle(this.getDateTitle(sf,totalString));
        chartData = ChartUtil.completeDate(chartData,sf);
        return callback(chartData);
    }

    private ChartCalandarData createChartCalandarData(MusicPracticeDateStatSearch sf){
        List<MusicPracticeDateStat> list = musicPracticeService.statDateMusicPractice(sf);
        ChartCalandarData calandarData = ChartUtil.createChartCalandarData("音乐练习统计","练习时间","分钟",sf,list);
        if (!StringUtil.isEmpty(sf.getTune())) {
            MusicPracticeTuneByTunenameSearch mpts = new MusicPracticeTuneByTunenameSearch();
            BeanCopy.copyProperties(sf,mpts);
            PageRequest pr = mpts.buildQuery();
            pr.setBeanClass(MusicPracticeTune.class);
            List<MusicPracticeTune> dd = baseService.getBeanList(pr);
            //添加监控走势数据
            for(MusicPracticeTune tt : dd){
                calandarData.addGraph(tt.getMusicPractice().getPracticeDate(),tt.getTimes());
            }
        }else {
            calandarData.setTop(3);
        }
        return calandarData;
    }

    /**
     * 同期比对
     * @return
     */
    @RequestMapping(value = "/yoyStatList")
    public String yoyStatList() {
        return "music/musicPracticeYoyStatList";
    }

    /**
     * 按照日期统计
     * @return
     */
    @RequestMapping(value = "/yoyStat")
    @ResponseBody
    public ResultBean yoyStat(@Valid MusicPracticeYoyStatSearch sf) {
        ChartData chartData = initYoyCharData(sf,musicPracticeService.getMusicInstrumentName(sf.getMusicInstrumentId())+"练习统计同期对比",null);
        String[] legendData = new String[sf.getYears().size()];
        for(int i=0;i<sf.getYears().size();i++){
            legendData[i]=sf.getYears().get(i).toString();
            //数据,为了代码复用及统一，统计还是按照日期的统计
            MusicPracticeDateStatSearch dateSearch= new MusicPracticeDateStatSearch();
            dateSearch.setDateGroupType(sf.getDateGroupType());
            dateSearch.setStartDate(DateUtil.getDate(sf.getYears().get(i)+"-01-01",DateUtil.FormatDay1));
            dateSearch.setEndDate(DateUtil.getDate(sf.getYears().get(i)+"-12-31",DateUtil.FormatDay1));
            dateSearch.setUserId(sf.getUserId());
            ChartYData yData= new ChartYData();
            yData.setName(sf.getYears().get(i).toString());
            List<MusicPracticeDateStat> list = musicPracticeService.statDateMusicPractice(dateSearch);
            //临时内容，作为补全用
            ChartData temp = new ChartData();
            for(MusicPracticeDateStat bean : list){
                temp.addXData(bean,sf.getDateGroupType());
                if(sf.getGroupType()== GroupType.COUNT){
                    yData.getData().add(bean.getTotalCount());
                }else{
                    yData.getData().add(minutesToHours(bean.getTotalMinutes().doubleValue()));
                }
            }
            //临时内容，作为补全用
            temp.getYdata().add(yData);
            dateSearch.setCompliteDate(true);
            temp = ChartUtil.completeDate(temp,dateSearch);
            //设置到最终的结果集中
            chartData.getYdata().add(temp.getYdata().get(0));
        }
        chartData.setLegendData(legendData);

        return callback(chartData);
    }

    @RequestMapping(value = "/timeStatList")
    public String timeStatList() {
        return "music/musicPracticeTimeStatList";
    }


    /**
     * 按照时间统计（查看主要在哪个小时内练习，或者练习分钟数）
     * @return
     */
    @RequestMapping(value = "/timeStat")
    @ResponseBody
    public ResultBean timeStat(MusicPracticeTimeStatSearch sf) {
        List<MusicPracticeTimeStat> list = musicPracticeService.statTimeMusicPractice(sf);
        if(sf.getChartType()== ChartType.PIE){
            return createTimeStatPieData(list,sf);
        }else {
            return createTimeStatBarData(list,sf);
        }
    }

    /**
     * 获取时间统计的饼图数据
     * @param list
     * @param sf
     * @return
     */
    private ResultBean createTimeStatPieData(List<MusicPracticeTimeStat> list, MusicPracticeTimeStatSearch sf){
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle(this.getChartTitle(sf.getMusicInstrumentId()));
        ChartPieSerieData serieData = new ChartPieSerieData();
        if(sf.getDateGroupType()==DateGroupType.MINUTE){
            serieData.setName("练习时长");
        }else if(sf.getDateGroupType()==DateGroupType.HOUR){
            serieData.setName("时间点");
        }else if(sf.getDateGroupType()==DateGroupType.DAY){
            serieData.setName("天");
        }else if(sf.getDateGroupType()==DateGroupType.WEEK){
            serieData.setName("周");
        }
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for(MusicPracticeTimeStat bean : list) {
            chartPieData.getXdata().add(bean.getIndexValue().toString());
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(getTimeStatName(bean.getIndexValue(),sf.getDateGroupType()));
            dataDetail.setValue(bean.getTotalCount());
            serieData.getData().add(dataDetail);
            totalValue=totalValue.add(new BigDecimal(bean.getTotalCount()));
        }
        String subTitle = this.getDateTitle(sf,String.valueOf(totalValue.doubleValue()));
        chartPieData.setSubTitle(subTitle);
        chartPieData.getDetailData().add(serieData);
        return callback(chartPieData);
    }

    /**
     * 获取时间统计的柱状图数据
     * @param list
     * @param sf
     * @return
     */
    private ResultBean createTimeStatBarData(List<MusicPracticeTimeStat> list, MusicPracticeTimeStatSearch sf){
        ChartData chartData = new ChartData();
        chartData.setTitle(this.getChartTitle(sf.getMusicInstrumentId()));
        chartData.setLegendData(new String[]{"次数"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        int year=DateUtil.getYear(sf.getEndDate()==null ? new Date() : sf.getEndDate());
        for(MusicPracticeTimeStat bean : list){
            chartData.getIntXData().add(bean.getIndexValue());
            chartData.getXdata().add(getTimeStatName(bean.getIndexValue(),sf.getDateGroupType()));
            yData1.getData().add(bean.getTotalCount());
            totalValue=totalValue.add(new BigDecimal(bean.getTotalCount()));
        }
        chartData.getYdata().add(yData1);
        chartData = ChartUtil.completeDate(chartData,sf);
        String subTitle = this.getDateTitle(sf,String.valueOf(totalValue.doubleValue()));
        chartData.setSubTitle(subTitle);
        return callback(chartData);
    }

    /**
     * 获取时间统计的名称表示
     * @param dateGroupType
     * @return
     */
    private String getTimeStatName(Integer indexVaule,DateGroupType dateGroupType){
        return ChartUtil.getStringXdata(dateGroupType,indexVaule);
    }

    /**
     * 获取统计图表的表头
     * @param musicInstrumentId
     * @return
     */
    private String getChartTitle(Long musicInstrumentId){
        return musicPracticeService.getMusicInstrumentName(musicInstrumentId)+"练习统计";
    }

    @RequestMapping(value = "/compareStatList")
    public String compareStatList() {
        return "music/musicPracticeCompareStatList";
    }


    /**
     * 比对，采用散点图
     * @return
     */
    @RequestMapping(value = "/compareStat")
    @ResponseBody
    public ResultBean compareStat(@Valid MusicPracticeCompareStatSearch sf) {
        ScatterChartData chartData = new ScatterChartData();
        chartData.setTitle("乐器练习比对");
        chartData.setxUnit(sf.getXgroupType().getName());
        chartData.setyUnit(sf.getYgroupType().getName());
        for(Long id : sf.getMusicInstrumentIds()){
            MusicInstrument mi = baseService.getObject(MusicInstrument.class,id);
            chartData.addLegent(mi.getName());
            List<MusicPracticeCompareStat> list = musicPracticeService.statCompareMusicPractice(sf,id);
            ScatterChartDetailData detailData = new ScatterChartDetailData();
            detailData.setName(mi.getName());
            double totalX=0;
            int n=0;
            for(MusicPracticeCompareStat stat : list){
                detailData.addData(new Object[]{stat.getxDoubleValue(),stat.getyDoubleValue()});
                totalX+=stat.getxDoubleValue();
                n++;
            }
            detailData.setxAxisAverage(totalX/n);
            chartData.addSeriesData(detailData);
        }
        return callback(chartData);
    }


}
