package cn.mulanbay.pms.web.controller;

import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-01-22 21:32
 */
@Controller
@RequestMapping("/maintain")
public class MaintainController extends BaseController {

    @Autowired
    CacheHandler cacheHandler;

    /**
     * 删除缓存
     *
     * @return
     */
    @RequestMapping(value = "/deleteCache", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean deleteCache(String key){
        cacheHandler.delete(key);
        return callback(null);
    }

    /**
     * 获取缓存
     *
     * @return
     */
    @RequestMapping(value = "/getCache", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCache(String key){
        return callback(cacheHandler.get(key));
    }

}
