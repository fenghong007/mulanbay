package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.bean.UserPointsDateStat;
import cn.mulanbay.pms.persistent.bean.UserPointsSourceStat;
import cn.mulanbay.pms.persistent.bean.UserPointsValueStat;
import cn.mulanbay.pms.persistent.domain.*;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.RewardSource;
import cn.mulanbay.pms.persistent.service.AuthService;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.CommonOrderType;
import cn.mulanbay.pms.web.bean.request.user.UserPointsSourceStatSearch;
import cn.mulanbay.pms.web.bean.request.user.UserPointsTimelineStatSearch;
import cn.mulanbay.pms.web.bean.request.user.UserPointsValueStatSearch;
import cn.mulanbay.pms.web.bean.request.user.UserRewardPointRecordSearch;
import cn.mulanbay.pms.web.bean.response.chart.*;
import cn.mulanbay.pms.web.bean.response.user.UserRewardPointRecordResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-16 10:14
 */
@Controller
@RequestMapping("/userRewardPointRecord")
public class UserRewardPointRecordController extends BaseController {

    private static Class<UserRewardPointRecord> beanClass = UserRewardPointRecord.class;


    @Autowired
    AuthService authService;

    @RequestMapping(value = "list")
    public String list() {
        return "user/userRewardPointRecordList";
    }

    /**
     * 获取任务列表
     * 使用RequestParam 方式是因为easyui 的datagrid使用ajax请求时对于list多值类型的参数参数名会带上中挂号
     * 且无法在js请求方式里设置使用 traditional:true参数
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserRewardPointRecordSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s1 = new Sort("createdTime", Sort.DESC);
        pr.addSort(s1);
        PageResult<UserRewardPointRecord> qr = baseService.getBeanResult(pr);
        PageResult<UserRewardPointRecordResponse> result = new PageResult<UserRewardPointRecordResponse>(qr);
        List<UserRewardPointRecordResponse> list = new ArrayList<>();
        for(UserRewardPointRecord urp : qr.getBeanList()){
            UserRewardPointRecordResponse bean= new UserRewardPointRecordResponse();
            BeanCopy.copyProperties(urp,bean);
            bean.setSourceName(this.getSourceName(urp.getSourceId(),urp.getRewardSource()));
            list.add(bean);
        }
        result.setBeanList(list);
        return callbackDataGrid(result);
    }
    /**
     * 年统计列表
     * @return
     */
    @RequestMapping(value = "/pointsTimelineStatList")
    public String statList() {
        return "user/userPointsTimelineStatList";
    }


