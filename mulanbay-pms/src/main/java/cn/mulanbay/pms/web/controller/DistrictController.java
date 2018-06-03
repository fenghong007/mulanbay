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
 * @create 2018-02-21 17:35
 */
@Controller
@RequestMapping("/district")
public class DistrictController extends BaseController{

    @Autowired
    GeoService geoService;

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getDistrictList")
    @ResponseBody
    public ResultBean getDistrictList(Integer cityId) {
        return callback(geoService.getDistrictList(cityId));
    }

}
