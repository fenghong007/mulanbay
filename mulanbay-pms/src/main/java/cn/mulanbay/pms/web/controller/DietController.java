package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.persistent.bean.DietAnalyseStat;
import cn.mulanbay.pms.persistent.domain.Diet;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.UserBehaviorType;
import cn.mulanbay.pms.persistent.service.DietService;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.diet.*;
import cn.mulanbay.pms.web.bean.response.chart.*;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/21.
 */
@Controller
@RequestMapping("/diet")
public class DietController extends BaseController {
    

    private static Class<Diet> beanClass = Diet.class;

    @Autowired
    DietService dietService;

    @Autowired
    CacheHandler cacheHandler;

    @RequestMapping(value = "list")
    public String list() {
        return "diet/dietList";
    }

    /**
     * 获取任务列表
     * 使用RequestParam 方式是因为easyui 的datagrid使用ajax请求时对于list多值类型的参数参数名会带上中挂号
     * 且无法在js请求方式里设置使用 traditional:true参数
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(DietSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("occurTime", Sort.DESC);
        pr.addSort(s);
        PageResult<Diet> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid DietFormRequest formRequest) {
        Diet bean = new Diet();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
        String key = "userLastDiet:"+formRequest.getDietType()+":"+formRequest.getDietSource()+":"+formRequest.getUserId();
        //缓存上一次的
        cacheHandler.set(key,bean,48*3600);
        return callback(null);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        Diet bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 获取最后的地点
     *
     * @return
     */
    @RequestMapping(value = "/getLastLocation", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getLastLocation() {
        Long userId = this.getCurrentUserId();
        String location = cacheHandler.getForString("dietLastLocation:"+userId);
        return callback(location);
    }

    /**
     * 获取最后的饮食
     *
     * @return
     */
    @RequestMapping(value = "/getLastDiet", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getLastDiet(@Valid LastDietRequest dietRequest) {
        String key = "userLastDiet:"+dietRequest.getDietType()+":"+dietRequest.getDietSource()+":"+dietRequest.getUserId();
        Diet diet = cacheHandler.get(key,Diet.class);
        return callback(diet);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid DietFormRequest formRequest) {
        Diet bean=this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setLastModifyTime(new Date());
        baseService.updateObject(bean);
        return callback(null);
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        this.deleteUserEntity(beanClass,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    @RequestMapping(value = "statList")
    public String statList() {
        return "diet/dietStatList";
    }


    /**
     * 用户行为统计（带饼图分析）
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(@Valid DietStatSearch sf) {
        if(sf.getDateGroupType()== DateGroupType.MONTH){
            return callback(this.createMonthStat(sf));
        }else {
            return callback(this.createYearStat(sf));
        }
    }

    private ChartCalandarPieData createMonthStat(DietStatSearch sf){
        Date date = DateUtil.getDate(sf.getYear()+"-"+sf.getMonth()+"-01",DateUtil.FormatDay1);
        Date startTime = DateUtil.getFirstDayOfMonth(date);
        Date endDate = DateUtil.getLastDayOfMonth(date);
        Date endTime =DateUtil.getTodayTillMiddleNightDate(endDate);
        ChartCalandarPieData pieData = new ChartCalandarPieData(UserBehaviorType.LIFE);
        String monthString = DateUtil.getFormatDate(startTime,"yyyyMM");
        pieData.setTitle(monthString+"饮食习惯分析");
        pieData.setStartDate(startTime);
        List<Diet> list = dietService.getDietList(startTime,endTime,sf.getUserId(),sf.getDietSource(),sf.getDietType());
        for(Diet stat :list){
            String dd = DateUtil.getFormatDate(stat.getOccurTime(),DateUtil.FormatDay1);
            pieData.addData(dd,stat.getDietType().getName(),1,false,1);
        }
        return pieData;
    }

    private ChartCalandarCompareData createYearStat(DietStatSearch sf){
        if(sf.getDietType()==null){
            throw new ApplicationException(PmsErrorCode.DIET_TYPE_NULL);
        }
        Date startTime = DateUtil.getDate(sf.getYear()+"-01-01 00:00:00",DateUtil.Format24Datetime);
        Date endTime = DateUtil.getDate(sf.getYear()+"-12-31 23:59:59",DateUtil.Format24Datetime);

        ChartCalandarCompareData chartData = new ChartCalandarCompareData();
        chartData.setTitle(sf.getYear()+"饮食习惯分析");
        chartData.setUnit("次");
        chartData.setYear(sf.getYear());
        List<Diet> list = dietService.getDietList(startTime,endTime,sf.getUserId(),sf.getDietSource(),sf.getDietType());
        Date beginFlag = list.get(0).getOccurTime();
        for(Diet stat : list){
            String dd = DateUtil.getFormatDate(stat.getOccurTime(),DateUtil.FormatDay1);
            chartData.addSerieData(dd,1,false,1,1);
            addMissDate(beginFlag,stat.getOccurTime(),chartData);
            beginFlag = stat.getOccurTime();
        }
        String s1= sf.getDietType().getName();
        String s2= "没吃"+sf.getDietType().getName();
        chartData.setLegendData(new String[]{s1,s2});

        return chartData;
    }

    /**
     * 统计没吃某餐的数据
     * @param start
     * @param end
     * @param chartData
     */
    private void addMissDate(Date start,Date end,ChartCalandarCompareData chartData){
        Date startDate = DateUtil.getDate(start,DateUtil.FormatDay1);
        Date endDate = DateUtil.getDate(end,DateUtil.FormatDay1);
        int n = DateUtil.getIntervalDays(startDate,endDate);
        if(n<=1){
            return;
        }else{
            for(int i=2;i<=n;i++){
                String dd = DateUtil.getFormatDate(DateUtil.getDate((i-1),startDate),DateUtil.FormatDay1);
                chartData.addSerieData(dd,1,false,1,2);
            }
        }
    }

    /**
     * 分析
     *
     * @return
     */
    @RequestMapping(value = "/analyse", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean analyse(@Valid DietAnalyseSearch sf) {
        List<DietAnalyseStat> list = dietService.statDietAnalyse(sf);
        if(sf.getChartType()== ChartType.PIE){
            return callback(this.createAnalyseStatPieData(list,sf));
        }else{
            return callback(this.createAnalyseStatBarData(list,sf));
        }
    }

    /**
     * 封装消费分析的饼状图数据
     *
     * @param list
     * @param sf
     * @return
     */
    private ChartPieData createAnalyseStatPieData(List<DietAnalyseStat> list, DietAnalyseSearch sf) {
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("饮食分析");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName("食物");
        //总的值
        for (DietAnalyseStat bean : list) {
            chartPieData.getXdata().add(bean.getName());
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(bean.getName());
            dataDetail.setValue(bean.getTotalCount().longValue());
            serieData.getData().add(dataDetail);
        }
        String subTitle = this.getDateTitle(sf);
        chartPieData.setSubTitle(subTitle);
        chartPieData.getDetailData().add(serieData);
        return chartPieData;
    }

    /**
     * 封装消费记录分析的柱状图数据
     *
     * @param list
     * @param sf
     * @return
     */
    private ChartData createAnalyseStatBarData(List<DietAnalyseStat> list, DietAnalyseSearch sf) {
        ChartData chartData = new ChartData();
        chartData.setTitle("饮食分析");
        chartData.setLegendData(new String[]{"食物"});
        ChartYData yData = new ChartYData();
        yData.setName("食物");
        for (DietAnalyseStat bean : list) {
            chartData.getXdata().add(bean.getName());
            yData.getData().add(bean.getTotalCount().longValue());
        }
        String subTitle = this.getDateTitle(sf);
        chartData.setSubTitle(subTitle);
        chartData.getYdata().add(yData);
        return chartData;

    }
}
