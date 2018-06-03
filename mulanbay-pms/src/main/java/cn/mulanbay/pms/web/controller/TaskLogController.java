package cn.mulanbay.pms.web.controller;

import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.handler.PmsScheduleHandler;
import cn.mulanbay.pms.web.bean.request.schedule.TaskLogSearch;
import cn.mulanbay.schedule.domain.TaskLog;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/taskLog")
public class TaskLogController extends BaseController {

    private static Class<TaskLog> beanClass = TaskLog.class;

    @Autowired
    PmsScheduleHandler pmsScheduleHandler;

    @RequestMapping(value = "list")
    public String list() {
        return "schedule/taskLogList";
    }

    /**
     * 获取任务列表
     * 使用RequestParam 方式是因为easyui 的datagrid使用ajax请求时对于list多值类型的参数参数名会带上中挂号
     * 且无法在js请求方式里设置使用 traditional:true参数
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(TaskLogSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s1 = new Sort("startTime", Sort.DESC);
        pr.addSort(s1);
        PageResult<TaskLog> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 重做
     * @return
     */
    @RequestMapping(value = "/redo", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean redo(Long id) {
        pmsScheduleHandler.manualRedo(id,false);
        return callback(null);
    }
}
