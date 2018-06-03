package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.TreatOperationStat;
import cn.mulanbay.pms.persistent.domain.TreatOperation;
import cn.mulanbay.pms.persistent.domain.TreatRecord;
import cn.mulanbay.pms.persistent.service.TreatService;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.health.TreatOperationFormRequest;
import cn.mulanbay.pms.web.bean.request.health.TreatOperationSearch;
import cn.mulanbay.pms.web.bean.request.health.TreatOperationStatSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/31.
 * 看病记录中的手术
 */
@Controller
@RequestMapping("/treatOperation")
public class TreatOperationController  extends BaseController {

    private static Class<TreatOperation> beanClass = TreatOperation.class;

    @Autowired
    TreatService treatService;

    @RequestMapping(value = "list")
    public String list() {
        return "health/treatOperationList";
    }

    /**
     * 获取列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(TreatOperationSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("createdTime", Sort.DESC);
        pr.addSort(s);
        PageResult<TreatOperation> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid TreatOperationFormRequest formRequest ) {
        TreatOperation bean = new TreatOperation();
        BeanCopy.copyProperties(formRequest,bean);
        TreatRecord treatRecord = this.getUserEntity(TreatRecord.class, formRequest.getTreatRecordId(),formRequest.getUserId());
        bean.setTreatRecord(treatRecord);
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
        TreatOperation bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid TreatOperationFormRequest formRequest) {
        TreatOperation bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        TreatRecord treatRecord = this.getUserEntity(TreatRecord.class, formRequest.getTreatRecordId(),formRequest.getUserId());
        bean.setTreatRecord(treatRecord);        bean.setLastModifyTime(new Date());
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
        baseService.deleteObjects(TreatOperation.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
        return callback(null);
    }

    /**
     * 总的概要统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(TreatOperationStatSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        List<TreatOperationStat> data = treatService.treatOperationStat(pr);
        TreatOperationStat total = new TreatOperationStat();
        BigInteger n=BigInteger.ZERO;
        BigDecimal totalFee=BigDecimal.ZERO;
        for(TreatOperationStat bean : data){
            n=n.add(bean.getTotalCount());
            totalFee =totalFee.add(bean.getTotalFee());
        }
        total.setName("小计");
        total.setTotalCount(n);
        total.setTotalFee(totalFee);
        data.add(total);
        return callback(data);

    }


}
