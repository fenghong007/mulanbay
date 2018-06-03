package cn.mulanbay.pms.web.controller;

import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.CacheKey;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.persistent.domain.UserCalendar;
import cn.mulanbay.pms.persistent.enums.UserCalendarFinishType;
import cn.mulanbay.pms.persistent.service.UserCalendarService;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.usercalendar.UserCalendarSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-03-28 16:57
 */
@Controller
@RequestMapping("/userCalendar")
public class UserCalendarController extends BaseController {

    private static Class<UserCalendar> beanClass = UserCalendar.class;

    @Autowired
    UserCalendarService userCalendarService;

    @Autowired
    CacheHandler cacheHandler;

    @RequestMapping(value = "list")
    public String list() {
        return "userCalendar/userCalendarList";
    }


    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(UserCalendarSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("createdTime", Sort.DESC);
        pr.addSort(s);
        PageResult<UserCalendar> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 今日行程
     *
     * @return
     */
    @RequestMapping(value = "/todayCalendarList")
    @ResponseBody
    public ResultBean todayCalendarList() {
        List<UserCalendar> list = userCalendarService.getCurrentUserCalendarList(this.getCurrentUserId());
        return callback(list);
    }

    /**
     * 完成今日行程
     *
     * @return
     */
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean finish(@Valid CommonBeanGetRequest getRequest) {
        UserCalendar userCalendar = baseService.getObjectWithUser(UserCalendar.class,getRequest.getId(),getRequest.getUserId());
        Date date = new Date();
        userCalendar.setFinishedTime(date);
        userCalendar.setLastModifyTime(date);
        userCalendar.setExpireTime(date);
        userCalendar.setFinishType(UserCalendarFinishType.MANUAL);
        baseService.updateObject(userCalendar);
        //删除缓存ß
        String key= MessageFormat.format(CacheKey.USER_TODAY_CALENDAR_COUNTS,getRequest.getUserId());
        cacheHandler.delete(key);
        return callback(null);
    }

}
