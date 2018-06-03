package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.persistent.bean.NotifyResult;
import cn.mulanbay.pms.persistent.domain.NotifyConfig;
import cn.mulanbay.pms.persistent.domain.UserNotify;
import cn.mulanbay.pms.persistent.domain.UserNotifyRemind;
import cn.mulanbay.pms.persistent.enums.BussType;
import cn.mulanbay.pms.persistent.service.NotifyService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.notify.UserNotifyFormRequest;
import cn.mulanbay.pms.web.bean.request.notify.UserNotifyRemindFormRequest;
import cn.mulanbay.pms.web.bean.request.notify.UserNotifySearch;
import cn.mulanbay.pms.web.bean.request.notify.UserNotifyTreeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by fenghong on 2017/1/21.
 */
@Controller
@RequestMapping("/userNotify")
public class UserNotifyController extends BaseController {
    

    private static Class<UserNotify> beanClass = UserNotify.class;

    @Autowired
    NotifyService notifyService;

    @Autowired
    CacheHandler cacheHandler;

    @RequestMapping(value = "list")
    public String list() {
        return "notify/userNotifyList";
    }

    private Map<String,List<UserNotify>> changeToUserNotifyMap(List<UserNotify> gtList ){
        Map<String,List<UserNotify>> map = new TreeMap<>();
        for(UserNotify nc : gtList){
            List<UserNotify> list = map.get(nc.getNotifyConfig().getRelatedBeans());
            if(list==null){
                list= new ArrayList<>();
            }
            list.add(nc);
            map.put(nc.getNotifyConfig().getRelatedBeans(),list);
        }
        return map;
    }


    /**
     * 为锻炼管理界面的下拉菜单使用
     * @return
     */
    @RequestMapping(value = "/getUserNotifyTree")
    @ResponseBody
    public ResultBean getUserNotifyTree(UserNotifyTreeSearch sf) {

        try {
            List<TreeBean> list = new ArrayList<TreeBean>();
            UserNotifySearch uns = new UserNotifySearch();
            BeanCopy.copyProperties(sf,uns);
            uns.setPage(-1);
            PageRequest pr = uns.buildQuery();
            pr.setBeanClass(beanClass);
            List<UserNotify> unList = baseService.getBeanList(pr);
            Map<String,List<UserNotify>> map = this.changeToUserNotifyMap(unList);
            for(String key : map.keySet()){
                TreeBean tb = new TreeBean();
                BussType bt = BussType.getBussType(key);
                tb.setId("");
                if(bt==null){
                    tb.setText("未分类");
                }else{
                    tb.setText(bt.getName());
                }
                List<UserNotify> ll = map.get(key);
                for(UserNotify nc : ll){
                    TreeBean child = new TreeBean();
                    child.setId(nc.getId().toString());
                    child.setText(nc.getTitle());
                    tb.addChild(child);
                }
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取乐器树异常",
                    e);
        }
    }

    /**
     * 获取任务列表
     * 使用RequestParam 方式是因为easyui 的datagrid使用ajax请求时对于list多值类型的参数参数名会带上中挂号
     * 且无法在js请求方式里设置使用 traditional:true参数
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserNotifySearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<UserNotify> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid UserNotifyFormRequest formRequest ) {
        UserNotify bean = new UserNotify();
        BeanCopy.copyProperties(formRequest,bean);
        NotifyConfig notifyConfig = notifyService.getNotifyConfig(formRequest.getNotifyConfigId(),formRequest.getLevel());
        if(notifyConfig==null){
            return callbackErrorCode(PmsErrorCode.USER_ENTITY_NOT_ALLOWED);
        }
        bean.setNotifyConfig(notifyConfig);
        checkUserNotify(bean);
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
        UserNotify bean=this.getUserEntity(beanClass,getRequest.getId(),getRequest.getUserId());
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
        UserNotify bean=this.getUserEntity(beanClass,getRequest.getId(),getRequest.getUserId());
        NotifyResult notifyResult = notifyService.getNotifyResult(bean,getRequest.getUserId());
        return callback(notifyResult);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid UserNotifyFormRequest formRequest ) {
        UserNotify bean = this.getUserEntity(beanClass,formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        NotifyConfig notifyConfig = notifyService.getNotifyConfig(formRequest.getNotifyConfigId(),formRequest.getLevel());
        if(notifyConfig==null){
            return callbackErrorCode(PmsErrorCode.USER_ENTITY_NOT_ALLOWED);
        }
        bean.setNotifyConfig(notifyConfig);
        checkUserNotify(bean);
        bean.setLastModifyTime(new Date());
        baseService.updateObject(bean);
        return callback(bean);
    }

    private void checkUserNotify(UserNotify userNotify){
        if(userNotify.getNotifyConfig().getNotifyType()== NotifyConfig.NotifyType.LESS){
            //小于类型
            if(userNotify.getWarningValue()>userNotify.getAlertValue()){
                throw new ApplicationException(PmsErrorCode.NOTIFY_WARNING_VALUE_LESS_ALERT);
            }
        }else{
            //大于类型
            if(userNotify.getWarningValue()<userNotify.getAlertValue()){
                throw new ApplicationException(PmsErrorCode.NOTIFY_WARNING_VALUE_LESS_ALERT);
            }
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
        this.deleteUserEntity(beanClass,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    /**
     * 获取
     * 这里的id为UserNotify的ID
     * @return
     */
    @RequestMapping(value = "/getRemind", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getRemind(@Valid CommonBeanGetRequest getRequest) {
        UserNotifyRemind userNotifyRemind = notifyService.getUserNotifyRemind(getRequest.getId(),getRequest.getUserId());
        return callback(userNotifyRemind);
    }

    /**
     * 增加/修改用户计划提醒
     *
     * @return
     */
    @RequestMapping(value = "/addOrEditRemind", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean addOrEditRemind(@Valid UserNotifyRemindFormRequest formRequest) {
        UserNotifyRemind bean =null;
        if(formRequest.getId()!=null){
            bean = this.getUserEntity(UserNotifyRemind.class,formRequest.getId(),formRequest.getUserId());
            BeanCopy.copyProperties(formRequest,bean);
            UserNotify userNotify = this.getUserEntity(UserNotify.class,formRequest.getUserNotifyId(),formRequest.getUserId());
            bean.setUserNotify(userNotify);
            bean.setLastModifyTime(new Date());
            baseService.updateObject(bean);
            //只要修改过重新开始计算提醒
            cacheHandler.delete("userNotify:"+this.getCurrentUserId()+":"+bean.getUserNotify().getId());
        }else{
            bean = new UserNotifyRemind();
            BeanCopy.copyProperties(formRequest,bean);
            UserNotify userNotify = this.getUserEntity(UserNotify.class,formRequest.getUserNotifyId(),formRequest.getUserId());
            bean.setUserNotify(userNotify);
            bean.setCreatedTime(new Date());
            baseService.saveObject(bean);
        }
        return callback(null);
    }

}
