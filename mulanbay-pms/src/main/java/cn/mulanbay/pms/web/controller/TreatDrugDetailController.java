package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.domain.TreatDrug;
import cn.mulanbay.pms.persistent.domain.TreatDrugDetail;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.UserBehaviorType;
import cn.mulanbay.pms.persistent.service.TreatService;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.health.TreatDrugDetailCalandarStat;
import cn.mulanbay.pms.web.bean.request.health.TreatDrugDetailFormRequest;
import cn.mulanbay.pms.web.bean.request.health.TreatDrugDetailSearch;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarData;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarPieData;
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
 * Created by fenghong
 * 用药详细记录
 */
@Controller
@RequestMapping("/treatDrugDetail")
public class TreatDrugDetailController extends BaseController  {

    private static Class<TreatDrugDetail> beanClass = TreatDrugDetail.class;

    @Autowired
    TreatService treatService;

    @RequestMapping(value = "list")
    public String list() {
        return "health/treatDrugDetailList";
    }

    /**
     * 获取列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(TreatDrugDetailSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("createdTime", Sort.DESC);
        pr.addSort(s);
        PageResult<TreatDrugDetail> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid TreatDrugDetailFormRequest formRequest ) {
        TreatDrugDetail bean = new TreatDrugDetail();
        BeanCopy.copyProperties(formRequest,bean);
        TreatDrug treatDrug = this.getUserEntity(TreatDrug.class,formRequest.getTreatDrugId(),formRequest.getUserId());
        bean.setTreatDrug(treatDrug);
        bean.setCreatedTime(new Date());
        checkTreatDrugDetail(bean);
        treatService.saveOrUpdateTreatDrugDetail(bean);
        return callback(bean);
    }

    private void checkTreatDrugDetail(TreatDrugDetail bean){
        if(bean.getOccurTime().after(bean.getCreatedTime())){
            throw new ApplicationException(PmsErrorCode.TREAT_DRUG_DETAIL_OCCURTIME_INCORRECT);
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
        TreatDrugDetail bean = this.getUserEntity(beanClass,getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid TreatDrugDetailFormRequest formRequest) {
        TreatDrugDetail bean = this.getUserEntity(beanClass,formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        TreatDrug treatDrug = this.getUserEntity(TreatDrug.class,formRequest.getTreatDrugId(),formRequest.getUserId());
        bean.setTreatDrug(treatDrug);
        bean.setLastModifyTime(new Date());
        checkTreatDrugDetail(bean);
        treatService.saveOrUpdateTreatDrugDetail(bean);
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
        this.deleteUserEntity(beanClass,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/calandarStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean calandarStat(TreatDrugDetailCalandarStat sf) {
        TreatDrug treatDrug =baseService.getObject(TreatDrug.class,sf.getTreatDrugId());
        TreatDrugDetailSearch search = new TreatDrugDetailSearch();
        if(sf.getDateGroupType()==null||sf.getDateGroupType()== DateGroupType.YEAR){
            Date startDate = DateUtil.getDate(sf.getYear()+"-01-01 00:00:00",DateUtil.Format24Datetime);
            Date endDate = DateUtil.getDate(sf.getYear()+"-12-31 23:59:59",DateUtil.Format24Datetime);
            search.setStartDate(startDate);
            search.setEndDate(endDate);
        }else {
            Date date = DateUtil.getDate(sf.getYear()+"-"+sf.getMonth()+"-01",DateUtil.FormatDay1);
            Date startDate = DateUtil.getFirstDayOfMonth(date);
            String ed = DateUtil.getFormatDate(DateUtil.getLastDayOfMonth(date),DateUtil.FormatDay1);
            Date endDate = DateUtil.getDate(ed+" 23:59:59",DateUtil.Format24Datetime);
            search.setStartDate(startDate);
            search.setEndDate(endDate);
        }
        search.setUserId(sf.getUserId());
        search.setTreatDrugId(sf.getTreatDrugId());
        PageRequest pr = search.buildQuery();
        pr.setPage(-1);
        pr.setBeanClass(beanClass);
        Sort s = new Sort("createdTime", Sort.ASC);
        pr.addSort(s);
        List<TreatDrugDetail> list = baseService.getBeanList(pr);
        if(sf.getDateGroupType()==null||sf.getDateGroupType()== DateGroupType.YEAR){
            return callback(this.createYearCalandarData(list,sf,treatDrug));
        }else{
            return callback(this.createMonthCalandarData(list,sf.getYear(),sf.getMonth(),treatDrug));
        }
    }

    private ChartCalandarPieData createMonthCalandarData(List<TreatDrugDetail> list , int year,String month, TreatDrug treatDrug){
        ChartCalandarPieData pieData = new ChartCalandarPieData(UserBehaviorType.HEALTH);
        pieData.setTitle(year+"-"+month+"用户行为分析");
        pieData.setStartDate(DateUtil.getDate(year+"-"+month+"-01",DateUtil.FormatDay1));

        for(TreatDrugDetail stat :list){
            String name="";
            if(treatDrug.getPerDay()>1){
                name="吃药";
            }else{
                int perTimes = treatDrug.getPerTimes();
                if(perTimes==1){
                    name="吃药";
                }else{
                    //获取小时数
                    int occurHours = Integer.valueOf(DateUtil.getFormatDate(stat.getOccurTime(),"HH"));
                    if(perTimes==2){
                        if(occurHours<12){
                            name="早";
                        }else{
                            name="晚";
                        }
                    }else if(perTimes==3){
                        if(occurHours<=9){
                            name="早";
                        }else if(occurHours<15){
                            name="中";
                        }else{
                            name="晚";
                        }
                    }else {
                        name="吃药点"+occurHours/4;
                    }
                }

            }
            pieData.addData(DateUtil.getFormatDate(stat.getOccurTime(),DateUtil.FormatDay1),name,1,false,1);
        }
        return pieData;
    }

        /**
         * 获取年的统计
         * @param list
         * @param sf
         * @param treatDrug
         * @return
         */
    private ChartCalandarData createYearCalandarData(List<TreatDrugDetail> list ,TreatDrugDetailCalandarStat sf,TreatDrug treatDrug){
        ChartCalandarData chartData = new ChartCalandarData();
        if(treatDrug.getPerDay()==1){
            //只有每天用的才有用
            chartData.setCustomData(treatDrug.getPerTimes());
        }
        chartData.setTitle(sf.getYear()+"年用药分析");
        chartData.setCount(list.size());
        chartData.setYear(sf.getYear());
        chartData.setLegendData("用药",3);
        chartData.setUnit("次");
        chartData.setCount(list.size());
        for(TreatDrugDetail stat : list){
            //这时value就是天数值
            chartData.addSerieData(stat.getOccurTime(),1);
        }
        return chartData;
    }

}
