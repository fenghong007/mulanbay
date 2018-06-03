package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.handler.SystemConfigHandler;
import cn.mulanbay.pms.persistent.bean.common.SystemFunctionBean;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.enums.*;
import cn.mulanbay.pms.persistent.service.DataService;
import cn.mulanbay.pms.persistent.service.LogService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonTreeSearch;
import cn.mulanbay.pms.web.bean.request.auth.SystemFunctionSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/10.
 */
@Controller
@RequestMapping("/systemFunction")
public class SystemFunctionController extends BaseController  {

    private static Class<SystemFunction> beanClass = SystemFunction.class;

    @Autowired
    DataService dataService;

    @Autowired
    LogService logService;

    @Autowired
    SystemConfigHandler systemConfigHandler;

    /**
     *
     * @return
     */
    @RequestMapping(value = "/getSystemFunctionMenuTree")
    @ResponseBody
    public ResultBean getSystemFunctionMenuTree() {
        try {
            List<SystemFunctionBean> list = dataService.getSystemFunctionMenu();
            List<TreeBean> treeBeans = new ArrayList<>();
            for(SystemFunctionBean sfb : list){
                TreeBean treeBean = new TreeBean();
                treeBean.setId(sfb.getId().toString());
                treeBean.setText(sfb.getName());
                treeBean.setPid(sfb.getPid()==null ? null : sfb.getPid().toString());
                treeBeans.add(treeBean);
            }
            TreeBean root = new TreeBean();
            root.setId("0");
            root.setText("根");

            TreeBean result = TreeBeanUtil.changToTree(root,treeBeans);
            List<TreeBean> resultList = new ArrayList<TreeBean>();
            resultList.add(result);
            return callback(resultList);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取商品类型树异常",
                    e);
        }
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/getSystemFunctionTree")
    @ResponseBody
    public ResultBean getSystemFunctionTree(CommonTreeSearch cts) {

        try {
            SystemFunctionSearch sf = new SystemFunctionSearch();
            sf.setPage(-1);
            PageResult<SystemFunction> pr =this.getSystemFunctionResult(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<SystemFunction> gtList = pr.getBeanList();
            for (SystemFunction gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            if (cts.getNeedRoot() != null && cts.getNeedRoot()) {
                TreeBean root = new TreeBean();
                root.setId(null);
                root.setText("根");
                root.setChildren(list);
                List<TreeBean> newList = new ArrayList<TreeBean>();
                newList.add(root);
                return callback(newList);
            } else {
                return callback(list);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取公司树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "auth/systemFunctionList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(SystemFunctionSearch sf) {
        return callbackDataGrid(getSystemFunctionResult(sf));
    }

    private PageResult<SystemFunction> getSystemFunctionResult(SystemFunctionSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort1 =new Sort("groupId",Sort.ASC);
        pr.addSort(sort1);
        Sort sort =new Sort("orderIndex",Sort.ASC);
        pr.addSort(sort);
        PageResult<SystemFunction> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(SystemFunction bean) {
        if(bean.getParent()!=null&&bean.getParent().getId()==null){
            //如果外键为空springmvc会默认设置一个空对象，hibernate保存会失败
            bean.setParent(null);
        }
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
        return callback(null);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(Long id) {
        SystemFunction br=baseService.getObject(SystemFunction.class, id);
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(SystemFunction bean) {
        if(bean.getParent()!=null&&bean.getParent().getId()==null){
            //如果外键为空springmvc会默认设置一个空对象，hibernate保存会失败
            bean.setParent(null);
        }
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
    public ResultBean delete(String ids) {
        baseService.deleteObjects(SystemFunction.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
        return callback(null);
    }

    /**
     * 刷新系统缓存
     *
     * @return
     */
    @RequestMapping(value = "/refreshSystemConfig", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean refreshSystemConfig() {
        systemConfigHandler.reloadFunctions();
        return callback(null);
    }

    /**
     * 刷新系统缓存
     *
     * @return
     */
    @RequestMapping(value = "/initUnConfigedFunctions", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean initUnConfigedFunctions() {
        List<Object[]> list = logService.getUnConfigedUrlsForOperationLog();
        for(Object[] ss : list){
            SystemFunction sf = new SystemFunction();
            sf.setName("自动设置");
            sf.setSupportMethods(ss[1].toString());
            sf.setUrlAddress(ss[0].toString());
            sf.setUrlType(UrlType.NORMAL);
            sf.setFunctionType(FunctionType.CREATE);
            sf.setFunctionDataType(FunctionDataType.NORMAL);
            sf.setStatus(CommonStatus.ENABLE);
            sf.setTriggerStat(false);
            sf.setIdField("id");
            sf.setIdFieldType(IdFieldType.LONG);
            sf.setOrderIndex( 0);
            sf.setDoLog(true);
            sf.setCreatedTime(new Date());
            sf.setDiffUser(true);
            sf.setRecordReturnData(false);
            sf.setLoginAuth(true);
            sf.setPermissionAuth(false);
            sf.setIpAuth(false);
            sf.setAutoLogin(false);
            sf.setRequestLimit(false);
            sf.setRequestLimitPeriod(5);
            sf.setDayLimit(0);
            sf.setRewardPoint(0);
            sf.setErrorCode(0);
            baseService.saveObject(sf);
        }
        return callback(null);
    }




}
