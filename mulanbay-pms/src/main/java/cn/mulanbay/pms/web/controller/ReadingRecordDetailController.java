package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.ReadingRecord;
import cn.mulanbay.pms.persistent.domain.ReadingRecordDetail;
import cn.mulanbay.pms.persistent.service.ReadingRecordService;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.read.ReadingRecordDetailFormRequest;
import cn.mulanbay.pms.web.bean.request.read.ReadingRecordDetailSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/readingRecordDetail")
public class ReadingRecordDetailController extends BaseController {

    private static Class<ReadingRecordDetail> beanClass = ReadingRecordDetail.class;

    @Autowired
    ReadingRecordService readingRecordService;

    @RequestMapping(value = "list")
    public String list() {
        return "read/readingRecordDetailList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(ReadingRecordDetailSearch sf) {
        return callbackDataGrid(getReadingRecordDetailResult(sf));
    }

    private PageResult<ReadingRecordDetail> getReadingRecordDetailResult(ReadingRecordDetailSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort2 =new Sort("readTime",Sort.DESC);
        pr.addSort(sort2);
        PageResult<ReadingRecordDetail> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid ReadingRecordDetailFormRequest formRequest ) {
        ReadingRecordDetail bean = new ReadingRecordDetail();
        BeanCopy.copyProperties(formRequest,bean);
        ReadingRecord readingRecord=this.getUserEntity(ReadingRecord.class, formRequest.getReadingRecordId(),formRequest.getUserId());
        bean.setReadingRecord(readingRecord);
        bean.setCreatedTime(new Date());
        readingRecordService.saveOrUpdateReadingRecordDetail(bean,false);
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
        ReadingRecordDetail bean = this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid ReadingRecordDetailFormRequest formRequest) {
        ReadingRecordDetail bean = this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        ReadingRecord readingRecord=this.getUserEntity(ReadingRecord.class, formRequest.getReadingRecordId(),formRequest.getUserId());
        bean.setReadingRecord(readingRecord);
        bean.setLastModifyTime(new Date());
        readingRecordService.saveOrUpdateReadingRecordDetail(bean,true);
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
