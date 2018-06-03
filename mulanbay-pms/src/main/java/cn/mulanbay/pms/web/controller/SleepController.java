package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.SleepAnalyseStat;
import cn.mulanbay.pms.persistent.domain.Sleep;
import cn.mulanbay.pms.persistent.service.SleepService;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.sleep.SleepAnalyseStatSearch;
import cn.mulanbay.pms.web.bean.request.sleep.SleepFormRequest;
import cn.mulanbay.pms.web.bean.request.sleep.SleepSearch;
import cn.mulanbay.pms.web.bean.response.chart.ScatterChartData;
import cn.mulanbay.pms.web.bean.response.chart.ScatterChartDetailData;
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
 * Created by fenghong on 2017/6/5.
 */
@Controller
@RequestMapping("/sleep")
public class SleepController extends BaseController {

    private static Class<Sleep> beanClass = Sleep.class;

    @Autowired
    SleepService sleepService;

    @RequestMapping(value = "list")
    public String list() {
        return "sleep/sleepList";
    }


    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(SleepSearch sf) {
        return callbackDataGrid(getSleepResult(sf));
    }

    private PageResult<Sleep> getSleepResult(SleepSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("sleepDate",Sort.DESC);
        pr.addSort(sort);
        PageResult<Sleep> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid SleepFormRequest formRequest) {
        Sleep bean = new Sleep();
        BeanCopy.copyProperties(formRequest,bean);
        Date sleepDate = calSleepDate(bean.getSleepTime());
        bean.setSleepDate(sleepDate);
        if(bean.getSleepTime()!=null&&bean.getGetUpTime()!=null){
            long n = bean.getGetUpTime().getTime()-bean.getSleepTime().getTime();
            bean.setTotalMinutes((int) (n/(1000*60)));
        }
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
        return callback(null);
    }

    /**
     * 计算睡眠日
     * @param date
     */
    private Date calSleepDate(Date date){
        if(date==null){
            return null;
        }else {
            String hour = DateUtil.getFormatDate(date,"HH");
            int n = Integer.valueOf(hour);
            if(n>=12){
                //当天
                return DateUtil.getDate(0,date);
            }else{
                //昨天
                return DateUtil.getDate(-1,date);
            }
        }
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        Sleep bean=this.getUserEntity(Sleep.class, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid SleepFormRequest formRequest) {
        Sleep bean=this.getUserEntity(Sleep.class, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        Date sleepDate = calSleepDate(bean.getSleepTime());
        bean.setSleepDate(sleepDate);
        if(bean.getSleepTime()!=null&&bean.getGetUpTime()!=null){
            long n = bean.getGetUpTime().getTime()-bean.getSleepTime().getTime();
            bean.setTotalMinutes((int) (n/(1000*60)));
        }
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
        this.deleteUserEntity(Sleep.class,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    @RequestMapping(value = "/analyseSatList")
    public String analyseSatList() {
        return "sleep/sleepAnalyseSatList";
    }


    /**
     * 比对，采用散点图
     * @return
     */
    @RequestMapping(value = "/analyseSat")
    @ResponseBody
    public ResultBean analyseSat(@Valid SleepAnalyseStatSearch sf) {
        ScatterChartData chartData = new ScatterChartData();
        chartData.setTitle("睡眠分析");
        chartData.setxUnit(sf.getXgroupType().getName());
        chartData.setyUnit(sf.getYgroupType().getUnit());
        List<SleepAnalyseStat> list = sleepService.statSleepAnalyse(sf);
        chartData.addLegent(sf.getYgroupType().getName());
        ScatterChartDetailData detailData = new ScatterChartDetailData();
        detailData.setName(sf.getYgroupType().getName());
        double totalX=0;
        int n=0;
        for(SleepAnalyseStat stat : list){
            if(sf.getYgroupType()== SleepAnalyseStatSearch.SleepStatType.DURATION){
                double hours = stat.getyDoubleValue()/60;
                BigDecimal b = new BigDecimal(hours);
                double v  =  b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                detailData.addData(new Object[]{stat.getxDoubleValue(),v});
            }else{
                detailData.addData(new Object[]{stat.getxDoubleValue(),stat.getyDoubleValue()});

            }
            totalX+=stat.getxDoubleValue();
            n++;
        }
        detailData.setxAxisAverage(totalX/n);
        chartData.addSeriesData(detailData);
        return callback(chartData);
    }


}
