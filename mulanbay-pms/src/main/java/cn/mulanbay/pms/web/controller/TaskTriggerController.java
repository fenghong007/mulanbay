package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.handler.PmsScheduleHandler;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.schedule.TaskManualNewRequest;
import cn.mulanbay.pms.web.bean.request.schedule.TaskTriggerFormRequest;
import cn.mulanbay.pms.web.bean.request.schedule.TaskTriggerSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.schedule.domain.TaskTrigger;
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
@RequestMapping("/taskTrigger")
public class TaskTriggerController extends BaseController {
    

    private static Class<TaskTrigger> beanClass = TaskTrigger.class;

    @Autowired
    PmsScheduleHandler pmsScheduleHandler;

    @RequestMapping(value = "list")
    public String list() {
        return "schedule/taskTriggerList";
    }

    /**
     * 为锻炼管理界面的下拉菜单使用
     * @return
     */
    @RequestMapping(value = "/getTaskTriggerTree")
    @ResponseBody
    public ResultBean getTaskTriggerTree(String groupName,Boolean needRoot) {

        try {
            TaskTriggerSearch sf = new TaskTriggerSearch();
            sf.setGroupName(groupName);
            PageResult<TaskTrigger> pr = getTaskTrigger(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<TaskTrigger> gtList = pr.getBeanList();
            for (TaskTrigger gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,needRoot));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取运动类型树异常",
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
    public ResultBean getData(TaskTriggerSearch sf) {
        boolean isSchedule = pmsScheduleHandler.isEnableSchedule();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("isSchedule", isSchedule);
        PageResult<TaskTrigger> pr = getTaskTrigger(sf);
        data.put("total", pr.getMaxRow());
        data.put("rows", pr.getBeanList());
        data.put("currentlyExecutingJobsCount",
                pmsScheduleHandler.getCurrentlyExecutingJobsCount());
        data.put("scheduleJobsCount", pmsScheduleHandler.getScheduleJobsCount());
        return callback(data);
    }

    private PageResult<TaskTrigger> getTaskTrigger(TaskTriggerSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s1 = new Sort("triggerStatus", Sort.DESC);
        Sort s2 = new Sort("nextExecuteTime", Sort.ASC);
        pr.addSort(s1);
        pr.addSort(s2);
        PageResult<TaskTrigger> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid TaskTriggerFormRequest formRequest ) {
        TaskTrigger bean = new TaskTrigger();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setFailCount(0L);
        bean.setTotalCount(0L);
        //默认是系统的
        bean.setUserId(0L);
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
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        TaskTrigger br=baseService.getObject(TaskTrigger.class, getRequest.getId());
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid TaskTriggerFormRequest formRequest) {
        TaskTrigger bean = baseService.getObject(TaskTrigger.class,formRequest.getId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setModifyTime(new Date());
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
        baseService.deleteObjects(TaskTrigger.class, NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")));
        return callback(null);
    }
    /**
     * 手动执行
     *
     * @return
     */
    @RequestMapping(value = "/manualNew", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean manualNew(TaskManualNewRequest tmnr) {
        pmsScheduleHandler.manualNew(tmnr.getTaskTriggerId(),tmnr.getBussDate(),tmnr.isSync());
        return callback(null);
    }

}
