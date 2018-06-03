package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.TreatRecordAnalyseStat;
import cn.mulanbay.pms.persistent.bean.TreatRecordDateStat;
import cn.mulanbay.pms.persistent.bean.TreatRecordSummaryStat;
import cn.mulanbay.pms.persistent.domain.TreatRecord;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.TreatService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.GroupType;
import cn.mulanbay.pms.web.bean.request.health.*;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.chart.*;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/31.
 * 看病记录
 */
@Controller
@RequestMapping("/treatRecord")
public class TreatRecordController extends BaseController {

    private static Class<TreatRecord> beanClass = TreatRecord.class;

    @Autowired
    TreatService treatService;
    private ChartCalandarData calandarData;

    /**
     * 获取看病或者器官的各种分类归类
     *
     * @return
     */
    @RequestMapping(value = "/getTreatCategoryTree")
    @ResponseBody
    public ResultBean getTreatCategoryTree(TreatCategorySearch sf) {

        try {
            List<String> categoryList = treatService.getTreatCategoryList(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            int i = 0;
            for (String ss : categoryList) {
                TreeBean tb = new TreeBean();
                tb.setId(ss);
                tb.setText(ss);
                list.add(tb);
                i++;
            }
            return callback(TreeBeanUtil.addRoot(list, sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取看病的各种分类归类异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list(Long id) {
        if (id == null) {
            this.request.setAttribute("showTreatRecordId", 0);
        } else {
            this.request.setAttribute("showTreatRecordId", id);
        }
        return "health/treatRecordList";
    }

    /**
     * 获取列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(TreatRecordSearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("treatDate", Sort.DESC);
        pr.addSort(s);
        PageResult<TreatRecord> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid TreatRecordFormRequest formRequest ) {
        TreatRecord bean = new TreatRecord();
        BeanCopy.copyProperties(formRequest,bean);
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
        TreatRecord bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid TreatRecordFormRequest formRequest) {
        TreatRecord bean =this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setLastModifyTime(new Date());
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
        for (String s : deleteRequest.getIds().split(",")) {
            TreatRecord bean =this.getUserEntity(beanClass, Long.valueOf(s),deleteRequest.getUserId());
            treatService.deleteTreatRecord(bean);
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
    public ResultBean stat(TreatRecordSearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        TreatRecordSummaryStat data = treatService.statTreatRecord(sf);
        //统计医保、个人的支付比例
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("费用统计");
        chartPieData.setSubTitle(this.getDateTitle(sf));
        chartPieData.getXdata().add("医保支付");
        chartPieData.getXdata().add("个人支付");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName("医保/个人");
        ChartPieSerieDetailData medicalInsurancePaidFeeDataDetail = new ChartPieSerieDetailData();
        medicalInsurancePaidFeeDataDetail.setName("医保支付");
        medicalInsurancePaidFeeDataDetail.setValue(data.getTotalMedicalInsurancePaidFee());
        serieData.getData().add(medicalInsurancePaidFeeDataDetail);

        ChartPieSerieDetailData personalPaidFeeDataDetail = new ChartPieSerieDetailData();
        personalPaidFeeDataDetail.setName("个人支付");
        personalPaidFeeDataDetail.setValue(data.getTotalPersonalPaidFee());
        serieData.getData().add(personalPaidFeeDataDetail);

        chartPieData.getDetailData().add(serieData);
        data.setPieData(chartPieData);
        return callback(data);
    }

    /**
     * 统计分析
     *
     * @return
     */
    @RequestMapping(value = "/analyseStatList")
    public String dayStatList() {
        return "health/treatRecordAnalyseStatList";
    }


    /**
     * 统计分析
     *
     * @return
     */
    @RequestMapping(value = "/analyseStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean analyseStat(TreatRecordAnalyseStatSearch sf) {
        List<TreatRecordAnalyseStat> list = treatService.treatRecordAnalyseStat(sf);
        if (sf.getChartType() == ChartType.BAR) {
            return callback(this.createAnalyseStatBarData(list, sf));
        } else {
            return callback(this.createAnalyseStatPieData(list, sf));
        }

    }

    /**
     * 封装看病记录分析的饼状图数据
     *
     * @param list
     * @param sf
     * @return
     */
    private ChartPieData createAnalyseStatPieData(List<TreatRecordAnalyseStat> list, TreatRecordAnalyseStatSearch sf) {
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("看病记录分析");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName(sf.getGroupType().getName());
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        for (TreatRecordAnalyseStat bean : list) {
            chartPieData.getXdata().add(bean.getName());
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(bean.getName());
            if (sf.getGroupType() == GroupType.COUNT) {
                dataDetail.setValue(bean.getTotalCount());
            } else {
                dataDetail.setValue(bean.getTotalFee());
            }
            totalCount = totalCount.add(new BigDecimal(bean.getTotalCount()));
            totalValue = totalValue.add(bean.getTotalFee());
            serieData.getData().add(dataDetail);
        }
        String subTitle = this.getDateTitle(sf, totalCount.longValue() + "次，" + totalValue.doubleValue() + "元");
        chartPieData.setSubTitle(subTitle);
        chartPieData.getDetailData().add(serieData);
        return chartPieData;
    }

    /**
     * 封装看病记录分析的柱状图数据
     *
     * @param list
     * @param sf
     * @return
     */
    private ChartData createAnalyseStatBarData(List<TreatRecordAnalyseStat> list, TreatRecordAnalyseStatSearch sf) {
        ChartData chartData = new ChartData();
        chartData.setTitle("看病记录分析");
        chartData.setLegendData(new String[]{"次数", "费用"});
        ChartYData yData1 = new ChartYData();
        yData1.setName("次数");
        ChartYData yData2 = new ChartYData();
        yData2.setName("费用");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        for (TreatRecordAnalyseStat bean : list) {
            chartData.getXdata().add(bean.getName());
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalFee());
            totalCount = totalCount.add(new BigDecimal(bean.getTotalCount()));
            totalValue = totalValue.add(bean.getTotalFee());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(sf, totalCount.longValue() + "次，" + totalValue.doubleValue() + "元");
        chartData.setSubTitle(subTitle);
        return chartData;

    }

    /**
     * 基于日期统计列表
     *
     * @return
     */
    @RequestMapping(value = "/dateStatList")
    public String dateStatList() {
        return "health/treatRecordDateStatList";
    }


    /**
     * 基于日期统计
     *
     * @return
     */
    @RequestMapping(value = "/dateStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean dateStat(TreatRecordDateStatSearch sf) {
        if (sf.getDateGroupType() == DateGroupType.DAYCALENDAR) {
            return callback(this.createChartCalandarDataDateStat(sf));
        }
        List<TreatRecordDateStat> list = treatService.statDateTreatRecord(sf);
        ChartData chartData = new ChartData();
        chartData.setTitle("看病统计");
        chartData.setLegendData(new String[]{"次数", "费用"});
        ChartYData yData1 = new ChartYData();
        yData1.setName("次数");
        ChartYData yData2 = new ChartYData();
        yData2.setName("费用");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        for (TreatRecordDateStat bean : list) {
            chartData.addXData(bean, sf.getDateGroupType());
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalFee());
            totalCount = totalCount.add(new BigDecimal(bean.getTotalCount()));
            totalValue = totalValue.add(bean.getTotalFee());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(sf, totalCount.longValue() + "次，" + totalValue.doubleValue() + "元");
        chartData.setSubTitle(subTitle);
        chartData = ChartUtil.completeDate(chartData, sf);
        return callback(chartData);
    }

    /**
     * 日历图
     * @param sf
     * @return
     */
    private ChartCalandarData createChartCalandarDataDateStat(TreatRecordDateStatSearch sf) {
        List<TreatRecordDateStat> list = treatService.statDateTreatRecord(sf);
        ChartCalandarData calandarData = ChartUtil.createChartCalandarData("看病统计", "次数", "次", sf, list);
        if (!StringUtil.isEmpty(sf.getDisease())) {
            //跟踪疾病
            TreatRecordByDiseaseSearch trds = new TreatRecordByDiseaseSearch();
            BeanCopy.copyProperties(sf, trds);
            PageRequest pr = trds.buildQuery();
            pr.setBeanClass(beanClass);
            List<TreatRecord> dd = baseService.getBeanList(pr);
            for(TreatRecord tb : dd){
                calandarData.addGraph(tb.getTreatDate(),1);
            }

        }else {
            calandarData.setTop(3);
        }
        return calandarData;
    }

    /**
     * 同期比对
     *
     * @return
     */
    @RequestMapping(value = "/yoyStatList")
    public String yoyStatList() {
        return "health/treatRecordYoyStatList";
    }

    /**
     * 同期比对统计
     *
     * @return
     */
    @RequestMapping(value = "/yoyStat")
    @ResponseBody
    public ResultBean yoyStat(@Valid TreatRecordYoyStatSearch sf) {
        if (sf.getDateGroupType() == DateGroupType.DAY) {
            return callback(createChartCalandarMultiData(sf));
        }
        ChartData chartData = initYoyCharData(sf, "看病记录同期对比", null);
        String[] legendData = new String[sf.getYears().size()];
        for (int i = 0; i < sf.getYears().size(); i++) {
            legendData[i] = sf.getYears().get(i).toString();
            TreatRecordDateStatSearch monthStatSearch = this.generateSearch(sf.getYears().get(i), sf);
            ChartYData yData = new ChartYData();
            yData.setName(sf.getYears().get(i).toString());
            List<TreatRecordDateStat> list = treatService.statDateTreatRecord(monthStatSearch);
            //临时内容，作为补全用
            ChartData temp = new ChartData();
            for (TreatRecordDateStat bean : list) {
                temp.addXData(bean, sf.getDateGroupType());
                if (sf.getGroupType() == GroupType.COUNT) {
                    yData.getData().add(bean.getTotalCount());
                } else {
                    yData.getData().add(bean.getTotalFee());
                }
            }
            //临时内容，作为补全用
            temp.getYdata().add(yData);
            monthStatSearch.setCompliteDate(true);
            temp = ChartUtil.completeDate(temp, monthStatSearch);
            //设置到最终的结果集中
            chartData.getYdata().add(temp.getYdata().get(0));
        }
        chartData.setLegendData(legendData);

        return callback(chartData);
    }

    private TreatRecordDateStatSearch generateSearch(int year, TreatRecordYoyStatSearch sf) {
        //数据,为了代码复用及统一，统计还是按照日期的统计
        TreatRecordDateStatSearch monthStatSearch = new TreatRecordDateStatSearch();
        monthStatSearch.setStartDate(DateUtil.getDate(year + "-01-01", DateUtil.FormatDay1));
        monthStatSearch.setEndDate(DateUtil.getDate(year + "-12-31", DateUtil.FormatDay1));
        monthStatSearch.setUserId(sf.getUserId());
        monthStatSearch.setFeeField(sf.getFeeField());
        monthStatSearch.setDateGroupType(sf.getDateGroupType());
        monthStatSearch.setName(sf.getName());
        return monthStatSearch;
    }

    /**
     * 基于日历的热点图
     *
     * @param sf
     * @return
     */
    private ChartCalandarMultiData createChartCalandarMultiData(TreatRecordYoyStatSearch sf) {
        ChartCalandarMultiData data = new ChartCalandarMultiData();
        data.setTitle("看病记录同期对比");
        if (sf.getGroupType() == GroupType.COUNT) {
            data.setUnit("次");
        } else {
            data.setUnit("元");
        }
        for (int i = 0; i < sf.getYears().size(); i++) {
            TreatRecordDateStatSearch monthStatSearch = this.generateSearch(sf.getYears().get(i), sf);
            List<TreatRecordDateStat> list = treatService.statDateTreatRecord(monthStatSearch);
            for (TreatRecordDateStat bean : list) {
                String dateString = DateUtil.getFormatDateString(bean.getIndexValue().toString(), "yyyyMMdd", "yyyy-MM-dd");
                if (sf.getGroupType() == GroupType.COUNT) {
                    data.addData(sf.getYears().get(i), dateString, bean.getTotalCount());
                } else {
                    data.addData(sf.getYears().get(i), dateString, bean.getTotalFee().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        }
        return data;
    }

}
