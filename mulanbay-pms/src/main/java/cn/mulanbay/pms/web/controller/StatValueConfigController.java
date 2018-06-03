package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.StatValueConfigBean;
import cn.mulanbay.pms.persistent.domain.StatValueConfig;
import cn.mulanbay.pms.persistent.service.StatValueConfigService;
import cn.mulanbay.pms.web.bean.request.common.GetStatValueConfig;
import cn.mulanbay.pms.web.bean.request.common.GetStatValueConfigs;
import cn.mulanbay.pms.web.bean.request.plan.StatValueConfigSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/statValueConfig")
public class StatValueConfigController extends BaseController {

    private static Class<StatValueConfig> beanClass = StatValueConfig.class;

    @Autowired
    StatValueConfigService statValueConfigService;

    /**
     * 获取任务列表
     * 使用RequestParam 方式是因为easyui 的datagrid使用ajax请求时对于list多值类型的参数参数名会带上中挂号
     * 且无法在js请求方式里设置使用 traditional:true参数
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(StatValueConfigSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<StatValueConfig> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }


    /**
     * 获取值的配置列表
     * @param sf
     * @return
     */
    @RequestMapping(value = "/getConfigs", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getConfigs(GetStatValueConfigs sf) {
        List<StatValueConfigBean> list = statValueConfigService.getStatValueConfig(sf.getFid(),sf.getType(),sf.getUserId());
        return callback(list);
    }

    /**
     * 获取值的配置列表
     * @param sf
     * @return
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getConfig(GetStatValueConfig sf) {
        StatValueConfig svc =baseService.getObject(StatValueConfig.class,sf.getId());
        StatValueConfigBean bean = statValueConfigService.getStatValueConfigBean(svc,sf.getPid(),sf.getUserId());
        return callback(bean);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(StatValueConfig bean) {
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
        StatValueConfig br=baseService.getObject(StatValueConfig.class, id);
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(StatValueConfig bean) {
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
        baseService.deleteObjects(StatValueConfig.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
        return callback(null);
    }
}
