package cn.mulanbay.pms.web.controller;

import cn.mulanbay.pms.persistent.service.GeoService;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-21 17:34
 */
@Controller
@RequestMapping("/city")
public class CityController extends BaseController {

    @Autowired
    GeoService geoService;

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getCityList")
    @ResponseBody
    public ResultBean getCityList(Integer provinceId) {
        return callback(geoService.getCityList(provinceId));
    }

}
