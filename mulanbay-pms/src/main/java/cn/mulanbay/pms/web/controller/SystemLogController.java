package cn.mulanbay.pms.web.controller;

import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.SystemLog;
import cn.mulanbay.pms.persistent.service.LogService;
import cn.mulanbay.pms.web.bean.request.log.SystemLogSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fenghong on 2017/1/10.
 */
@Controller
@RequestMapping("/systemLog")
public class SystemLogController extends BaseController  {

    private static Class<SystemLog> beanClass = SystemLog.class;

    @Autowired
    LogService logService;

    @RequestMapping(value = "list")
    public String list() {
        return "log/systemLogList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(SystemLogSearch sf) {
        return callbackDataGrid(getSystemLogResult(sf));
    }

    private PageResult<SystemLog> getSystemLogResult(SystemLogSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("occurTime",Sort.DESC);
        pr.addSort(sort);
        PageResult<SystemLog> qr = baseService.getBeanResult(pr);
        return qr;
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getParas", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getParas(Long id) {
        SystemLog br=baseService.getObject(SystemLog.class, id);
        return callback(br.getParas());
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(Long id) {
        SystemLog br=baseService.getObject(SystemLog.class, id);
        return callback(br);
    }

}
