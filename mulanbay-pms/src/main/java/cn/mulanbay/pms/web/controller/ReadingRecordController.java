package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.bean.ReadingRecordAnalyseStat;
import cn.mulanbay.pms.persistent.bean.ReadingRecordDateStat;
import cn.mulanbay.pms.persistent.bean.ReadingRecordReadedStat;
import cn.mulanbay.pms.persistent.domain.BookCategory;
import cn.mulanbay.pms.persistent.domain.ReadingRecord;
import cn.mulanbay.pms.persistent.service.ReadingRecordService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.read.*;
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
 * Created by fenghong on 2017/2/3.
 */
@Controller
@RequestMapping("/readingRecord")
public class ReadingRecordController extends BaseController {

    private static Class<ReadingRecord> beanClass = ReadingRecord.class;

    @Autowired
    ReadingRecordService readingRecordService;

    @RequestMapping(value = "list")
    public String list() {
        return "read/readingRecordList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(ReadingRecordSearch sf) {
        return callbackDataGrid(getReadingRecordResult(sf));
    }

    private PageResult<ReadingRecord> getReadingRecordResult(ReadingRecordSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort1 =new Sort("status",Sort.ASC);
        pr.addSort(sort1);
        Sort sort2 =new Sort("lastModifyTime",Sort.DESC);
        pr.addSort(sort2);
        Sort sort3 =new Sort("proposedDate",Sort.ASC);
        pr.addSort(sort3);
        PageResult<ReadingRecord> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid ReadingRecordFormRequest formRequest ) {
        ReadingRecord bean = new ReadingRecord();
        BeanCopy.copyProperties(formRequest,bean);
        BookCategory bookCategory = this.getUserEntity(BookCategory.class, formRequest.getBookCategoryId(),formRequest.getUserId());
        bean.setBookCategory(bookCategory);
        checkAndSetReadingRecord(bean);
        bean.setCreatedTime(new Date());
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
        ReadingRecord bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid ReadingRecordFormRequest formRequest) {
        ReadingRecord bean=this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        BookCategory bookCategory = this.getUserEntity(BookCategory.class, formRequest.getBookCategoryId(),formRequest.getUserId());
        bean.setBookCategory(bookCategory);
        checkAndSetReadingRecord(bean);
        bean.setLastModifyTime(new Date());
        baseService.updateObject(bean);
        return callback(bean);
    }

    /**
     * 检查完成情况
     * @param bean
     */
    private void checkAndSetReadingRecord(ReadingRecord bean){
        if(bean.getFinishedDate()!=null&&bean.getBeginDate()!=null){
            if(bean.getStatus()==null||bean.getStatus()!= ReadingRecord.ReadingStatus.READED){
                //状态不对
                throw new ApplicationException(PmsErrorCode.READING_RECORD_STATUS_ERROR);
            }
            //需要加1，因为同天的是一天
            int costDays = DateUtil.getIntervalDays(bean.getBeginDate(),bean.getFinishedDate())+1;
            bean.setCostDays(costDays);
        }
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
        for(Long id :ids){
            ReadingRecord bean=this.getUserEntity(beanClass, id,deleteRequest.getUserId());
            readingRecordService.deleteReadingRecord(bean);
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
    public ResultBean stat(ReadingRecordStatSearch sf) {
        sf.setDateQueryType("finished_date");
        ReadingRecordAnalyseStatSearch statSearch = new ReadingRecordAnalyseStatSearch();
        BeanCopy.copyProperties(sf,statSearch);
        List<ReadingRecordAnalyseStat> list = readingRecordService.statReadingRecordAnalyse(statSearch);
        ChartPieData chartPieData = this.createAnalyseStatPieData(statSearch,list);
        ReadingRecordReadedStat result = readingRecordService.statReadedReadingRecord(sf);
        result.setPieData(chartPieData);
        return callback(result);
    }


    @RequestMapping(value = "/dateStatList")
    public String dateStatList() {
        return "read/readingRecordDateStatList";
    }


    /**
     * 按照日期统计
     * @return
     */
    @RequestMapping(value = "/dateStat")
    @ResponseBody
    public ResultBean dateStat(ReadingRecordDateStatSearch sf) {
        List<ReadingRecordDateStat> list = readingRecordService.statDateReadingRecord(sf);
        ChartData chartData = new ChartData();
        chartData.setTitle("阅读统计");
        //chartData.setSubTitle(this.getDateTitle(sf));
        chartData.setLegendData(new String[]{"本数"});
        ChartYData yData1= new ChartYData();
        yData1.setName("本数");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for(ReadingRecordDateStat bean : list){
            chartData.addXData(bean,sf.getDateGroupType());
            yData1.getData().add(bean.getTotalCount());
            totalValue=totalValue.add(new BigDecimal(bean.getTotalCount()));
        }
        chartData.getYdata().add(yData1);
        String subTitle = this.getDateTitle(sf,String.valueOf(totalValue.longValue())+"本");
        chartData.setSubTitle(subTitle);
        chartData = ChartUtil.completeDate(chartData,sf);
        return callback(chartData);
    }

    /**
     * 统计分析
     * @return
     */
    @RequestMapping(value = "/analyseStatList")
    public String analyseStatList() {
        return "read/readingRecordAnalyseStatList";
    }


    /**
     *统计分析
     * @return
     */
    @RequestMapping(value = "/analyseStat")
    @ResponseBody
    public ResultBean analyseStat(ReadingRecordAnalyseStatSearch sf) {
        List<ReadingRecordAnalyseStat> list = readingRecordService.statReadingRecordAnalyse(sf);
        ChartPieData chartPieData = this.createAnalyseStatPieData(sf,list);
        return callback(chartPieData);
    }

    private ChartPieData createAnalyseStatPieData(ReadingRecordAnalyseStatSearch sf,List<ReadingRecordAnalyseStat> list){
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("阅读记录分析");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName("分析");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for(ReadingRecordAnalyseStat bean : list) {
            chartPieData.getXdata().add(bean.getName());
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(bean.getName());
            dataDetail.setValue(bean.getValue());
            serieData.getData().add(dataDetail);
            totalValue=totalValue.add(new BigDecimal(bean.getValue()));
        }
        String subTitle = this.getDateTitle(sf,String.valueOf(totalValue.intValue())+"本");
        chartPieData.setSubTitle(subTitle);
        chartPieData.getDetailData().add(serieData);
        return chartPieData;
    }

}
