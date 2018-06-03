package cn.mulanbay.pms.web.controller;

import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.domain.UserMessage;
import cn.mulanbay.pms.persistent.service.LogService;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.log.UserMessageSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by fenghong on 2017/1/10.
 */
@Controller
@RequestMapping("/userMessage")
public class UserMessageController extends BaseController  {

    private static Class<UserMessage> beanClass = UserMessage.class;

    @Autowired
    LogService logService;

    @Autowired
    PmsNotifyHandler pmsNotifyHandler;

    @RequestMapping(value = "list")
    public String list() {
        return "log/userMessageList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserMessageSearch sf) {
        return callbackDataGrid(getUserMessageResult(sf));
    }

    private PageResult<UserMessage> getUserMessageResult(UserMessageSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("createdTime",Sort.DESC);
        pr.addSort(sort);
        PageResult<UserMessage> qr = baseService.getBeanResult(pr);
        return qr;
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(Long id) {
        UserMessage br=baseService.getObject(UserMessage.class, id);
        return callback(br);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getByUser", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getByUser(@Valid CommonBeanGetRequest ubg) {
        UserMessage br=baseService.getObjectWithUser(beanClass, ubg.getId(), ubg.getUserId());
        return callback(br);
    }

    /**
     * 重新发送
     *
     * @return
     */
    @RequestMapping(value = "/resend", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean resend(Long id) {
        UserMessage br=baseService.getObject(UserMessage.class, id);
        pmsNotifyHandler.sendMessageAsync(br);
        return callback(br);
    }

}
