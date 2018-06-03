package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/17.
 */

import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.CityLocation;
import cn.mulanbay.pms.persistent.domain.LifeExperience;
import cn.mulanbay.pms.web.bean.request.life.CityLocationSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 城市位置
 */
@Controller
@RequestMapping("/cityLocation")
public class CityLocationController  extends BaseController  {

    private static Class<CityLocation> beanClass = CityLocation.class;


    @RequestMapping(value = "list")
    public String list() {
        return "life/cityLocationList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(CityLocationSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("createdTime", Sort.DESC);
        pr.addSort(s);
        PageResult<LifeExperience> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(CityLocation bean) {
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
    public ResultBean get(Long id) {
        CityLocation br=baseService.getObject(CityLocation.class, id);
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(CityLocation bean) {
        bean.setLastModifyTime(new Date());
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

        baseService.deleteObjects(CityLocation.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
        return callback(null);
    }

    /**
     * 初始化数据
     * 测试使用，当时第一次初始化数据时使用，原始数据来自于百度
     *
     * @return
     */
    @RequestMapping(value = "/intData", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean intData() {
        String s = JsonUtil.beanToJson(this.request.getParameterMap());
        String datastring = s.substring(2,s.length()-7).replaceAll("\\\\","");
        Map<String,List> data = (Map<String, List>) JsonUtil.jsonToBean(datastring,Map.class);
        List<CityLocation> list = new ArrayList<>();
        for(String key :data.keySet()){
            List ll = data.get(key);
            CityLocation bean = new CityLocation();
            bean.setCreatedTime(new Date());
            bean.setLat(Double.valueOf(ll.get(1).toString()));
            bean.setLocation(key);
            bean.setLon(Double.valueOf(ll.get(0).toString()));
            bean.setRemark("自动导入");
            list.add(bean);
        }
        baseService.saveObjects(list);
        return callback(null);
    }

    /**
     * 获取GeoCoordMap
     * 测试使用
     * @return
     */
    @RequestMapping(value = "/getGeoCoordMapData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getGeoCoordMapData(){
        Map<String,double[]> result = new HashMap<>();
        result.put("上海",new double[]{121.48,31.22});
        result.put("武汉",new double[]{114.31,30.52});
        return callback(result);
    }

}
