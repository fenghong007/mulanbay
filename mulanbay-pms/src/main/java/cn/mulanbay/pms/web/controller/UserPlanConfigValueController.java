package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.UserPlan;
import cn.mulanbay.pms.persistent.domain.UserPlanConfigValue;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanConfigValueFormRequest;
import cn.mulanbay.pms.web.bean.request.plan.UserPlanConfigValueSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by fenghong on 2017/2/2.
 * 计划配置
 */
@Controller
@RequestMapping("/userPlanConfigValue")
public class UserPlanConfigValueController extends BaseController  {

    private static Class<UserPlanConfigValue> beanClass = UserPlanConfigValue.class;

    @RequestMapping(value = "list")
    public String list() {
        return "plan/UserPlanConfigValueValueList";
    }

    /**
     * 获取加班记录列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserPlanConfigValueSearch sf) {
        return callbackDataGrid(getUserPlanConfigValueData(sf));
    }

    private PageResult<UserPlanConfigValue> getUserPlanConfigValueData(UserPlanConfigValueSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("year", Sort.DESC);
        pr.addSort(s);
        PageResult<UserPlanConfigValue> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid UserPlanConfigValueFormRequest formRequest ) {
        UserPlanConfigValue bean = new UserPlanConfigValue();
        BeanCopy.copyProperties(formRequest,bean);
        UserPlan userPlan = this.getUserEntity(UserPlan.class,formRequest.getUserPlanId(),formRequest.getUserId());
        bean.setUserPlan(userPlan);
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
        UserPlanConfigValue bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid UserPlanConfigValueFormRequest formRequest) {
        UserPlanConfigValue bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        UserPlan userPlan = this.getUserEntity(UserPlan.class,formRequest.getUserPlanId(),formRequest.getUserId());
        bean.setUserPlan(userPlan);
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
}
