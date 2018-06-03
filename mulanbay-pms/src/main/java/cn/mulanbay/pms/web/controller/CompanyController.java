package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.Company;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.CommonTreeSearch;
import cn.mulanbay.pms.web.bean.request.work.CompanyFormRequest;
import cn.mulanbay.pms.web.bean.request.work.CompanySearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/10.
 */
@Controller
@RequestMapping("/company")
public class CompanyController  extends BaseController  {

    private static Class<Company> beanClass = Company.class;

    /**
     *
     * @return
     */
    @RequestMapping(value = "/getCompanyTree")
    @ResponseBody
    public ResultBean getCompanyTree(CommonTreeSearch cts) {

        try {
            CompanySearch sf = new CompanySearch();
            //sf.setUserId(cts.getUserId());
            PageResult<Company> pr =this.getCompanyResult(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<Company> gtList = pr.getBeanList();
            for (Company gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            if (cts.getNeedRoot() != null && cts.getNeedRoot()) {
                TreeBean root = new TreeBean();
                root.setId(null);
                root.setText("根");
                root.setChildren(list);
                List<TreeBean> newList = new ArrayList<TreeBean>();
                newList.add(root);
                return callback(newList);
            } else {
                return callback(list);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取公司树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "work/companyList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(CompanySearch sf) {
        return callbackDataGrid(getCompanyResult(sf));
    }

    private PageResult<Company> getCompanyResult(CompanySearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("entryDate",Sort.DESC);
        pr.addSort(sort);
        PageResult<Company> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid CompanyFormRequest formRequest) {
        Company bean = new Company();
        BeanCopy.copyProperties(formRequest,bean);
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
        Company bean=this.getUserEntity(Company.class, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid CompanyFormRequest formRequest) {
        Company bean=this.getUserEntity(Company.class, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
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
        this.deleteUserEntity(Company.class,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }
}
