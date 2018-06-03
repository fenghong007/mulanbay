package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.bean.UserBehaviorDataStat;
import cn.mulanbay.pms.persistent.domain.*;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.DataService;
import cn.mulanbay.pms.persistent.service.UserBehaviorService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.data.UserBehaviorCompareSearch;
import cn.mulanbay.pms.web.bean.request.data.UserBehaviorStatSearch;
import cn.mulanbay.pms.web.bean.request.userbehavior.*;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarCompareData;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarCompareRowData;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarData;
import cn.mulanbay.pms.web.bean.response.chart.ChartCalandarPieData;
import cn.mulanbay.pms.web.bean.response.user.UserOperationResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/2/2.
 * 用户行为分析配置
 */
@Controller
@RequestMapping("/userBehavior")
public class UserBehaviorController extends BaseController {

    private static Logger logger = Logger.getLogger(UserBehaviorController.class);

    private static Class<UserBehavior> beanClass = UserBehavior.class;

    @Autowired
    DataService dataService;

    @Autowired
    UserBehaviorService userBehaviorService;

    /**
     * 获取用户行为配置模板树
     *
     * @return
     */
    @RequestMapping(value = "/getUserBehaviorConfigTree")
    @ResponseBody
    public ResultBean getUserBehaviorConfigTree(UserBehaviorConfigToUserTreeSearch sf) {
        try {
            List<UserBehaviorConfig> ubcs = userBehaviorService.getUserBehaviorConfigList(sf.getLevel());
            List<TreeBean> list = new ArrayList<TreeBean>();
            for (UserBehaviorConfig ubc : ubcs) {
                TreeBean tb = new TreeBean();
                tb.setId(ubc.getId().toString());
                tb.setText(ubc.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list, sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取用户行为配置模板树异常",
                    e);
        }
    }

    /**
     * 获取用户行为配置树
     *
     * @return
     */
    @RequestMapping(value = "/getUserBehaviorTree")
    @ResponseBody
    public ResultBean getUserBehaviorTree(UserBehaviorTreeSearch sf) {
        try {
            sf.setStatus(CommonStatus.ENABLE);
            PageRequest pr = sf.buildQuery();
            pr.setBeanClass(beanClass);
            List<UserBehavior> ubcs = baseService.getBeanList(pr);
            List<TreeBean> list = new ArrayList<TreeBean>();
            for (UserBehavior ubc : ubcs) {
                TreeBean tb = new TreeBean();
                tb.setId(ubc.getId().toString());
                tb.setText(ubc.getTitle());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list, sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取用户行为配置模板树异常",
                    e);
        }
    }


    @RequestMapping(value = "list")
    public String list() {
        return "userBehavior/userBehaviorList";
    }

    /**
     * 获取加班记录列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserBehaviorSearch sf) {
        PageResult<UserBehavior> pr = getUserBehaviorData(sf);
        return callbackDataGrid(pr);
    }

    private PageResult<UserBehavior> getUserBehaviorData(UserBehaviorSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<UserBehavior> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid UserBehaviorCreateRequest formRequest) {
        UserBehavior bean = new UserBehavior();
        UserBehaviorConfig unc = userBehaviorService.getUserBehaviorConfig(formRequest.getUserBehaviorConfigId(), formRequest.getLevel());
        if (unc == null) {
            return callbackErrorCode(PmsErrorCode.USER_BEHAVIOR_CONFIG_WITH_LEVEL_NOT_FOUND);
        }
        bean.setUserBehaviorConfig(unc);
        BeanCopy.copyProperties(formRequest, bean);
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
        return callback(bean);
    }


    /**
     * 获取
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest ubg) {
        UserBehavior bean = baseService.getObjectWithUser(beanClass, ubg.getId(), ubg.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid UserBehaviorEditRequest formRequest) {
        UserBehavior bean = baseService.getObjectWithUser(beanClass, formRequest.getId(), formRequest.getUserId());
        //不支持修改模板
        BeanCopy.copyProperties(formRequest, bean);
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
    public ResultBean delete(@Valid CommonBeanDeleteRequest cbd) {
        baseService.deleteObjectsWithUser(beanClass, NumberUtil.stringArrayToIntegerArray(cbd.getIds().split(",")), cbd.getUserId());
        return callback(null);
    }


    @RequestMapping(value = "statList")
    public String statList() {
        return "userBehavior/userBehaviorStatList";
    }

    /**
     * 用户行为统计（带饼图分析）
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(@Valid UserBehaviorStatSearch sf) {
        if (sf.getDateGroupType() == DateGroupType.MONTH) {
            return callback(this.createMonthStat(sf));
        } else {
            return callback(this.createYearStat(sf));
        }
    }

    private ChartCalandarPieData createMonthStat(UserBehaviorStatSearch sf) {
        Date date = DateUtil.getDate(sf.getYear() + "-" + sf.getMonth() + "-01", DateUtil.FormatDay1);
        Date startDate = DateUtil.getFirstDayOfMonth(date);
        Date endDate = DateUtil.getLastDayOfMonth(date);        //界面传过来的只有开始时间，需要转换为当月第一天及最后一天
        ChartCalandarPieData pieData = new ChartCalandarPieData(sf.getBehaviorType());
        String monthString = DateUtil.getFormatDate(startDate, "yyyyMM");
        pieData.setTitle(monthString + "用户行为分析");
        pieData.setStartDate(startDate);
        List<UserBehaviorDataStat> list = userBehaviorService.statUserBehaviorData(sf.getBehaviorType(), startDate, endDate, sf.getUserId(), sf.getName(), true, sf.getUserBehaviorId());
        for (UserBehaviorDataStat stat : list) {
            pieData.addData(stat.getDate(), stat.getName(), stat.getValue(), stat.getDateRegion(), Integer.valueOf(stat.getValue().toString()));
        }
        return pieData;
    }

    private ChartCalandarData createYearStat(UserBehaviorStatSearch sf) {
        if (sf.getUserBehaviorId() == null) {
            throw new ApplicationException(PmsErrorCode.USER_BEHAVIOR_CONFIG_NULL);
        }
        Date startDate = DateUtil.getDate(sf.getYear() + "-01-01 00:00:00", DateUtil.Format24Datetime);
        Date endDate = DateUtil.getDate(sf.getYear() + "-12-31 23:59:59", DateUtil.Format24Datetime);
        UserBehavior userBehavior = baseService.getObject(UserBehavior.class, sf.getUserBehaviorId());
        List<UserBehaviorDataStat> list = userBehaviorService.statUserBehaviorData(userBehavior, startDate, endDate, sf.getUserId(), sf.getName());
        ChartCalandarData chartData = new ChartCalandarData();
        chartData.setTitle(sf.getYear() + "年用户行为(" + userBehavior.getTitle() + ")分析");
        chartData.setCount(list.size());
        chartData.setYear(sf.getYear());
        chartData.setLegendData(userBehavior.getTitle(), 3);
        chartData.setUnit(userBehavior.getUserBehaviorConfig().getUnit());
        for (UserBehaviorDataStat stat : list) {
            double vv = Double.valueOf(stat.getValue().toString());
            //这时value就是天数值
            chartData.addSerieData(stat.getDate(), vv, stat.getDateRegion(), Integer.valueOf(stat.getValue().toString()));
        }
        return chartData;
    }

    /**
     * 数据比对
     *
     * @return
     */
    @RequestMapping(value = "compareList")
    public String compareList() {
        return "userBehavior/userBehaviorCompareList";
    }

    /**
     * 用户行为统计（带饼图分析）
     *
     * @return
     */
    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean compare(@Valid UserBehaviorCompareSearch sf) {
        Date startDate = DateUtil.getDate(sf.getYear() + "-01-01 00:00:00", DateUtil.Format24Datetime);
        Date endDate = DateUtil.getDate(sf.getYear() + "-12-31 23:59:59", DateUtil.Format24Datetime);
        ChartCalandarCompareData chartData = new ChartCalandarCompareData();
        chartData.setTitle("用户行为对比");
        chartData.setUnit("");
        chartData.setYear(sf.getYear());
        UserBehavior sourceUserBehavior = baseService.getObject(UserBehavior.class, sf.getSourceUserBehaviorId());
        List<UserBehaviorDataStat> sourceList = userBehaviorService.statUserBehaviorData(sourceUserBehavior, startDate, endDate, sf.getUserId(), sf.getSourceName());
        for (UserBehaviorDataStat stat : sourceList) {
            double vv = Double.valueOf(stat.getValue().toString());
            chartData.addSerieData(stat.getDate(), vv, stat.getDateRegion(), Integer.valueOf(stat.getValue().toString()), 1);
        }
        List<ChartCalandarCompareRowData> comapreRowDatas = new ArrayList<>();
        UserBehavior targetUserBehavior = baseService.getObject(UserBehavior.class, sf.getTargetUserBehaviorId());
        List<UserBehaviorDataStat> targetList = userBehaviorService.statUserBehaviorData(targetUserBehavior, startDate, endDate, sf.getUserId(), sf.getTargetName());
        for (UserBehaviorDataStat stat : targetList) {
            double vv = Double.valueOf(stat.getValue().toString());
            chartData.addSerieData(stat.getDate(), vv, stat.getDateRegion(), Integer.valueOf(stat.getValue().toString()), 2);
            ChartCalandarCompareRowData rowData = this.getCompareDate(stat.getDate(), sourceList);
            comapreRowDatas.add(rowData);
        }
        chartData.setCustomData(comapreRowDatas);
        String s1 = sourceUserBehavior.getTitle();
        if (sf.getSourceName() != null) {
            s1 += "(" + sf.getSourceName() + ")";
        }
        String s2 = targetUserBehavior.getTitle();
        if (sf.getTargetName() != null) {
            s2 += "(" + sf.getTargetName() + ")";
        }
        chartData.setLegendData(new String[]{s1, s2});
        return callback(chartData);
    }

    private ChartCalandarCompareRowData getCompareDate(String targetDate, List<UserBehaviorDataStat> sourceList) {
        ChartCalandarCompareRowData rowData = new ChartCalandarCompareRowData();
        Date t = DateUtil.getDate(targetDate, DateUtil.FormatDay1);
        for (UserBehaviorDataStat stat : sourceList) {
            Date s = DateUtil.getDate(stat.getDate(), DateUtil.FormatDay1);
            int days = DateUtil.getIntervalDays(s, t);
            if (days >= 0 && days <= 3) {
                rowData.setSourceDate(stat.getDate());
                rowData.setDays(days);
            }
        }
        rowData.setTargetDate(targetDate);
        return rowData;
    }

    /**
     * 获取用户行为配置的关键字列表
     *
     * @return
     */
    @RequestMapping(value = "/getUserBehaviorKeywordsTree")
    @ResponseBody
    public ResultBean getUserBehaviorKeywordsTree(Long id, Boolean needRoot) {

        try {
            List<String> dataList = userBehaviorService.getUserBehaviorKeywordsList(id);
            List<TreeBean> list = new ArrayList<TreeBean>();
            int i = 0;
            for (String ss : dataList) {
                TreeBean tb = new TreeBean();
                tb.setId(ss);
                tb.setText(ss);
                list.add(tb);
                i++;
            }
            return callback(TreeBeanUtil.addRoot(list, needRoot));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取用户行为配置的关键字列表异常",
                    e);
        }
    }