    /**
     * 积分时间线
     *
     * @return
     */
    @RequestMapping(value = "/pointsTimelineStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean pointsTimelineStat(UserPointsTimelineStatSearch sf) {
        if(sf.getDateGroupType()==null){
            return callback(createTimelineUserPoint(sf));
        }else if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(createCalandarTimelineUserPoint(sf));
        }else{
            return callback(createUserPointsDateStat(sf));
        }
    }

    /**
     * 日历类型
     * @param sf
     * @return
     */
    private ChartCalandarCompareData createCalandarTimelineUserPoint(UserPointsTimelineStatSearch sf){
        if(sf.getStartDate()==null||sf.getEndDate()==null){
            throw new ApplicationException(PmsErrorCode.START_OR_END_DATE_NULL);
        }
        List<UserPointsDateStat> list = authService.statDateUserPoints(sf);
        int year = DateUtil.getYear(sf.getStartDate());
        int endYear = DateUtil.getYear(sf.getEndDate());
        if(year!=endYear){
            throw new ApplicationException(PmsErrorCode.START_YEAR_NOT_EQUALS_END_YEAR);
        }
        ChartCalandarCompareData chartData = new ChartCalandarCompareData();
        String sourceName = this.getSourceName(sf.getSourceId(),sf.getRewardSource());
        if(sourceName!=null){
            chartData.setTitle("["+sourceName+"]"+year+"年用户积分统计");
        }else{
            chartData.setTitle(year+"年用户积分统计");
        }
        chartData.setUnit("次");
        chartData.setYear(year);
        for(UserPointsDateStat stat : list){
            String dd =DateUtil.getFormatDateString(String.valueOf(stat.getDateIndexValue()),"yyyyMMdd",DateUtil.FormatDay1);
            double vv = stat.getCalendarStatValue();
            if(vv>=0){
                chartData.addSerieData(dd,vv,false,1,1);
            }else{
                chartData.addSerieData(dd,0-vv,false,1,2);
            }
        }
        chartData.setLegendData(new String[]{"正分","负分"});
        return chartData;
    }

    /**
     * 时间线
     * @param sf
     * @return
     */
    private ChartData createTimelineUserPoint(@Valid UserPointsTimelineStatSearch sf){
        UserRewardPointRecordSearch urpr  = new UserRewardPointRecordSearch();
        BeanCopy.copyProperties(sf,urpr);
        //不需要分页
        urpr.setPage(-1);
        PageRequest pr = urpr.buildQuery();
        pr.setBeanClass(UserRewardPointRecord.class);
        Sort sort = new Sort("createdTime",Sort.ASC);
        pr.addSort(sort);
        List<UserRewardPointRecord> list = baseService.getBeanList(pr);
        ChartData chartData = new ChartData();
        String sourceName = this.getSourceName(sf.getSourceId(),sf.getRewardSource());
        if(sourceName!=null){
            chartData.setTitle("["+sourceName+"]用户积分统计");
        }else{
            chartData.setTitle("用户积分统计");
        }
        chartData.setLegendData(new String[]{"积分"});
        ChartYData yData =new ChartYData("积分");
        String timeFormat="yyyy-MM-dd HH:mm";
        int startYear = DateUtil.getYear(urpr.getStartDate());
        int endYear = DateUtil.getYear(urpr.getEndDate());
        if(startYear==endYear){
            //同一年内
            timeFormat="MM-dd HH:mm";
        }
        for(UserRewardPointRecord bean : list) {
            chartData.getXdata().add(DateUtil.getFormatDate(bean.getCreatedTime(),timeFormat));
            yData.getData().add(bean.getAfterPoints());
        }
        chartData.getYdata().add(yData);
        String subTitle = this.getDateTitle(sf);
        chartData.setSubTitle(subTitle);
        return chartData;
    }

    private ChartData createUserPointsDateStat(UserPointsTimelineStatSearch tlSf){
        List<UserPointsDateStat> list = authService.statDateUserPoints(tlSf);
        ChartData chartData = new ChartData();
        String sourceName = this.getSourceName(tlSf.getSourceId(),tlSf.getRewardSource());
        if(sourceName!=null){
            chartData.setTitle("["+sourceName+"]用户积分统计");
        }else{
            chartData.setTitle("用户积分统计");
        }
        chartData.setLegendData(new String[]{"次数","积分"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        ChartYData yData2= new ChartYData();
        yData2.setName("积分");
        for(UserPointsDateStat bean : list) {
            chartData.getXdata().add(bean.getIndexValue().toString());
            yData1.getData().add(bean.getTotalCount().longValue());
            yData2.getData().add(bean.getTotalRewardPoints().longValue());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(tlSf);
        chartData.setSubTitle(subTitle);
        return chartData;
    }
    /**
     * 按照值积分值统计
     *
     * @return
     */
    @RequestMapping(value = "/pointsValueStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean pointsValueStat(UserPointsValueStatSearch sf) {
        sf.setRewardSourceIntByRewardSource(sf.getRewardSource());
        if(sf.getDateGroupType()==null){
            return callback(createDefaultPointsValueStat(sf));
        }else{
            UserPointsTimelineStatSearch tlSf =new UserPointsTimelineStatSearch();
            BeanCopy.copyProperties(sf,tlSf);
            if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
                return callback(createCalandarTimelineUserPoint(tlSf));
            }else{
                return callback(createUserPointsDateStat(tlSf));
            }
        }

    }

    private ChartPieData createDefaultPointsValueStat(UserPointsValueStatSearch sf){
        List<UserPointsValueStat> list = authService.statUserPointsValue(sf);
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("["+this.getSourceName(sf.getSourceId(),sf.getRewardSource())+"]积分值统计");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName("积分(次)");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for(UserPointsValueStat bean : list) {
            chartPieData.getXdata().add(bean.getRewards().toString()+"分");
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(bean.getRewards().toString()+"分");
            dataDetail.setValue(bean.getTotalCount().intValue());
            serieData.getData().add(dataDetail);
            totalValue=totalValue.add(new BigDecimal(bean.getTotalRewardPoints().longValue()));
        }
        String subTitle = this.getDateTitle(sf,String.valueOf(totalValue.intValue())+"分");
        chartPieData.setSubTitle(subTitle);
        chartPieData.getDetailData().add(serieData);
        return chartPieData;
    }

    private String getSourceName(Long sourceId,RewardSource rewardSource){
        if(sourceId==null||rewardSource==null){
            return null;
        }
        if(rewardSource== RewardSource.NOTIFY){
            UserNotify userNotify = baseService.getObject(UserNotify.class,sourceId);
            return userNotify ==null ? "未知": userNotify.getTitle();
        }else if(rewardSource== RewardSource.PLAN){
            UserPlan userPlan = baseService.getObject(UserPlan.class,sourceId);
            return userPlan==null ? "未知":userPlan.getTitle();
        }else if(rewardSource== RewardSource.OPERATION){
            SystemFunction systemFunction = baseService.getObject(SystemFunction.class,sourceId);
            return systemFunction ==null ? "未知":systemFunction.getName();
        }
        return null;
    }

    /**
     * 按照来源的积分值统计
     *
     * @return
     */
    @RequestMapping(value = "/pointsSourceStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean pointsSourceStat(UserPointsSourceStatSearch sf) {
        sf.setRewardSourceIntByRewardSource(sf.getRewardSource());
        List<UserPointsSourceStat> list = authService.statUserPointsSource(sf);
        if (sf.getChartType() == ChartType.BAR) {
            return callback(createUserPointsSourceStatBarChartData(list,sf));
        } else{
            return callback(createUserPointsSourceStatPieChartData(list,sf));
        }
    }

    private ChartData createUserPointsSourceStatBarChartData(List<UserPointsSourceStat> list,UserPointsSourceStatSearch sf){
        ChartData chartData = new ChartData();
        chartData.setTitle("用户积分统计");
        chartData.setLegendData(new String[]{"次数","积分"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        ChartYData yData2= new ChartYData();
        yData2.setName("积分");
        for(UserPointsSourceStat bean : list) {
            RewardSource rewardSource = RewardSource.getRewardSource(bean.getRewardSource().intValue());
            String sourceName = this.getSourceName(bean.getSourceId().longValue(),rewardSource);
            chartData.getXdata().add(sourceName ==null ? "未知":sourceName);
            yData1.getData().add(bean.getTotalCount().longValue());
            yData2.getData().add(bean.getTotalRewardPoints().longValue());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        String subTitle = this.getDateTitle(sf);
        chartData.setSubTitle(subTitle);
        return chartData;
    }

    /**
     * 封装消费分析的饼状图数据
     *
     * @param list
     * @param sf
     * @return
     */
    private ChartPieData createUserPointsSourceStatPieChartData(List<UserPointsSourceStat> list,UserPointsSourceStatSearch sf) {
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("用户积分统计");
        ChartPieSerieData serieData = new ChartPieSerieData();
        if(sf.getOrderBy()== CommonOrderType.COUNTS){
            serieData.setName("次数");
        }else{
            serieData.setName("积分");
        }
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for (UserPointsSourceStat bean : list) {
            RewardSource rewardSource = RewardSource.getRewardSource(bean.getRewardSource().intValue());
            String sourceName = this.getSourceName(bean.getSourceId().longValue(),rewardSource);
            if(sourceName==null){
                sourceName="未知";
            }
            chartPieData.getXdata().add(sourceName);
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(sourceName);
            if(sf.getOrderBy()== CommonOrderType.COUNTS){
                dataDetail.setValue(bean.getTotalCount().longValue());
                totalValue = totalValue.add(new BigDecimal(bean.getTotalCount().longValue()));
            }else{
                dataDetail.setValue(bean.getTotalRewardPoints().longValue());
                totalValue = totalValue.add(new BigDecimal(bean.getTotalRewardPoints().longValue()));
            }
            serieData.getData().add(dataDetail);
        }
        chartPieData.getDetailData().add(serieData);
        return chartPieData;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getMessageContent", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getMessageContent(@Valid CommonBeanGetRequest getRequest) {
        UserRewardPointRecord bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        if(bean.getMessageId()==null){
            return callback("没有相关消息记录");
        }else{
            if(bean.getRewardSource()==RewardSource.OPERATION){
                OperationLog ol = baseService.getObject(OperationLog.class,bean.getMessageId());
                if(ol==null){
                    return callback("未找到相关操作记录");
                }else{
//                    if(ol.getSystemFunction()!=null){
//                        return callback(ol.getSystemFunction().getName());
//                    }else{
//                        return callback("请求地址:"+ol.getUrlAddress());
//                    }
                    return callback("请求地址:"+ol.getUrlAddress()+"<br>\n"+"操作参数："+ol.getParas());
                }
            }else{
                UserMessage userMessage = baseService.getObject(UserMessage.class,bean.getMessageId());
                if(userMessage==null){
                    return callback("未找到相关消息记录");
                }else{
                    return callback(userMessage.getContent());
                }
            }
        }
    }
}
