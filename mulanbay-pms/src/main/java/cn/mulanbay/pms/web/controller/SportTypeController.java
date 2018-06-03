package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.SportType;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.sport.SportTypeFormRequest;
import cn.mulanbay.pms.web.bean.request.sport.SportTypeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/1/30.
 * 运动类型
 */
@Controller
@RequestMapping("/sportType")
public class SportTypeController  extends BaseController  {

    private static Class<SportType> beanClass = SportType.class;

    /**
     * 为锻炼管理界面的下拉菜单使用
     * @return
     */
    @RequestMapping(value = "/getSportTypeTree")
    @ResponseBody
    public ResultBean getSportTypeTree(SportTypeSearch sf) {

        try {
            sf.setStatus(CommonStatus.ENABLE);
            PageResult<SportType> pr = getSportTypePageResult(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<SportType> gtList = pr.getBeanList();
            for (SportType gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取运动类型树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "sport/sportTypeList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(SportTypeSearch sf) {
        return callbackDataGrid(getSportTypePageResult(sf));
    }

    private PageResult<SportType> getSportTypePageResult(SportTypeSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("orderIndex",Sort.ASC);
        pr.addSort(sort);
        PageResult<SportType> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid SportTypeFormRequest formRequest) {
        SportType bean = new SportType();
        BeanCopy.copyProperties(formRequest,bean);
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
        SportType bean=this.getUserEntity(beanClass, getRequest.getId().intValue(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid SportTypeFormRequest formRequest) {
        SportType bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
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
                NumberUtil.stringArrayToIntegerArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }
}
