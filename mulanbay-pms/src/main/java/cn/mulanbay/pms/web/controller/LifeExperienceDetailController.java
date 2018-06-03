package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/13.
 */

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.CityLocation;
import cn.mulanbay.pms.persistent.domain.LifeExperience;
import cn.mulanbay.pms.persistent.domain.LifeExperienceDetail;
import cn.mulanbay.pms.persistent.service.LifeExperienceService;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceDetailFormRequest;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceDetailSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * 人生经历明细
 */
@Controller
@RequestMapping("/lifeExperienceDetail")
public class LifeExperienceDetailController extends BaseController{
    
    private static Class<LifeExperienceDetail> beanClass = LifeExperienceDetail.class;

    @Autowired
    LifeExperienceService lifeExperienceService;

    @RequestMapping(value = "list")
    public String list() {
        return "life/lifeExperienceDetailList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    public ResultBean getData(LifeExperienceDetailSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("occurDate", Sort.ASC);
        pr.addSort(s);
        PageResult<LifeExperienceDetail> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid LifeExperienceDetailFormRequest formRequest ) {
        LifeExperienceDetail bean = new LifeExperienceDetail();
        BeanCopy.copyProperties(formRequest,bean);
        LifeExperience lifeExperience = this.getUserEntity(LifeExperience.class,formRequest.getLifeExperienceId(),formRequest.getUserId());
        bean.setLifeExperience(lifeExperience);
        checkLocation(bean.getStartCity());
        checkLocation(bean.getArriveCity());
        bean.setCost(0.0);
        bean.setCreatedTime(new Date());
        lifeExperienceService.saveOrUpdateLifeExperienceDetail(bean,true);
        return callback(bean);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        LifeExperienceDetail bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid LifeExperienceDetailFormRequest formRequest) {
        LifeExperienceDetail bean=this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        LifeExperience lifeExperience = this.getUserEntity(LifeExperience.class,formRequest.getLifeExperienceId(),formRequest.getUserId());
        bean.setLifeExperience(lifeExperience);
        checkLocation(bean.getStartCity());
        checkLocation(bean.getArriveCity());
        bean.setLastModifyTime(new Date());
        lifeExperienceService.saveOrUpdateLifeExperienceDetail(bean,true);
        return callback(bean);
    }

    /**
     * 检查location是否存在,如果没有创建地理位置信息会导致迁徙的地图加载出差
     * @param name
     * @return
     */
    private void checkLocation(String name){
        CityLocation cl=lifeExperienceService.getCityLocationByLocation(name);
        if(cl==null){
            throw new ApplicationException(10035,"["+name+"]的地理位置信息不存在,请先去创建相关地理位置信息");
        }
    }
    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        Long[] ll =  NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(","));
        for(Long id : ll){
            lifeExperienceService.deleteLifeExperienceDetail(id,deleteRequest.getUserId(),false);
        }
        return callback(null);
    }

}
