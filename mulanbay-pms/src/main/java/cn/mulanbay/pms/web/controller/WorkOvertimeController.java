package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/10.
 */

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.WorkOvertimeDateStat;
import cn.mulanbay.pms.persistent.bean.WorkOvertimeStat;
import cn.mulanbay.pms.persistent.domain.Company;
import cn.mulanbay.pms.persistent.domain.WorkOvertime;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.WorkService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.work.WorkOvertimeDateStatSearch;
import cn.mulanbay.pms.web.bean.request.work.WorkOvertimeFormRequest;
import cn.mulanbay.pms.web.bean.request.work.WorkOvertimeSearch;
import cn.mulanbay.pms.web.bean.request.work.WorkOvertimeStatSearch;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.pms.web.bean.response.chart.ChartYData;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 加班记录
 */
@Controller
@RequestMapping("/workOvertime")
public class WorkOvertimeController extends BaseController {

    private static Class<WorkOvertime> beanClass = WorkOvertime.class;

    @Autowired
    WorkService workService;

    @RequestMapping(value = "list")
    public String list() {
        return "work/workOvertimeList";
    }

    @Value("${work.days.of.week}")
    double weekWorkDays;

    @Value("${work.days.of.month}")
    double monthWorkDays;


    @Value("${work.days.of.year}")
    double yearWorkDays;

    /**
     * 获取加班记录列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(WorkOvertimeSearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("workDate", Sort.DESC);
        pr.addSort(s);
        PageResult<WorkOvertime> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid WorkOvertimeFormRequest formRequest ) {
        WorkOvertime bean = new WorkOvertime();
        BeanCopy.copyProperties(formRequest,bean);
        Company company = this.getUserEntity(Company.class,formRequest.getCompanyId(),formRequest.getUserId());
        bean.setCompany(company);
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
        return callback(null);
    }


    /**
     * 获取
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        WorkOvertime bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid WorkOvertimeFormRequest formRequest ) {
        WorkOvertime bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        Company company = this.getUserEntity(Company.class,formRequest.getCompanyId(),formRequest.getUserId());
        bean.setCompany(company);
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

    @RequestMapping(value = "/dateStatList")
    public String dateStatList() {
        return "work/workOvertimeDateStatList";
    }


    /**
     * 按照日期统计
     * @return
     */
    @RequestMapping(value = "/dateStat")
    @ResponseBody
    public ResultBean dateStat(WorkOvertimeDateStatSearch sf) {
        List<WorkOvertimeDateStat> list = workService.statDateWorkOvertime(sf);
        if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(ChartUtil.createChartCalandarData("回家统计","次数","次",sf,list));
        }
        ChartData chartData = new ChartData();
        chartData.setTitle("加班统计");
        chartData.setSubTitle(this.getDateTitle(sf));
        chartData.setLegendData(new String[]{"次数","总时长(小时)","平均每天加班(小时)"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        ChartYData yData2= new ChartYData();
        yData2.setName("总时长(小时)");
        ChartYData yData3= new ChartYData();
        yData3.setName("平均每天加班(小时)");
        for(WorkOvertimeDateStat bean : list){
            chartData.getIntXData().add(bean.getIndexValue());
            if(sf.getDateGroupType()== DateGroupType.MONTH){
                chartData.getXdata().add(bean.getIndexValue()+"月份");
                yData3.getData().add(getAverageHours(bean.getTotalHours().doubleValue(),monthWorkDays));
            }else if(sf.getDateGroupType()== DateGroupType.YEAR){
                chartData.getXdata().add(bean.getIndexValue()+"年");
                yData3.getData().add(getAverageHours(bean.getTotalHours().doubleValue(),yearWorkDays));
            }else if(sf.getDateGroupType()== DateGroupType.WEEK){
                chartData.getXdata().add("第"+bean.getIndexValue()+"周");
                yData3.getData().add(getAverageHours(bean.getTotalHours().doubleValue(),weekWorkDays));
            }else{
                chartData.getXdata().add(bean.getIndexValue().toString());
                yData3.getData().add(getAverageHours(bean.getTotalHours().doubleValue(),1.0));
            }
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalHours().doubleValue());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        chartData.getYdata().add(yData3);
        chartData = ChartUtil.completeDate(chartData,sf);
        return callback(chartData);
    }

    private double getAverageHours(double hours,double days){
        double l =hours/days;
        BigDecimal b = new BigDecimal(l);
        double value  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }


    /**
     * 总的概要统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(WorkOvertimeStatSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        WorkOvertimeStat data = workService.getWorkOvertimeStat(pr);
        if(sf.getStartDate()!=null&&sf.getEndDate()!=null){
            //只有都不为空平均加班时长才有效
            int days = DateUtil.getIntervalDays(sf.getStartDate(),sf.getEndDate());
            double b = getAverageHours(data.getTotalHours().doubleValue(),days);
            data.setAverageHours(BigDecimal.valueOf(b));
        }
        return callback(data);
    }
}