    @RequestMapping(value = "userOperationStatList")
    public String userOperationStatList() {
        return "userBehavior/userOperationStatList";
    }

    /**
     * 用户操作记录
     *
     * @return
     */
    @RequestMapping(value = "/userOperationStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean userOperationStat(@Valid UserOperationSearch sf) {
        int page =sf.getPage();
        int pageSize =sf.getPageSize();
        int index=0;
        List<UserOperationResponse> list = new ArrayList<>();
        //消费记录
        List<BuyRecord> brList = userBehaviorService.getUserOpertionList(BuyRecord.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"buyDate","buyDate");
        UserOperationResponse uor1= new UserOperationResponse();
        uor1.setId(index++);
        uor1.setTitle("消费记录");
        for(BuyRecord br : brList){
            uor1.addUserOperation(br.getBuyDate(),"["+br.getBuyType().getName()+"]"+"["+br.getGoodsType().getName()+"]"+br.getGoodsName());
        }
        list.add(uor1);
        List<MusicPractice> mpList = userBehaviorService.getUserOpertionList(MusicPractice.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"practiceDate","practiceDate");
        UserOperationResponse uor2= new UserOperationResponse();
        uor2.setId(index++);
        uor2.setTitle("音乐练习记录");
        for(MusicPractice br : mpList){
            uor2.addUserOperation(br.getPracticeDate(),"["+br.getMusicInstrument().getName()+"]"+br.getMinutes()+"分钟");
        }
        list.add(uor2);
        List<SportExercise> seList = userBehaviorService.getUserOpertionList(SportExercise.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"exerciseDate","exerciseDate");
        UserOperationResponse uor3= new UserOperationResponse();
        uor3.setId(index++);
        uor3.setTitle("锻炼记录");
        for(SportExercise br : seList){
            uor3.addUserOperation(br.getExerciseDate(),"["+br.getSportType().getName()+"]"+br.getMinutes()+"分钟");
        }
        list.add(uor3);
        List<TreatRecord> trList = userBehaviorService.getUserOpertionList(TreatRecord.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"treatDate","treatDate");
        UserOperationResponse uor4= new UserOperationResponse();
        uor4.setId(index++);
        uor4.setTitle("看病记录");
        for(TreatRecord br : trList){
            uor4.addUserOperation(br.getTreatDate(),"["+br.getHospital()+"]看病:"+br.getDisease());
        }
        list.add(uor4);
        List<BodyAbnormalRecord> barList = userBehaviorService.getUserOpertionList(BodyAbnormalRecord.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"occurDate","occurDate");
        UserOperationResponse uor5= new UserOperationResponse();
        uor5.setId(index++);
        uor5.setTitle("身体不适记录");
        for(BodyAbnormalRecord br : barList){
            uor5.addUserOperation(br.getOccurDate(),br.getDisease());
        }
        list.add(uor5);
        List<LifeExperience> leList = userBehaviorService.getUserOpertionList(LifeExperience.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"startDate","startDate");
        UserOperationResponse uor6= new UserOperationResponse();
        uor6.setId(index++);
        uor6.setTitle("人生经历记录");
        for(LifeExperience br : leList){
            uor6.addUserOperation(br.getStartDate(),"["+br.getTypeName()+"]"+br.getName());
        }
        list.add(uor6);
        List<ReadingRecordDetail> rrdList = userBehaviorService.getUserOpertionList(ReadingRecordDetail.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"readTime","readTime");
        UserOperationResponse uor7= new UserOperationResponse();
        uor7.setId(index++);
        uor7.setTitle("阅读记录");
        for(ReadingRecordDetail br : rrdList){
            uor7.addUserOperation(br.getReadTime(),"["+br.getReadingRecord().getBookName()+"]"+br.getMinutes()+"分钟");
        }
        list.add(uor7);
        List<WorkOvertime> wrList = userBehaviorService.getUserOpertionList(WorkOvertime.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"workDate","workDate");
        UserOperationResponse uor8= new UserOperationResponse();
        uor8.setId(index++);
        uor8.setTitle("加班记录");
        for(WorkOvertime br : wrList){
            uor8.addUserOperation(br.getWorkDate(),"["+br.getCompany().getName()+"]加班"+br.getHours()+"小时");
        }
        list.add(uor8);
        List<CommonRecord> crList = userBehaviorService.getUserOpertionList(CommonRecord.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"occurTime","occurTime");
        UserOperationResponse uor10= new UserOperationResponse();
        uor10.setId(index++);
        uor10.setTitle("通用记录");
        for(CommonRecord br : crList){
            uor10.addUserOperation(br.getOccurTime(),"["+br.getCommonRecordType().getName()+"]"+br.getName()+br.getValue()+br.getCommonRecordType().getUnit());
        }
        list.add(uor10);
        List<BusinessTrip> btList = userBehaviorService.getUserOpertionList(BusinessTrip.class,page,pageSize,sf.getStartTime(),sf.getEndTime(),sf.getUserId(),"tripDate","tripDate");
        UserOperationResponse uor9= new UserOperationResponse();
        uor9.setId(index++);
        uor9.setTitle("出差记录");
        for(BusinessTrip br : btList){
            uor9.addUserOperation(br.getTripDate(),"["+br.getCompany().getName()+"]出差"+br.getDays()+"天");
        }
        list.add(uor9);

        return callback(list);
    }
}