package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.BusinessTrip;
import cn.mulanbay.pms.persistent.domain.Company;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.work.BusinessTripFormRequest;
import cn.mulanbay.pms.web.bean.request.work.BusinessTripSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by fenghong on 2017/2/3.
 */
@Controller
@RequestMapping("/businessTrip")
public class BusinessTripController extends BaseController {


    private static Class<BusinessTrip> beanClass = BusinessTrip.class;

    @RequestMapping(value = "list")
    public String list() {
        return "work/businessTripList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(BusinessTripSearch sf) {
        return callbackDataGrid(getBusinessTripResult(sf));
    }

    private PageResult<BusinessTrip> getBusinessTripResult(BusinessTripSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("tripDate",Sort.DESC);
        pr.addSort(sort);
        PageResult<BusinessTrip> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid BusinessTripFormRequest formRequest) {
        Company company=this.getUserEntity(Company.class, formRequest.getCompanyId(),formRequest.getUserId());
        BusinessTrip bean = new BusinessTrip();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCompany(company);
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
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        BusinessTrip bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid BusinessTripFormRequest formRequest) {
        BusinessTrip bean=this.getUserEntity(BusinessTrip.class, formRequest.getId(),formRequest.getUserId());
        Company company=this.getUserEntity(Company.class, formRequest.getCompanyId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCompany(company);
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
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        this.deleteUserEntity(beanClass,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

}
