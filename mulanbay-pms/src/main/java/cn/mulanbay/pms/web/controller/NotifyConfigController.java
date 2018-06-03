package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.NotifyConfig;
import cn.mulanbay.pms.persistent.enums.BussType;
import cn.mulanbay.pms.persistent.service.NotifyService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.notify.NotifyConfigFormRequest;
import cn.mulanbay.pms.web.bean.request.notify.NotifyConfigSearch;
import cn.mulanbay.pms.web.bean.request.notify.NotifyConfigTreeSearch;
import cn.mulanbay.pms.web.bean.request.notify.UserNotifyConfigTreeSearch;
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
 * Created by fenghong on 2017/2/2.
 * 提醒配置,用于提醒所有的个人事项
 */
@Controller
@RequestMapping("/notifyConfig")
public class NotifyConfigController  extends BaseController  {

    private static Class<NotifyConfig> beanClass = NotifyConfig.class;

    @Autowired
    NotifyService notifyService;

    /**
     * 提醒配置模板选项列表(用户使用，需要判断用户级别)
     * @return
     */
    @RequestMapping(value = "/getNotifyConfigForUserTree")
    @ResponseBody
    public ResultBean getNotifyConfigForUserTree(UserNotifyConfigTreeSearch sf) {
        try {
            List<NotifyConfig> gtList = notifyService.getNotifyConfigList(sf.getLevel());
            List<TreeBean> list = this.createNotifyConfigTree(gtList);
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
    @RequestMapping(value = "/getNotifyConfigTree")
    @ResponseBody
    public ResultBean getNotifyConfigTree(NotifyConfigTreeSearch sf) {

        try {
            NotifyConfigSearch ncSearch = new NotifyConfigSearch();
            ncSearch.setPage(-1);
            ncSearch.setRelatedBeans(sf.getRelatedBeans());
            PageResult<NotifyConfig> pr = getNotifyConfigData(ncSearch);
            List<NotifyConfig> gtList = pr.getBeanList();
            List<TreeBean> list = this.createNotifyConfigTree(gtList);
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取乐器树异常",
                    e);
        }
    }

    private List<TreeBean> createNotifyConfigTree(List<NotifyConfig> gtList){
        List<TreeBean> list = new ArrayList<TreeBean>();
        Map<String,List<NotifyConfig>> map = this.changeToNotifyConfigMap(gtList);
        for(String key : map.keySet()){
            TreeBean tb = new TreeBean();
            BussType bt = BussType.getBussType(key);
            tb.setId("");
            if(bt==null){
                tb.setText("未分类");
            }else{
                tb.setText(bt.getName());
            }
            List<NotifyConfig> ll = map.get(key);
            for(NotifyConfig nc : ll){
                TreeBean child = new TreeBean();
                child.setId(nc.getId().toString());
                child.setText(nc.getTitle());
                tb.addChild(child);
            }
            list.add(tb);
        }
        return list;
    }

    private Map<String,List<NotifyConfig>> changeToNotifyConfigMap(List<NotifyConfig> gtList ){
        Map<String,List<NotifyConfig>> map = new TreeMap<>();
        for(NotifyConfig nc : gtList){
            List<NotifyConfig> list = map.get(nc.getRelatedBeans());
            if(list==null){
                list= new ArrayList<>();
            }
            list.add(nc);
            map.put(nc.getRelatedBeans(),list);
        }
        return map;
    }

    @RequestMapping(value = "list")
    public String list() {
        return "notify/notifyConfigList";
    }

    /**
     * 获取加班记录列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(NotifyConfigSearch sf) {
        return callbackDataGrid(getNotifyConfigData(sf));
    }

    private PageResult<NotifyConfig> getNotifyConfigData(NotifyConfigSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<NotifyConfig> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid NotifyConfigFormRequest formRequest) {
        NotifyConfig bean = new NotifyConfig();
        BeanCopy.copyProperties(formRequest,bean);
        checkNotifyConfig(bean);
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
        NotifyConfig br=baseService.getObject(NotifyConfig.class, getRequest.getId());
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid NotifyConfigFormRequest formRequest) {
        NotifyConfig bean=baseService.getObject(NotifyConfig.class, formRequest.getId());
        BeanCopy.copyProperties(formRequest,bean);
        checkNotifyConfig(bean);
        bean.setLastModifyTime(new Date());
        baseService.updateObject(bean);
        return callback(null);
    }

    public void checkNotifyConfig(NotifyConfig bean){
        //notifyService.getNotifyResult(bean,this.getCurrentUserId(),0);
    }

    /**
     * 删除
     * todo 该功能其实不能提供，即使提供也要级联删除
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        baseService.deleteObjects(NotifyConfig.class, NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")));
        return callback(null);
    }

}
