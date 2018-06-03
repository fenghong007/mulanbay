package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.domain.PlanConfig;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.PlanType;
import cn.mulanbay.pms.persistent.service.PlanService;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.plan.PlanConfigForUserTreeSearch;
import cn.mulanbay.pms.web.bean.request.plan.PlanConfigFormRequest;
import cn.mulanbay.pms.web.bean.request.plan.PlanConfigSearch;
import cn.mulanbay.pms.web.bean.request.plan.PlanConfigTreeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.plan.PlanConfigResponse;
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
 * 计划配置
 */
@Controller
@RequestMapping("/planConfig")
public class PlanConfigController extends BaseController  {

    private static Logger logger = Logger.getLogger(PlanConfigController.class);

    private static Class<PlanConfig> beanClass = PlanConfig.class;

    @Autowired
    PlanService planService;

    /**
     * 需要绑定用户级别
     *
     * @return
     */
    @RequestMapping(value = "/getPlanConfigForUserTree")
    @ResponseBody
    public ResultBean getPlanConfigForUserTree(PlanConfigForUserTreeSearch sf) {
        return getPlanConfigTree(sf);
    }

    /**
     * 为锻炼管理界面的下拉菜单使用
     * @return
     */
    @RequestMapping(value = "/getPlanConfigTree")
    @ResponseBody
    public ResultBean getPlanConfigTree(PlanConfigTreeSearch planConfigTreeSearch) {
        try {
            //系统模板
            List<PlanConfig> list = this.getPlanConfigList(null,planConfigTreeSearch);
            List<TreeBean> result = new ArrayList<>();
            PlanType current = list.get(0).getPlanType();
            TreeBean typeTreeBean = new TreeBean();
            typeTreeBean.setId("");
            typeTreeBean.setText(current.getName());
            int n = list.size();
            for(int i=0;i<n;i++ ){
                PlanConfig pc = list.get(i);
                if(pc.getPlanType()==current){
                    TreeBean tb = new TreeBean();
                    tb.setId(pc.getId().toString());
                    tb.setText(pc.getName());
                    typeTreeBean.addChild(tb);
                }
                if(pc.getPlanType()!=current){
                    current =pc.getPlanType();
                    result.add(typeTreeBean);
                    typeTreeBean = new TreeBean();
                    typeTreeBean.setId("");
                    typeTreeBean.setText(current.getName());
                }
                if(i==n-1){
                    //最后一个
                    TreeBean tb = new TreeBean();
                    tb.setId(pc.getId().toString());
                    tb.setText(pc.getName());
                    typeTreeBean.addChild(tb);
                    result.add(typeTreeBean);
                }
            }
            return callback(result);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取计划配置树异常",
                    e);
        }
    }

    private List<PlanConfig> getPlanConfigList(Long userId,PlanConfigTreeSearch planConfigTreeSearch){
        PlanConfigSearch sf = new PlanConfigSearch();
        sf.setRelatedBeans(planConfigTreeSearch.getRelatedBeans());
        sf.setStatus(CommonStatus.ENABLE);
        sf.setUserId(userId);
        sf.setLevel(planConfigTreeSearch.getLevel());
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("planType", Sort.ASC);
        Sort s2 = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        pr.addSort(s2);
        List<PlanConfig> list = baseService.getBeanList(pr);
        return list;
    }

    @RequestMapping(value = "list")
    public String list() {
        return "plan/planConfigList";
    }

    /**
     * 获取加班记录列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(PlanConfigSearch sf) {
        PageResult<PlanConfig> pr = getPlanConfigData(sf);
        if(sf.getStatNow()!=null&&sf.getStatNow()){
            //需要统计当前的报表
            Date now = new Date();
            long userId =sf.getUserId();
            PageResult<PlanConfigResponse> result = new PageResult<>();
            List<PlanConfigResponse> list = new ArrayList<>();
            for(PlanConfig pc : pr.getBeanList()){
                PlanConfigResponse response = new PlanConfigResponse();
                BeanCopy.copyProperties(pc,response);
            }
            result.setBeanList(list);
            result.setMaxRow(pr.getMaxRow());
            result.setPageSize(pr.getPageSize());
            return callbackDataGrid(result);
        }else{
            return callbackDataGrid(pr);
        }
    }

    private PageResult<PlanConfig> getPlanConfigData(PlanConfigSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<PlanConfig> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid PlanConfigFormRequest formRequest) {
        PlanConfig bean = new PlanConfig();
        BeanCopy.copyProperties(formRequest,bean);
        checkPlanConfig(bean);
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
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        PlanConfig bean=baseService.getObject(PlanConfig.class, getRequest.getId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid PlanConfigFormRequest formRequest) {
        PlanConfig bean=baseService.getObject(PlanConfig.class, formRequest.getId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setLastModifyTime(new Date());
        baseService.updateObject(bean);
        return callback(bean);
    }

    /**
     * 检查计划配置
     * @param bean
     */
    private void checkPlanConfig(PlanConfig bean){
        //先计算返回列是否满足要求
        calculateResultColumns(bean);
        //再计算查询条件是否满足要求
        Date now = new Date();
        try {
//            planService.statPlanReport(bean,now,bean.getUserId(), PlanReportDataStatFilterType.ORIGINAL);
//            planService.statPlanReport(bean,now,bean.getUserId(), PlanReportDataStatFilterType.NO_USER);
//            planService.statPlanReport(bean,now,bean.getUserId(), PlanReportDataStatFilterType.NO_DATE);
//            planService.statPlanReport(bean,now,bean.getUserId(), PlanReportDataStatFilterType.NONE);
        } catch (Exception e) {
            logger.error("检查计划配置异常",e);
            throw new ApplicationException(PmsErrorCode.PLAN_CONFIG_SQL_ERROR);
        }

    }
    /**
     * 计算返回列的数量
     * select,from都需要小写
     * @param bean
     * @return
     */
    private int calculateResultColumns(PlanConfig bean){
        //先获取第一个select与from之间的字符，根据逗号分析数量
        String sqlContent = bean.getSqlContent();
        int first = sqlContent.indexOf("select");
        int last = sqlContent.indexOf("from");
        String column = sqlContent.substring(first,last);
        //如果没有一个逗号，说明是1个
        int n = StringUtil.countChar(column,",")+1;
        if(n!=2){
            //抛异常
            throw new ApplicationException(PmsErrorCode.PLAN_CONFIG_SQL_RETURN_COLUMN_ERROR);
        }
        //再验证与resultTemplate比对数量是否一致
        return n;
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        for(String s : deleteRequest.getIds().split(",")){
            planService.deletePlanConfig(Long.valueOf(s));
        }
        return callback(null);
    }

    /**
     * 获取首次统计日期
     *
     * @return
     */
    @RequestMapping(value = "/getFirstStatDay")
    @ResponseBody
    public ResultBean getFirstStatDay(PlanConfig bean) {
        Date date = null;
        try {
            bean.setUserId(bean.getUserId());
            date = planService.getFirstStatDay(bean);
        } catch (Exception e) {
            logger.error("getFirstStatDay error",e);
            return callback(null);
        }
        return callback(date);
    }

}
