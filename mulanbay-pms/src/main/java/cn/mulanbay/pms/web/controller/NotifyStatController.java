package cn.mulanbay.pms.web.controller;

import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.NotifyResult;
import cn.mulanbay.pms.persistent.domain.UserNotify;
import cn.mulanbay.pms.persistent.service.NotifyService;
import cn.mulanbay.pms.web.bean.request.notify.NotifyStatSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/2.
 */
@Controller
@RequestMapping("/notifyStat")
public class NotifyStatController extends BaseController {

    @Value("${system.notifyStat.cache}")
    boolean statCache;

    @Autowired
    NotifyService notifyService;

    @RequestMapping(value = "list")
    public String list() {
        return "notify/notifyStatList";
    }

    /**
     * 首页提醒列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(NotifyStatSearch sf) {
        PageRequest pr = sf.buildQuery();
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        pr.setBeanClass(UserNotify.class);

        PageResult<UserNotify> unResult = baseService.getBeanResult(pr);
        List<NotifyResult> list = new ArrayList<>();
        int i=1;
        for(UserNotify un : unResult.getBeanList()){
            NotifyResult nr =null;
            if(statCache){
                nr = notifyService.getNotifyResultForCache(un,sf.getUserId());
            }else{
                nr = notifyService.getNotifyResult(un,sf.getUserId());
            }
            nr.setId(i);
            list.add(nr);
            i++;
        }
        PageResult<NotifyResult> res = new PageResult<>(sf.getPage(),sf.getPageSize());
        res.setBeanList(list);
        res.setMaxRow(unResult.getMaxRow());
        return callbackDataGrid(res);
    }
}
