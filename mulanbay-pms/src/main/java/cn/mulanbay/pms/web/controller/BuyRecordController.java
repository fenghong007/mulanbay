package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.bean.*;
import cn.mulanbay.pms.persistent.domain.BuyRecord;
import cn.mulanbay.pms.persistent.domain.BuyType;
import cn.mulanbay.pms.persistent.domain.GoodsType;
import cn.mulanbay.pms.persistent.domain.PriceRegion;
import cn.mulanbay.pms.persistent.enums.BuyRecordPriceType;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.MoneyFlow;
import cn.mulanbay.pms.persistent.enums.Payment;
import cn.mulanbay.pms.persistent.service.BuyRecordService;
import cn.mulanbay.pms.persistent.service.LifeExperienceService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.GroupType;
import cn.mulanbay.pms.web.bean.request.buy.*;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.buy.ChartRadarGroupBean;
import cn.mulanbay.pms.web.bean.response.chart.*;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 购买记录
 */
@Controller
@RequestMapping("/buyRecord")
public class BuyRecordController extends BaseController {

    private static Class<BuyRecord> beanClass = BuyRecord.class;

    @Autowired
    BuyRecordService buyRecordService;

    @Autowired
    LifeExperienceService lifeExperienceService;

    // # 购买记录统计中是否启用商品类型里的可统计字段
    @Value("${system.buyRecord.stat.useStatable}")
    boolean useStatable;


    @RequestMapping(value = "list")
    public String list() {
        return "buy/buyRecordList";
    }


    /**
     * 关键字列表
     *
     * @return
     */
    @RequestMapping(value = "/getKeywordsTree")
    @ResponseBody
    public ResultBean getKeywordsTree(BuyRecordKeywordsSearch sf) {
        List<TreeBean> list = new ArrayList<TreeBean>();
        Date start = DateUtil.getDate(0 - sf.getDays());
        sf.setStartDate(start);
        List<String> keywordsList = buyRecordService.getKeywordsList(sf);
        //去重
        Set<String> keywordsSet = deleteDuplidate(keywordsList);
        for (String s : keywordsSet) {
            TreeBean tb = new TreeBean();
            tb.setId(s);
            tb.setText(s);
            list.add(tb);
        }
        return callback(TreeBeanUtil.addRoot(list, sf.getNeedRoot()));
    }

