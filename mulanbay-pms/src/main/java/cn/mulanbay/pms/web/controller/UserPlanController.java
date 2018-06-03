package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.persistent.domain.PlanConfig;
import cn.mulanbay.pms.persistent.domain.PlanReport;
import cn.mulanbay.pms.persistent.domain.UserPlan;
import cn.mulanbay.pms.persistent.domain.UserPlanRemind;
import cn.mulanbay.pms.persistent.enums.PlanReportDataStatFilterType;
import cn.mulanbay.pms.persistent.enums.PlanType;
import cn.mulanbay.pms.persistent.service.PlanService;
import cn.mulanbay.pms.persistent.service.UserPlanService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanFormRequest;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanRemindFormRequest;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanSearch;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanTreeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.plan.UserPlanResponse;
import cn.mulanbay.web.bean.response.ResultBean;
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
 * Created by fenghong on 2017/1/21.
 */
@Controller
@RequestMapping("/userPlan")
public class UserPlanController extends BaseController {
    

    private static Class<UserPlan> beanClass = UserPlan.class;

    @Autowired
    UserPlanService userPlanService;

    @Autowired
    PlanService planService;

    @Autowired
    CacheHandler cacheHandler;


    /**
     * 用户计划树
     * @return
     */
    @RequestMapping(value = "/getPlanConfigTree")
    @ResponseBody
    public ResultBean getPlanConfigTree(UserPlanTreeSearch sf) {
        try {
            // 用户模板
            List<UserPlan> list = planService.getUserPlanList(sf.getUserId(),sf.getRelatedBeans(),sf.getPlanType());
            if(list.isEmpty()){
                return callback(TreeBeanUtil.addRoot(new ArrayList<>(),sf.getNeedRoot()));
            }
            List<TreeBean> result = new ArrayList<>();
            PlanType current = list.get(0).getPlanConfig().getPlanType();
            TreeBean typeTreeBean = new TreeBean();
            typeTreeBean.setId("");
            typeTreeBean.setText(current.getName());
            int n = list.size();
            for(int i=0;i<n;i++ ){
                UserPlan pc = list.get(i);
                if(pc.getPlanConfig().getPlanType()==current){
                    TreeBean tb = new TreeBean();
                    tb.setId(pc.getId().toString());
                    tb.setText(pc.getTitle());
                    typeTreeBean.addChild(tb);
                }
                if(pc.getPlanConfig().getPlanType()!=current){
                    current =pc.getPlanConfig().getPlanType();
                    result.add(typeTreeBean);
                    typeTreeBean = new TreeBean();
                    typeTreeBean.setId("");
                    typeTreeBean.setText(current.getName());
                    TreeBean tb = new TreeBean();
                    tb.setId(pc.getId().toString());
                    tb.setText(pc.getTitle());
                    typeTreeBean.addChild(tb);
                }
                if(i==n-1){
                    //最后一个
                    result.add(typeTreeBean);
                }
            }
            return callback(TreeBeanUtil.addRoot(result,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取用户计划树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "plan/userPlanList";
    }

    /**
     * 获取任务列表
     * 使用RequestParam 方式是因为easyui 的datagrid使用ajax请求时对于list多值类型的参数参数名会带上中挂号
     * 且无法在js请求方式里设置使用 traditional:true参数
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserPlanSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<UserPlan> qr = baseService.getBeanResult(pr);
        if(sf.getStatNow()!=null&&sf.getStatNow()){
            //需要统计当前的报表
            Date now = new Date();
            long userId =sf.getUserId();
            PageResult<UserPlanResponse> result = new PageResult<>();
            List<UserPlanResponse> list = new ArrayList<>();
            for(UserPlan pc : qr.getBeanList()){
                UserPlanResponse response = new UserPlanResponse();
                BeanCopy.copyProperties(pc,response);
                //设置PlanReport
                PlanReport report = planService.statPlanReport(pc,now,userId,sf.getFilterType());
                response.setPlanReport(report);
                list.add(response);
            }
            result.setBeanList(list);
            result.setMaxRow(qr.getMaxRow());
            result.setPageSize(pr.getPageSize());
            return callbackDataGrid(result);
        }else {
            return callbackDataGrid(qr);

        }
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid UserPlanFormRequest formRequest ) {
        UserPlan bean = new UserPlan();
        BeanCopy.copyProperties(formRequest,bean);
        PlanConfig planConfig = planService.getPlanConfig(formRequest.getPlanConfigId(),formRequest.getLevel());
        if(planConfig==null){
            return callbackErrorCode(PmsErrorCode.USER_ENTITY_NOT_ALLOWED);
        }
        bean.setPlanConfig(planConfig);
        userPlanService.saveUsePlan(bean);
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
        UserPlan bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getStat(@Valid CommonBeanGetRequest getRequest) {
        UserPlan userPlan=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        PlanReport planReport = planService.statPlanReport(userPlan,new Date(),getRequest.getUserId(), PlanReportDataStatFilterType.ORIGINAL);
        return callback(planReport);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid UserPlanFormRequest formRequest ) {
        UserPlan bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        PlanConfig planConfig = planService.getPlanConfig(formRequest.getPlanConfigId(),formRequest.getLevel());
        if(planConfig==null){
            return callbackErrorCode(PmsErrorCode.USER_ENTITY_NOT_ALLOWED);
        }
        bean.setPlanConfig(planConfig);
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
        String[] ss = deleteRequest.getIds().split(",");
        for(String s : ss){
            UserPlan bean = this.getUserEntity(beanClass,Long.valueOf(s),deleteRequest.getUserId());
            userPlanService.deleteUsePlan(bean);
        }
        return callback(null);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getRemind", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getRemind(@Valid CommonBeanGetRequest getRequest) {
        UserPlanRemind userPlanRemind = userPlanService.getRemindByUserPlan(getRequest.getId(),getRequest.getUserId());
        return callback(userPlanRemind);
    }

    /**
     * 增加/修改用户计划提醒
     *
     * @return
     */
    @RequestMapping(value = "/addOrEditRemind", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean addOrEditRemind(@Valid UserPlanRemindFormRequest formRequest ) {
        UserPlanRemind bean =null;
        if(formRequest.getId()!=null){
            bean = this.getUserEntity(UserPlanRemind.class,formRequest.getId(),formRequest.getUserId());
            BeanCopy.copyProperties(formRequest,bean);
            UserPlan userPlan = this.getUserEntity(UserPlan.class,formRequest.getUserPlanId(),formRequest.getUserId());
            bean.setUserPlan(userPlan);
            bean.setLastModifyTime(new Date());
            baseService.updateObject(bean);
            //只要修改过重新开始计算提醒
            cacheHandler.delete("userPlanNotify:"+this.getCurrentUserId()+":"+bean.getUserPlan().getId());
        }else{
            bean = new UserPlanRemind();
            BeanCopy.copyProperties(formRequest,bean);
            UserPlan userPlan = this.getUserEntity(UserPlan.class,formRequest.getUserPlanId(),formRequest.getUserId());
            bean.setUserPlan(userPlan);
            bean.setCreatedTime(new Date());
            baseService.saveObject(bean);
        }
        return callback(null);
    }

}
