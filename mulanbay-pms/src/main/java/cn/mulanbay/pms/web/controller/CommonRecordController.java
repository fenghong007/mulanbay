package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.CommonRecordDateStat;
import cn.mulanbay.pms.persistent.domain.CommonRecord;
import cn.mulanbay.pms.persistent.domain.CommonRecordType;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.DataService;
import cn.mulanbay.pms.persistent.service.TreatService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.commonrecord.CommonRecordDateStatSearch;
import cn.mulanbay.pms.web.bean.request.commonrecord.CommonRecordFormRequest;
import cn.mulanbay.pms.web.bean.request.commonrecord.CommonRecordSearch;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarData;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.pms.web.bean.response.chart.ChartYData;
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
 * Created by fenghong on 2017/2/2.
 * 通用记录
 */
@Controller
@RequestMapping("/commonRecord")
public class CommonRecordController extends BaseController {

    private static Class<CommonRecord> beanClass = CommonRecord.class;

    @Autowired
    TreatService treatService;

    @Autowired
    DataService dataService;

    @RequestMapping(value = "list")
    public String list() {
        return "data/commonRecordList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(CommonRecordSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort = new Sort("occurTime", Sort.DESC);
        pr.addSort(sort);
        PageResult<CommonRecord> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid CommonRecordFormRequest formRequest) {
        CommonRecordType commonRecordType = this.getUserEntity(CommonRecordType.class, formRequest.getCommonRecordTypeId(),formRequest.getUserId());
        CommonRecord bean = new CommonRecord();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCommonRecordType(commonRecordType);
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
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
        CommonRecord bean = this.getUserEntity(CommonRecord.class, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid CommonRecordFormRequest formRequest) {
        CommonRecordType commonRecordType = this.getUserEntity(CommonRecordType.class, formRequest.getCommonRecordTypeId(),formRequest.getUserId());
        CommonRecord bean = this.getUserEntity(CommonRecord.class, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCommonRecordType(commonRecordType);
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
        this.deleteUserEntity(CommonRecord.class,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }


    @RequestMapping(value = "/dateStatList")
    public String dateStatList() {
        return "data/commonRecordDateStatList";
    }


    /**
     * 按照日期统计
     * @return
     */
    @RequestMapping(value = "/dateStat")
    @ResponseBody
    public ResultBean dateStat(CommonRecordDateStatSearch sf) {
        List<CommonRecordDateStat> list = dataService.statDateCommonRecord(sf);
        if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(createChartCalandarDataDateStat(sf));
        }
        CommonRecordType crt = baseService.getObject(CommonRecordType.class,sf.getCommonRecordTypeId());
        ChartData chartData = creatBarDataDateStat(list,sf,crt);
        return callback(chartData);
    }

    /**
     * 柱状图、折线图数据统计
     * @param list
     * @param sf
     * @param crt
     * @return
     */
    private ChartData creatBarDataDateStat(List<CommonRecordDateStat> list ,CommonRecordDateStatSearch sf,CommonRecordType crt){
        ChartData chartData = new ChartData();
        chartData.setTitle("通用记录统计");
        chartData.setSubTitle(this.getDateTitle(sf));
        chartData.setLegendData(new String[]{"次数",crt.getUnit()});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        ChartYData yData2= new ChartYData();
        yData2.setName(crt.getUnit());
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        for(CommonRecordDateStat bean : list){
            chartData.addXData(bean,sf.getDateGroupType());
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalValue());
            totalCount = totalCount.add(new BigDecimal(bean.getTotalCount()));
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(sf, totalCount.longValue()+"次");
        chartData.setSubTitle(subTitle);
        chartData = ChartUtil.completeDate(chartData,sf);
        return chartData;
    }

    /**
     * 日历类型
     * @param sf
     * @return
     */
    private ChartCalandarData createChartCalandarDataDateStat(CommonRecordDateStatSearch sf){
        CommonRecordType crt = baseService.getObject(CommonRecordType.class,sf.getCommonRecordTypeId());
        List<CommonRecordDateStat> list = dataService.statDateCommonRecord(sf);
        ChartCalandarData calandarData=ChartUtil.createChartCalandarData("通用记录统计","次数",crt.getUnit(),sf,list);
        calandarData.setTop(3);
        return calandarData;
    }

}