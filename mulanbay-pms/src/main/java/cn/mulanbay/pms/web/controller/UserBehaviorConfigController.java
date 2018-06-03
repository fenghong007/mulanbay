package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.UserBehaviorConfig;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.UserBehaviorType;
import cn.mulanbay.pms.persistent.service.DataService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.CommonTreeSearch;
import cn.mulanbay.pms.web.bean.request.data.UserBehaviorConfigSearch;
import cn.mulanbay.pms.web.bean.request.data.UserBehaviorConfigTreeSearch;
import cn.mulanbay.pms.web.bean.request.userbehavior.UserBehaviorConfigFormRequest;
import cn.mulanbay.pms.web.bean.response.TreeBean;
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
@RequestMapping("/userBehaviorConfig")
public class UserBehaviorConfigController extends BaseController  {

    private static Logger logger = Logger.getLogger(UserBehaviorConfigController.class);

    private static Class<UserBehaviorConfig> beanClass = UserBehaviorConfig.class;

    @Autowired
    DataService dataService;

    /**
     * 获取类型树
     * @return
     */
    @RequestMapping(value = "/getUserBehaviorTypeTree")
    @ResponseBody
    public ResultBean getUserBehaviorTypeTree(CommonTreeSearch sf) {
        try {
            List<TreeBean> list = new ArrayList<TreeBean>();
            for(UserBehaviorType type :UserBehaviorType.values()){
                TreeBean tb = new TreeBean();
                tb.setId(type.toString());
                tb.setText(type.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取乐器树异常",
                    e);
        }
    }

    /**
     * 为锻炼管理界面的下拉菜单使用
     * @return
     */
    @RequestMapping(value = "/getUserBehaviorConfigTree")
    @ResponseBody
    public ResultBean getUserBehaviorConfigTree(UserBehaviorConfigTreeSearch sf) {
        try {
            sf.setStatus(CommonStatus.ENABLE);
            UserBehaviorConfigSearch ubcs = new UserBehaviorConfigSearch();
            BeanCopy.copyProperties(sf,ubcs);
            PageResult<UserBehaviorConfig> pr = getUserBehaviorConfigData(ubcs);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<UserBehaviorConfig> gtList = pr.getBeanList();
            for (UserBehaviorConfig gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取乐器树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "userBehavior/userBehaviorConfigList";
    }

    /**
     * 获取加班记录列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserBehaviorConfigSearch sf) {
        PageResult<UserBehaviorConfig> pr = getUserBehaviorConfigData(sf);
        return callbackDataGrid(pr);
    }

    private PageResult<UserBehaviorConfig> getUserBehaviorConfigData(UserBehaviorConfigSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<UserBehaviorConfig> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid UserBehaviorConfigFormRequest formRequest) {
        UserBehaviorConfig bean = new UserBehaviorConfig();
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
        UserBehaviorConfig bean=baseService.getObject(beanClass,getRequest.getId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid UserBehaviorConfigFormRequest formRequest) {
        UserBehaviorConfig bean =baseService.getObject(beanClass,formRequest.getId());
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
        for(String s : deleteRequest.getIds().split(",")){
            baseService.deleteObject(UserBehaviorConfig.class,Long.valueOf(s));
        }
        return callback(null);
    }

}