    /**
     * 去重，每个关键字以英文逗号分隔
     *
     * @param keywordsList
     * @return
     */
    private Set<String> deleteDuplidate(List<String> keywordsList) {
        //去重
        Set<String> keywordsSet = new TreeSet<>();
        for (String s : keywordsList) {
            if (s == null || s.isEmpty()) {
                continue;
            }
            String[] ss = s.split(",");
            for (String key : ss) {
                keywordsSet.add(key);
            }
        }
        return keywordsSet;
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(BuyRecordSearch sf) {
        PageRequest pr = sf.buildQuery();
        Sort s = new Sort(sf.getSortField(), sf.getSortType());
        pr.addSort(s);
        pr.setBeanClass(beanClass);
        //todo 该段逻辑转入到service中
        if(sf.getMoneyFlow()!=null){
            Parameter parameter = new Parameter("price",Parameter.Operator.SQL);
            if(sf.getMoneyFlow()== MoneyFlow.BUY){
                parameter.setValue("price>=0 ");
            }else{
                parameter.setValue("price<0 ");
            }
            pr.addParameter(parameter);
        }
        PageResult<BuyRecord> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 总的概要统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(BuyRecordSearch sf) {
        sf.setMoneyFlow(MoneyFlow.BUY);
        BuyRecordStat buyData = buyRecordService.getBuyRecordStat(sf);
        sf.setMoneyFlow(MoneyFlow.SALE);
        BuyRecordStat saleData = buyRecordService.getBuyRecordStat(sf);
        List<BuyRecordStat> list = new ArrayList<>();
        list.add(buyData);
        list.add(saleData);
        return callback(list);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid BuyRecordFormRequest formRequest) {
        BuyRecord buyRecord = new BuyRecord();
        changeFormToBean(formRequest,buyRecord);
        buyRecord.setCreatedTime(new Date());
        baseService.saveObject(buyRecord);
        return callback(null);
    }

    private void changeFormToBean(BuyRecordFormRequest formRequest,BuyRecord buyRecord){
        BeanCopy.copyProperties(formRequest,buyRecord);
        buyRecord.setTotalPrice(buyRecord.getPrice() * buyRecord.getAmount()
                + buyRecord.getShipment());
        //不判断会导致保存异常。当不选择商品子类型时，spring mvc会初始化一个空的GoodsType对象（id=null）
        if (formRequest.getSubGoodsTypeId() != null) {
            GoodsType subGoodsType = this.getUserEntity(GoodsType.class,formRequest.getSubGoodsTypeId(),formRequest.getUserId());
            buyRecord.setSubGoodsType(subGoodsType);
        }
        if (formRequest.getCrossGoodsTypeId() != null) {
            GoodsType crossGoodsType = this.getUserEntity(GoodsType.class,formRequest.getCrossGoodsTypeId(),formRequest.getUserId());
            buyRecord.setCrossGoodsType(crossGoodsType);
        }
        BuyType buyType = this.getUserEntity(BuyType.class,formRequest.getBuyTypeId(),formRequest.getUserId());
        buyRecord.setBuyType(buyType);
        GoodsType goodsType = this.getUserEntity(GoodsType.class,formRequest.getGoodsTypeId(),formRequest.getUserId());
        buyRecord.setGoodsType(goodsType);
        //消费日期默认为购买日期
        if(buyRecord.getConsumeDate()==null){
            buyRecord.setConsumeDate(buyRecord.getBuyDate());
        }
    }
    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid BuyRecordFormRequest formRequest) {
        BuyRecord buyRecord =  this.getUserEntity(BuyRecord.class,formRequest.getId(),formRequest.getUserId());
        changeFormToBean(formRequest,buyRecord);
        buyRecord.setLastModifyTime(new Date());
        baseService.updateObject(buyRecord);
        lifeExperienceService.updateLifeExperienceConsumeByBuyRecord(buyRecord);
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
        BuyRecord buyRecord =  this.getUserEntity(BuyRecord.class,getRequest.getId(),getRequest.getUserId());
        return callback(buyRecord);
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        this.deleteUserEntity(BuyRecord.class,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    /**
     * 统计分析
     *
     * @return
     */
    @RequestMapping(value = "/analyseStatList")
    public String analyseStatList() {
        return "buy/buyRecordAnalyseStatList";
    }


    /**
     * 统计分析
     *
     * @return
     */
    @RequestMapping(value = "/analyseStat")
    @ResponseBody
    public ResultBean analyseStat(BuyRecordAnalyseStatSearch sf) {
        sf.setUseStatable(useStatable);
        if (sf.getChartType() == ChartType.BAR) {
            List<BuyRecordRealTimeStat> list = buyRecordService.getAnalyseStat(sf);
            return callback(this.createAnalyseStatBarData(list, sf));
        } else if (sf.getChartType() == ChartType.PIE) {
            List<BuyRecordRealTimeStat> list = buyRecordService.getAnalyseStat(sf);
            return callback(this.createAnalyseStatPieData(list, sf));
        } else {
            return callback(this.createAnalyseStatRadarData(sf));
        }
    }

    /**
     * 封装消费分析的雷达图数据
     *
     * @param sf
     * @return
     */
    private ChartRadarData createAnalyseStatRadarData(BuyRecordAnalyseStatSearch sf) {
        ChartRadarData chartRadarData = new ChartRadarData();
        chartRadarData.setTitle("购买记录分析");
        List<BuyRecordRadarStat> list = buyRecordService.getRadarStat(sf);
        if(list.isEmpty()){
            return chartRadarData;
        }
        long maxValue = 0;
        //获取分类
        List<ChartRadarGroupBean> groups = this.getChartRadarGroupData(sf);
        List<Long> initData = new ArrayList<>();

        Map<Integer,List<BuyRecordRadarStat>> map = new TreeMap();
        //转为Map
        int size = list.size();
        for(int i =0;i<size;i++){
            BuyRecordRadarStat stat = list.get(i);
            List<BuyRecordRadarStat> statList = map.get(stat.getIndexValue());
            if(statList ==null){
                statList = new ArrayList<>();
                map.put(stat.getIndexValue(),statList);
            }
            statList.add(stat);
            //获取最大值
            long v=0L;
            if(sf.getType()==GroupType.COUNT){
                v = stat.getTotalCount().longValue();
            }else{
                v = stat.getTotalPrice().longValue();
            }
            if(v>maxValue){
                maxValue = v;
            }
        }
        //设置标签
        for(ChartRadarGroupBean gb : groups){
            ChartRadarIndicatorData rid = new ChartRadarIndicatorData();
            rid.setText(gb.getName());
            rid.setMax(maxValue);
            chartRadarData.getIndicatorData().add(rid);
            initData.add(Long.valueOf(0-gb.getId()));
        }
        // 设置LegendData
        for(Integer ii : map.keySet()){
            chartRadarData.getLegendData().add(ii.toString());
            //
            ChartRadarSerieData serieData = new ChartRadarSerieData();
            List<Long> data = new ArrayList<>();
            //克隆数组
            data.addAll(initData);
            int dataSize = data.size();
            List<BuyRecordRadarStat> statList = map.get(ii);
            for(BuyRecordRadarStat ss : statList){
                for(int i =0 ;i<dataSize;i++ ){
                    long v = data.get(i);
                    if(v + ss.getIntGroupIdValue()==0){
                        //取代位置
                        if(sf.getType()==GroupType.COUNT){
                            v = ss.getTotalCount().longValue();
                        }else{
                            v = ss.getTotalPrice().longValue();
                        }
                        data.set(i,v);
                    }
                }
            }
            // 把没有取代位置全部设置为0
            for(int i =0 ;i<dataSize;i++ ) {
                long v = data.get(i);
                if(v<0){
                    data.set(i,0L);
                }
            }
            //不能加中文，否则echarts无法显示lab
            serieData.setName(ii.toString());
            serieData.setData(data);
            chartRadarData.getSeries().add(serieData);
        }

        return chartRadarData;
    }

    /**
     * 获取分组数据
     * @param sf
     * @return
     */
    private List<ChartRadarGroupBean> getChartRadarGroupData(BuyRecordAnalyseStatSearch sf){
        List<ChartRadarGroupBean> result = new ArrayList<>();
        //获取分类
        if(sf.getGroupField().equals("goodsTypeId")){
            // 获取商品类型
            GoodsTypeSearch search = new GoodsTypeSearch();
            search.setPid(0);
            PageRequest pr = search.buildQuery();
            pr.setBeanClass(GoodsType.class);
            Sort sort =new Sort("id",Sort.ASC);
            pr.addSort(sort);
            PageResult<GoodsType> qr = baseService.getBeanResult(pr);
            for(GoodsType bb : qr.getBeanList()){
                ChartRadarGroupBean bean = new ChartRadarGroupBean();
                bean.setId(bb.getId());
                bean.setName(bb.getName());
                result.add(bean);
            }
        }else if(sf.getGroupField().equals("subGoodsTypeId")){
            Integer goodsTypeId = sf.getGoodsType();
            if(goodsTypeId==null){
                //商品子类统计需要父类商品类型
                throw new ApplicationException(PmsErrorCode.BUY_RECORD_GOODS_TYPE_NULL);
            }
            // 获取商品类型
            GoodsTypeSearch search = new GoodsTypeSearch();
            search.setPid(goodsTypeId);
            PageRequest pr = search.buildQuery();
            pr.setBeanClass(GoodsType.class);
            Sort sort =new Sort("id",Sort.ASC);
            pr.addSort(sort);
            PageResult<GoodsType> qr = baseService.getBeanResult(pr);
            for(GoodsType bb : qr.getBeanList()){
                ChartRadarGroupBean bean = new ChartRadarGroupBean();
                bean.setId(bb.getId());
                bean.setName(bb.getName());
                result.add(bean);
            }
        }else if(sf.getGroupField().equals("buyTypeId")){
            BuyTypeSearch search = new BuyTypeSearch();
            PageRequest pr = search.buildQuery();
            pr.setBeanClass(BuyType.class);
            Sort sort =new Sort("id",Sort.ASC);
            pr.addSort(sort);
            PageResult<BuyType> qr = baseService.getBeanResult(pr);
            for(BuyType bb : qr.getBeanList()){
                ChartRadarGroupBean bean = new ChartRadarGroupBean();
                bean.setId(bb.getId());
                bean.setName(bb.getName());
                result.add(bean);
            }
        }else if(sf.getGroupField().equals("payment")){
            // 未知
            ChartRadarGroupBean unKnown = new ChartRadarGroupBean();
            unKnown.setId(-1);
            unKnown.setName("未知");
            result.add(unKnown);

            for(Payment p : Payment.values()){
                ChartRadarGroupBean bean = new ChartRadarGroupBean();
                bean.setId(p.ordinal());
                bean.setName(p.getName());
                result.add(bean);
            }
        }else if(sf.getGroupField().equals("priceRegion")){
            PriceRegionSearch search = new PriceRegionSearch();
            PageRequest pr = search.buildQuery();
            pr.setBeanClass(PriceRegion.class);
            Sort sort =new Sort("id",Sort.ASC);
            pr.addSort(sort);
            PageResult<PriceRegion> qr = baseService.getBeanResult(pr);
            for(PriceRegion bb : qr.getBeanList()){
                ChartRadarGroupBean bean = new ChartRadarGroupBean();
                bean.setId(bb.getId());
                bean.setName(bb.getName()+"("+bb.getMinPrice().longValue()+"-"+bb.getMaxPrice().longValue()+"元)");
                result.add(bean);
            }
        }else{
            throw new ApplicationException(PmsErrorCode.UNSUPPORT_BUY_RECORD_GROUP_TYPE);
        }
        //todo 其他
        return result;
    }

    /**
     * 封装消费分析的饼状图数据
     *
     * @param list
     * @param sf
     * @return
     */
    private ChartPieData createAnalyseStatPieData(List<BuyRecordRealTimeStat> list, BuyRecordAnalyseStatSearch sf) {
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("购买记录分析");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName(sf.getType().getName());
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for (BuyRecordRealTimeStat bean : list) {
            chartPieData.getXdata().add(bean.getName());
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(bean.getName());
            dataDetail.setValue(bean.getValue());
            serieData.getData().add(dataDetail);
            totalValue = totalValue.add(new BigDecimal(bean.getValue()));
        }
        String subTitle = this.getDateTitle(sf, getSubTitlePostfix(sf.getType(),totalValue));
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
    private ChartData createAnalyseStatBarData(List<BuyRecordRealTimeStat> list, BuyRecordAnalyseStatSearch sf) {
        ChartData chartData = new ChartData();
        chartData.setTitle("消费分析");
        chartData.setLegendData(new String[]{sf.getType().getName()});
        ChartYData yData = new ChartYData();
        yData.setName(sf.getType().getName());
        BigDecimal totalValue = new BigDecimal(0);
        for (BuyRecordRealTimeStat bean : list) {
            chartData.getXdata().add(bean.getName());
            yData.getData().add(bean.getValue());
            totalValue = totalValue.add(new BigDecimal(bean.getValue()));
        }
        String subTitle = this.getDateTitle(sf, getSubTitlePostfix(sf.getType(),totalValue));
        chartData.setSubTitle(subTitle);
        chartData.getYdata().add(yData);
        return chartData;

    }

    /**
     * 获取子标题后缀
     * @param groupType
     * @param totalValue
     * @return
     */
    private String getSubTitlePostfix(GroupType groupType,BigDecimal totalValue){
        if (groupType == GroupType.COUNT) {
            return String.valueOf(totalValue.longValue()) + "次";
        } else {
            return String.valueOf(totalValue.doubleValue()) + "元";
        }
    }



    @RequestMapping(value = "/dateStatList")
    public String dateStatList() {
        return "buy/buyRecordDateStatList";
    }


    /**
     * 按照日期统计
     *
     * @return
     */
    @RequestMapping(value = "/dateStat")
    @ResponseBody
    public ResultBean dateStat(BuyRecordDateStatSearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        sf.setUseStatable(useStatable);
        List<BuyRecordDateStat> list = buyRecordService.statBuyRecordByDate(sf);
        if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(ChartUtil.createChartCalandarData("消费统计","次数","次",sf,list));
        }
        ChartData chartData = new ChartData();
        chartData.setTitle("消费统计");
        chartData.setSubTitle(this.getDateTitle(sf));
        chartData.setLegendData(new String[]{"次数", "消费(元)"});
        ChartYData yData1 = new ChartYData();
        yData1.setName("次数");
        ChartYData yData2 = new ChartYData();
        yData2.setName("消费(元)");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        for (BuyRecordDateStat bean : list) {
            chartData.addXData(bean, sf.getDateGroupType());
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalPrice());
            totalCount = totalCount.add(new BigDecimal(bean.getTotalCount().longValue()));
            totalValue = totalValue.add(bean.getTotalPrice());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(sf, totalCount.longValue()+"次，"+totalValue.doubleValue()+"元");
        chartData.setSubTitle(subTitle);
        chartData = ChartUtil.completeDate(chartData, sf);
        return callback(chartData);
    }

    /**
     * 同期比对
     *
     * @return
     */
    @RequestMapping(value = "/yoyStatList")
    public String yoyStatList() {
        return "buy/buyRecordYoyStatList";
    }

    /**
     * 同期比对统计
     *
     * @return
     */
    @RequestMapping(value = "/yoyStat")
    @ResponseBody
    public ResultBean yoyStat(@Valid BuyRecordYoyStatSearch sf) {
        if(sf.getDateGroupType()==DateGroupType.DAY){
            return callback(createChartCalandarMultiData(sf));
        }
        ChartData chartData = initYoyCharData(sf, "消费统计同期对比", null);
        String[] legendData = new String[sf.getYears().size()];
        for (int i = 0; i < sf.getYears().size(); i++) {
            legendData[i] = sf.getYears().get(i).toString();
            //数据,为了代码复用及统一，统计还是按照日期的统计
            BuyRecordDateStatSearch dateSearch = generateSearch(sf.getYears().get(i),sf);
            ChartYData yData = new ChartYData();
            yData.setName(sf.getYears().get(i).toString());
            List<BuyRecordDateStat> list = buyRecordService.statBuyRecordByDate(dateSearch);
            //临时内容，作为补全用
            ChartData temp = new ChartData();
            for (BuyRecordDateStat bean : list) {
                temp.addXData(bean, sf.getDateGroupType());
                if (sf.getGroupType() == GroupType.COUNT) {
                    yData.getData().add(bean.getTotalCount());
                } else {
                    yData.getData().add(bean.getTotalPrice());
                }
            }
            //临时内容，作为补全用
            temp.getYdata().add(yData);
            dateSearch.setCompliteDate(true);
            temp = ChartUtil.completeDate(temp, dateSearch);
            //设置到最终的结果集中
            chartData.getYdata().add(temp.getYdata().get(0));
        }
        chartData.setLegendData(legendData);

        return callback(chartData);
    }

    /**
     * 基于日历的热点图
     * @param sf
     * @return
     */
    private ChartCalandarMultiData createChartCalandarMultiData(BuyRecordYoyStatSearch sf) {
        ChartCalandarMultiData data = new ChartCalandarMultiData();
        data.setTitle("消费统计同期对比");
        if (sf.getGroupType() == GroupType.COUNT) {
            data.setUnit("次");
        }else {
            data.setUnit("元");
        }
        for (int i = 0; i < sf.getYears().size(); i++) {
            BuyRecordDateStatSearch dateSearch = generateSearch(sf.getYears().get(i),sf);
            List<BuyRecordDateStat> list = buyRecordService.statBuyRecordByDate(dateSearch);
            for (BuyRecordDateStat bean : list) {
                String dateString = DateUtil.getFormatDateString(bean.getIndexValue().toString(),"yyyyMMdd","yyyy-MM-dd");
                if (sf.getGroupType() == GroupType.COUNT) {
                    data.addData(sf.getYears().get(i),dateString,bean.getTotalCount());
                } else {
                    data.addData(sf.getYears().get(i),dateString, bean.getTotalPrice().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        }
        return data;
    }

    private BuyRecordDateStatSearch generateSearch(int year,BuyRecordYoyStatSearch sf){
        BuyRecordDateStatSearch dateSearch = new BuyRecordDateStatSearch();
        dateSearch.setDateGroupType(sf.getDateGroupType());
        dateSearch.setStartDate(DateUtil.getDate(year + "-01-01", DateUtil.FormatDay1));
        dateSearch.setEndDate(DateUtil.getDate(year + "-12-31", DateUtil.FormatDay1));
        dateSearch.setUserId(sf.getUserId());
        dateSearch.setStartTotalPrice(sf.getStartTotalPrice());
        dateSearch.setEndTotalPrice(sf.getEndTotalPrice());
        dateSearch.setPriceType(BuyRecordPriceType.TOTALPRICE);
        dateSearch.setBuyType(sf.getBuyType());
        dateSearch.setGoodsType(sf.getGoodsType());
        dateSearch.setSubGoodsType(sf.getSubGoodsType());
        dateSearch.setUseStatable(useStatable);
        return dateSearch;
    }
    /**
     * 根据关键字统计
     *
     * @return
     */
    @RequestMapping(value = "/keywordsStatList")
    public String keywordsStatList() {
        return "buy/buyRecordKeywordsStatList";
    }

    /**
     * 根据关键字统计
     *
     * @return
     */
    @RequestMapping(value = "/keywordsStat")
    @ResponseBody
    public ResultBean keywordsStat(@Valid BuyRecordKeywordsStatSearch sf) {
        sf.setUseStatable(useStatable);
        List<BuyRecordKeywordsStat> list = buyRecordService.statBuyRecordByKeywords(sf);
        ChartData chartData = new ChartData();
        chartData.setTitle("关键字统计");
        chartData.setSubTitle(this.getDateTitle(sf));
        chartData.setLegendData(new String[]{"次数", "消费(元)"});
        ChartYData yData1 = new ChartYData();
        yData1.setName("次数");
        ChartYData yData2 = new ChartYData();
        yData2.setName("消费(元)");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        //总的值
        BigDecimal totalCount = new BigDecimal(0);
        for (BuyRecordKeywordsStat bean : list) {
            chartData.getXdata().add(bean.getKeywords());
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalPrice());
            totalCount = totalCount.add(new BigDecimal(bean.getTotalCount().longValue()));
            totalValue = totalValue.add(bean.getTotalPrice());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(sf, totalCount.longValue()+"次，"+totalValue.doubleValue()+"元");
        chartData.setSubTitle(subTitle);
        chartData = ChartUtil.completeDate(chartData, sf);
        return callback(chartData);
    }


}
